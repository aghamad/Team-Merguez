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
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static final Log LOGGER = LogFactory.getLog(Bibliotheque.class);

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
        if(arguments.length < 1) {
            Bibliotheque.LOGGER.info("Usage: <fichier-transactions>");
            return;
        }

        try {
            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[0]);
            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionnaireBibliotheque = new BibliothequeCreateur();
                Bibliotheque.traiterTransactions(reader);
            }
        } catch(Exception exception) {
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
            exception.printStackTrace(System.out);
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
        Bibliotheque.LOGGER.info("\n\n\n");
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
            Bibliotheque.LOGGER.info("> "
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
        final String command = tokenizer.nextToken();

        switch(command) {
            case "aide":
                Bibliotheque.afficherAide();
                break;
            case "inscrire":
                Bibliotheque.inscrireMembre(tokenizer);
                break;
            case "deinscrire":
                Bibliotheque.desinscrireMembre(tokenizer);
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
            //   case "retourner":
            //         Bibliotheque.terminerPret(tokenizer);
            //       break;
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
                Bibliotheque.LOGGER.error(" Transactions non reconnu . Essayer \"aide\"");
        }
    }

    /**
     * Annuler  un reservation.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void annulerReservation(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
            final String idReservation = Bibliotheque.readString(tokenizer);
            final ReservationDTO reservationDTO = (ReservationDTO) Bibliotheque.gestionnaireBibliotheque.getReservationFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idReservation);
            if(reservationDTO == null) {
                throw new MissingDTOException("La réservation "
                    + idReservation
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().annuler(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | InvalidPrimaryKeyException
            | MissingDTOException e) {
            /*
            Bibliotheque.LOGGER.error(" **** "
                + exception.getMessage()); */
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }

    }

    /**
     * Reserver un livre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void placerReservation(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
            final String idmembre = Bibliotheque.readString(tokenizer);

            final MembreDTO membreDTO = (MembreDTO) Bibliotheque.gestionnaireBibliotheque.getMembreFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idmembre);
            if(membreDTO == null) {
                throw new MissingDTOException("La membre "
                    + idmembre
                    + " n'existe pas");
            }

            final String idLivre = Bibliotheque.readString(tokenizer);

            final LivreDTO livreDTO = (LivreDTO) Bibliotheque.gestionnaireBibliotheque.getLivreFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("La livre "
                    + idLivre
                    + " n'existe pas");
            }

            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setLivreDTO(livreDTO);
            reservationDTO.setMembreDTO(membreDTO);

            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().placer(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException
            | MissingLoanException
            | InvalidPrimaryKeyException
            | MissingDTOException e) {
            /*
            Bibliotheque.LOGGER.error(" **** "
                + exception.getMessage()); */
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Preter un livre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void preterLivre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setMembreDTO(membreDTO);
            pretDTO.setLivreDTO(livreDTO);
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(Bibliotheque.gestionnaireBibliotheque.getSession(),
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException
            | InvalidLoanLimitException e) {
            /*
            Bibliotheque.LOGGER.error(" **** "
                + exception.getMessage()); */
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }

    }

    /**
     * Vendre un livre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void vendreLivre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idLivre = Bibliotheque.readString(tokenizer);

            final LivreDTO livreDTO = (LivreDTO) Bibliotheque.gestionnaireBibliotheque.getLivreFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idLivre);

            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().vendre(Bibliotheque.gestionnaireBibliotheque.getSession(),
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException
            | MissingDTOException
            | InvalidPrimaryKeyException e) {

            Bibliotheque.LOGGER.error(" **** "
                + e.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     *
     * Renouveler un prêt.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void renouvelerPret(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final PretDTO pretDTO = new PretDTO();
            pretDTO.setIdPret(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().renouveler(Bibliotheque.gestionnaireBibliotheque.getSession(),
                pretDTO);
        } catch(
            BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | ExistingReservationException
            | MissingLoanException e) {

            Bibliotheque.LOGGER.error(" **** "
                + e.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }

    }

    /**
     *
     * Utiliser une reservation.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void utiliserReservation(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));

            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().utiliser(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException e) {

            Bibliotheque.LOGGER.error(" **** "
                + e.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     *
     * Acquerir un livre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void acquerirLivre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setTitre(Bibliotheque.readString(tokenizer));
            livreDTO.setAuteur(Bibliotheque.readString(tokenizer));
            livreDTO.setDateAcquisition(Bibliotheque.readDate(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().acquerir(Bibliotheque.gestionnaireBibliotheque.getSession(),
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException e) {
            Bibliotheque.LOGGER.error(" **** "
                + e.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }

    }

    /**
     *
     * Inscrit un membre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void inscrireMembre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setNom(Bibliotheque.readString(tokenizer));
            membreDTO.setTelephone(Bibliotheque.readString(tokenizer));
            membreDTO.setLimitePret(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().inscrire(Bibliotheque.gestionnaireBibliotheque.getSession(),
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exception) {

            Bibliotheque.LOGGER.error(" **** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     *
     * Deinscrit un membre.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void desinscrireMembre(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idMembre = Bibliotheque.readString(tokenizer);
            final MembreDTO membreDTO = (MembreDTO) Bibliotheque.gestionnaireBibliotheque.getMembreFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }

            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().desinscrire(Bibliotheque.gestionnaireBibliotheque.getSession(),
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | InvalidDTOException
            | FacadeException
            | ExistingLoanException
            | ExistingReservationException
            | MissingDTOException exception) {

            Bibliotheque.LOGGER.error("**** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {
        Bibliotheque.LOGGER.info("");
        Bibliotheque.LOGGER.info("Chaque transaction comporte un nom et une liste d'arguments");
        Bibliotheque.LOGGER.info("séparés par des espaces. La liste peut être vide.");
        Bibliotheque.LOGGER.info(" Les dates sont en format yyyy-mm-dd.\n");
        Bibliotheque.LOGGER.info("Les transactions sont :");
        Bibliotheque.LOGGER.info("  aide");
        Bibliotheque.LOGGER.info("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        Bibliotheque.LOGGER.info("  preter <idMembre> <idLivre>");
        Bibliotheque.LOGGER.info("  renouveler <idLivre>");
        Bibliotheque.LOGGER.info("  retourner <idLivre>");
        Bibliotheque.LOGGER.info("  vendre <idLivre>");
        Bibliotheque.LOGGER.info("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        Bibliotheque.LOGGER.info("  desinscrire <idMembre>");
        Bibliotheque.LOGGER.info("  reserver <idReservation> <idMembre> <idLivre>");
        Bibliotheque.LOGGER.info("  utiliser <idReservation>");
        Bibliotheque.LOGGER.info("  annuler <idReservation>");
        // Bibliotheque.LOGGER.info("  listerLivresRetard <dateCourante>");
        // Bibliotheque.LOGGER.info("  listerLivresTitre <mot>");
        // Bibliotheque.LOGGER.info("  listerLivres");
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
