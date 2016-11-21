// Fichier Bibliotheque.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;

/**
 * Interface du système de gestion d'une bibliothèque.<br /><br />
 *
 * Ce programme permet d'appeler les transactions de base d'une bibliothèque.  Il gere des livres, des membres et des réservations. Les données
 * sont conservées dans une base de données relationnelles accédée avec JDBC. Pour une liste des transactions traitées, voir la méthode
 * {@link Bibliotheque#afficherAide() afficherAide()}.<br /><br />
 *
 * Paramètres :<br />
 * 0- site du serveur SQL ("local", "distant" ou "postgres")<br />
 * 1- nom de la BD<br />
 * 2- user id pour établir une connexion avec le serveur SQL<br />
 * 3- mot de passe pour le user id<br />
 * 4- fichier de transaction<br /><br />
 *
 * Pré-condition :<br />
 *   La base de données de la bibliothèque doit exister<br /><br />
 *
 * Post-condition :<br />
 *   Le programme effectue les maj associées à chaque transaction
 *
 * @author Team-Merguez
 */
public final class Bibliotheque {
    private static BibliothequeCreateur gestionnaireBibliotheque;

    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private Bibliotheque() {
        super();
    }

    /**
     * Crée une connexion sur la base de données, traite toutes les transactions et détruit la connexion.
     *
     * @param arguments Les arguments du main
     * @throws Exception Si une erreur survient
     */
    public static void main(String[] arguments) throws Exception {
        // Validation du nombre de paramètres
        if(arguments.length < 5) {
            System.out.println("Usage: java Bibliotheque <serveur> <bd> <user> <password> <fichier-transactions>");
            System.out.println(Connexion.getServeursSupportes());
            return;
        }

        try {
            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[4]);
            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionnaireBibliotheque = new BibliothequeCreateur(arguments[0],
                    arguments[1],
                    arguments[2],
                    arguments[3]);
                Bibliotheque.traiterTransactions(reader);
            }
        } catch(Exception exception) {
            Bibliotheque.gestionnaireBibliotheque.rollback();
            exception.printStackTrace(System.out);
        } finally {
            Bibliotheque.gestionnaireBibliotheque.close();
        }
    }

    /**
     * Traite le fichier de transactions.
     *
     * @param reader Le flux d'entrée à lire
     * @throws Exception Si une erreur survient
     */
    private static void traiterTransactions(BufferedReader reader) throws Exception {
        Bibliotheque.afficherAide();
        System.out.println("\n\n\n");
        String transaction = Bibliotheque.lireTransaction(reader);
        while(!Bibliotheque.finTransaction(transaction)) {
            // Découpage de la transaction en mots
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                Bibliotheque.executerTransaction(tokenizer);
            }
            transaction = Bibliotheque.lireTransaction(reader);
        }
    }

    /**
     * Lit une transaction.
     *
     * @param reader Le flux d'entrée à lire
     * @return La transaction lue
     * @throws IOException Si une erreur de lecture survient
     */
    private static String lireTransaction(BufferedReader reader) throws IOException {
        final String transaction = reader.readLine();
        if(transaction != null) {
            System.out.println("> "
                + transaction);
        }
        return transaction;
    }

    /**
     * Décode et traite une transaction.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void executerTransaction(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            final String command = tokenizer.nextToken();

            switch(command) {
                case "aide":
                    Bibliotheque.afficherAide();
                    break;
                case "inscrire":
                    Bibliotheque.inscrireMembre(tokenizer);
                    break;
                case "deinscrire":
                    Bibliotheque.deinscrireMembre(tokenizer);
                    break;
                case "acquerir":
                    Bibliotheque.acquerirLivre(tokenizer);
                    break;
                case "vendre":
                    Bibliotheque.vendreLivre(tokenizer);
                    break;
                case "preter":
                    Bibliotheque.preterLivre(tokenizer);
                    break;
                case "renouveler":
                    Bibliotheque.renouvelerPret(tokenizer);
                    break;
                case "retourner":
                    Bibliotheque.terminerPret(tokenizer);
                    break;
                case "reserver":
                    Bibliotheque.placerReservation(tokenizer);
                    break;
                case "utiliser":
                    Bibliotheque.utiliserReservation(tokenizer);
                    break;
                case "annuler":
                    Bibliotheque.annulerReservation(tokenizer);
                    break;
                case "--":
                    break;
                default:
                    Bibliotheque.LOGGER.info(" Transactions non reconnu . Essayer \"aide\"");
            }

            if("aide".equals(command)) {
                Bibliotheque.afficherAide();
            } else if("acquerir".equals(command)) {
                final LivreDTO livreDTO = new LivreDTO();

                livreDTO.setTitre(Bibliotheque.readString(tokenizer));
                livreDTO.setAuteur(Bibliotheque.readString(tokenizer));
                livreDTO.setDateAcquisition(Bibliotheque.readDate(tokenizer));
                Bibliotheque.gestionnaireBibliotheque.getLivreFacade().acquerir(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    livreDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("vendre".equals(command)) {
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
                Bibliotheque.gestionnaireBibliotheque.getLivreFacade().vendre(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    livreDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("preter".equals(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
                final PretDTO pretDTO = new PretDTO();
                pretDTO.setMembreDTO(membreDTO);
                pretDTO.setLivreDTO(livreDTO);
                Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    pretDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();

            } else if("renouveler".equals(command)) {

                final PretDTO pretDTO = new PretDTO();
                pretDTO.setIdPret(Bibliotheque.readString(tokenizer));

                Bibliotheque.gestionnaireBibliotheque.getPretFacade().renouveler(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    pretDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("inscrire".equals(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setNom(Bibliotheque.readString(tokenizer));
                membreDTO.setTelephone(Bibliotheque.readLong(tokenizer));
                membreDTO.setLimitePret(Bibliotheque.readInt(tokenizer));
                Bibliotheque.gestionnaireBibliotheque.getMembreFacade().inscrire(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    membreDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("desinscrire".equals(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
                Bibliotheque.gestionnaireBibliotheque.getMembreFacade().desinscrire(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    membreDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("reserver".equals(command)) {
                // Juste pour éviter deux timestamps de réservation strictement identiques
                Thread.sleep(1);
                final ReservationDTO reservationDTO = new ReservationDTO();
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));

                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));

                reservationDTO.setLivreDTO(livreDTO);
                reservationDTO.setMembreDTO(membreDTO);

                Bibliotheque.gestionnaireBibliotheque.getReservationFacade().placer(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    reservationDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("retourner".equals(command)) {

                final PretDTO pretDTO = new PretDTO();
                pretDTO.setIdPret(Bibliotheque.readString(tokenizer));

                Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    pretDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();

            } else if("utiliser".equals(command)) {

                final ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));

                Bibliotheque.gestionnaireBibliotheque.getReservationFacade().utiliser(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    reservationDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
            } else if("annuler".equals(command)) {
                final ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
                Bibliotheque.gestionnaireBibliotheque.getReservationFacade().annuler(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
                    reservationDTO);
                Bibliotheque.gestionnaireBibliotheque.commit();
                // } else if("listerLivres".equals(command)) {
                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivres();
                // } else if("listerLivresRetard".equals(command)) {
                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivresRetard(readString(tokenizer) /* date courante */);
                // } else if("listerLivresTitre".equals(command)) {
                //     Bibliotheque.gestionBibliothque.livreDAO.listerLivresTitre(readString(tokenizer) /* mot */);
            } else if(!"--".equals(command)) {
                System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            }
        } catch(
            InvalidDTOClassException
            | BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException
            | FacadeException
            | MissingLoanException
            | InterruptedException exception) {
            System.out.println("** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollback();
        }
    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("séparés par des espaces. La liste peut être vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.\n");
        System.out.println("Les transactions sont :");
        System.out.println("  aide");
        System.out.println("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        System.out.println("  preter <idMembre> <idLivre>");
        System.out.println("  renouveler <idLivre>");
        System.out.println("  retourner <idLivre>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  reserver <idReservation> <idMembre> <idLivre>");
        System.out.println("  utiliser <idReservation>");
        System.out.println("  annuler <idReservation>");
        // System.out.println("  listerLivresRetard <dateCourante>");
        // System.out.println("  listerLivresTitre <mot>");
        // System.out.println("  listerLivres");
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     *
     * @param transaction La transaction à traiter
     * @return <code>true</code> Si la fin du fichier est atteinte, <code>false</code> sinon
     */
    private static boolean finTransaction(String transaction) {
        boolean finDeFichier = transaction == null;
        if(!finDeFichier) {
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            finDeFichier = !tokenizer.hasMoreTokens();
            if(!finDeFichier) {
                final String commande = tokenizer.nextToken();
                finDeFichier = "exit".equals(commande);
            }
        }

        return finDeFichier;
    }

    /**
     * Lit une chaîne de caractères de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La chaîne de caractères lue
     * @throws BibliothequeException Si l'élément lu est manquant
     */
    private static String readString(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

    /**
     * Lit un integer de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return Le integer lu
     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas un integer
     */
    private static int readInt(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return Integer.valueOf(token).intValue();
            } catch(NumberFormatException numberFormatException) {
                throw new BibliothequeException("Nombre attendu à la place de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

    /**
     * Lit un long de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return Le long lu
     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas un long
     */
    private static long readLong(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return Long.valueOf(token).longValue();
            } catch(NumberFormatException numberFormatException) {
                throw new BibliothequeException("Nombre attendu à la place de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

    /**
     * Lit une date au format YYYY-MM-DD de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La date lue
     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas une date correctement formatée
     */
    private static Timestamp readDate(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return FormatteurDate.timestampValue(token);
            } catch(ParseException parseException) {
                throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

}
