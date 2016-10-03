// Fichier Biblio.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatDate;

/**
 * Interface du système de gestion d'une bibliothèque +.
 *
 *<p>Ce programme permet d'appeler les transactions de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédée avec JDBC. Pour une liste des
 * transactions traitées, voir la méthode afficherAide().
 *
 * <p>Paramètres
 * 0- site du serveur SQL ("local", "distant" ou "postgres")
 * 1- nom de la BD
 * 2- user id pour �tablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 *<p>Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * <p>Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * @author Team-Merguez
 */
public final class Bibliotheque {
    private static BibliothequeCreateur gestionBibliotheque;

    /**¸
    *
    * Constructeur privé pour empêcher toute instanciation.
    *
    */
    private Bibliotheque() {
        super();
    }

    /**
     *Crée une connexion sur la base de données, traite toutes les transactions et détruit la connexion.
     *@param arguments Les arguments du main.
     *@throws Exception -Si une erreur survient.
     */
    public static void main(String[] arguments) throws Exception {

        if(arguments.length < 5) {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password> <fichier-transactions>");
            System.out.println(Connexion.serveursSupportes());
            return;
        }

        try {
            /**
             *  ouverture du fichier de transactions
             * s'il est spécifié comme argument
             */

            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[4]);

            /*
             * try-with-resources Statement
             */

            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionBibliotheque = new BibliothequeCreateur(arguments[0],
                    arguments[1],
                    arguments[2],
                    arguments[3]);

                Bibliotheque.traiterTransactions(reader);
            }

        } catch(Exception exception) {
            Bibliotheque.gestionBibliotheque.rollback();
            exception.printStackTrace(System.out);
        } finally {
            Bibliotheque.gestionBibliotheque.close();
        }
    }

    /**
      *Traite le fichier de transactions.
      *@param reader - Le flux d'entrée à lire.
      *@throws Exception - Si une erreur survient.
      */
    private static void traiterTransactions(BufferedReader reader) throws Exception {
        Bibliotheque.afficherAide();
        System.out.println("\n\n\n");
        String transaction = Bibliotheque.lireTransaction(reader);
        while(!finTransaction(transaction)) {
            /* découpage de la transaction en mots. */
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                Bibliotheque.executerTransaction(tokenizer);
            }
            transaction = Bibliotheque.lireTransaction(reader);
        }
    }

    /**
      *Lit une transaction.
      *@param reader - Le flux d'entrée à lire
      *@return La transaction lue.
      *@throws IOException - Si une erreur de lecture survient.
      */
    private static String lireTransaction(BufferedReader reader) throws IOException {
        System.out.print("> ");
        final String transaction = reader.readLine();
        if(transaction != null) {
            /* echo si lecture dans un fichier.*/
            System.out.print("> :");
            System.out.println(transaction);
        }
        return transaction;
    }

    /**
      *Décode et traite une transaction.
      *@param tokenizer - L'entrée à décoder.
      *@throws BibliothequeException - Si une erreur survient.
      *@throws Exception lancer s'il y a une erreur de transaction
      */
    private static void executerTransaction(StringTokenizer tokenizer) throws Exception {
        try {
            final String command = tokenizer.nextToken();

            if("aide".startsWith(command)) {
                afficherAide();
            } else if("acquerir".startsWith(command)) {

                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readInt(tokenizer));
                livreDTO.setTitre(Bibliotheque.readString(tokenizer));
                livreDTO.setAuteur(Bibliotheque.readString(tokenizer));
                livreDTO.setDateAcquisition(Bibliotheque.readDate(tokenizer));
                Bibliotheque.gestionBibliotheque.getLivreService().acquerir(livreDTO);
                Bibliotheque.gestionBibliotheque.commit();

            } else if("vendre".startsWith(command)) {

                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readInt(tokenizer));
                Bibliotheque.gestionBibliotheque.getLivreService().vendre(livreDTO);
                Bibliotheque.gestionBibliotheque.commit();

            } else if("preter".startsWith(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(Bibliotheque.readInt(tokenizer));
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(Bibliotheque.readInt(tokenizer));
                Bibliotheque.gestionBibliotheque.getMembreService().emprunter(membreDTO,
                    livreDTO);

            } else if("renouveler".startsWith(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(Bibliotheque.readInt(tokenizer));
                final LivreDTO livreDTO = new LivreDTO();
                Bibliotheque.gestionBibliotheque.getMembreService().renouveler(membreDTO,
                    livreDTO);

            } else if("retourner".startsWith(command)) {
                final int idMembre = readInt(tokenizer);
                final int idLivre = readInt(tokenizer);

                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(idMembre);
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(idLivre);
                Bibliotheque.gestionBibliotheque.getMembreService().retourner(membreDTO,
                    livreDTO);

            } else if("inscrire".startsWith(command)) {
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(readInt(tokenizer));
                membreDTO.setNom(readString(tokenizer));
                membreDTO.setTelephone(readLong(tokenizer));
                membreDTO.setLimitePret(readInt(tokenizer));

                Bibliotheque.gestionBibliotheque.getMembreService().inscrire(membreDTO);
            } else if("desinscrire".startsWith(command)) {
                Bibliotheque.gestionBibliotheque.getMembreService().desinscrire(Bibliotheque.gestionBibliotheque.getMembreService().read(readInt(tokenizer)));
            } else if("reserver".startsWith(command)) {
                final int idReservation = readInt(tokenizer);
                final int idMembre = readInt(tokenizer);
                final int idLivre = readInt(tokenizer);

                final ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(idReservation);
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(idMembre);
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(idLivre);

                Bibliotheque.gestionBibliotheque.getReservationService().reserver(reservationDTO,
                    livreDTO,
                    membreDTO);
            } else if("prendreRes".startsWith(command)) {
                gestionBibliotheque.getGestionReservation().prendreRes(readInt(tokenizer) /* idReservation */,
                    readDate(tokenizer) /* dateReservation */);
            } else if("annulerRes".startsWith(command)) {
                gestionBibliotheque.getGestionReservation().annulerRes(readInt(tokenizer) /* idReservation */);
            } else if("listerLivres".startsWith(command)) {
                gestionBibliotheque.getGestionInterrogation().listerLivres();
            } else if("listerLivresTitre".startsWith(command)) {
                gestionBibliotheque.getGestionInterrogation().listerLivresTitre(readString(tokenizer) /* mot */);
            } else if(!"--".startsWith(command)) {
                System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            }

        } catch(BibliothequeException e) {
            System.out.println("** "
                + e.toString());
        }
    }

    /**
     * private static void afficherAide().
     */
    static void afficherAide() {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("separes par des espaces. La liste peut etre vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.");
        System.out.println("");
        System.out.println("Les transactions sont:");
        System.out.println("  aide");
        System.out.println("  exit");
        System.out.println("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        System.out.println("  preter <idLivre> <idMembre> <dateEmprunt>");
        System.out.println("  renouveler <idLivre> <dateRenouvellement>");
        System.out.println("  retourner <idLivre> <dateRetour>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  reserver <idReservation> <idLivre> <idMembre> <dateReservation>");
        System.out.println("  prendreRes <idReservation> <dateEmprunt>");
        System.out.println("  annulerRes <idReservation>");
        System.out.println("  listerLivresRetard <dateCourante>");
        System.out.println("  listerLivresTitre <mot>");
        System.out.println("  listerLivres");
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     * @param transaction - La transaction à traiter.
     * @return true Si la fin du fichier est atteinte, false sinon.
     */
    static boolean finTransaction(String transaction) {
        boolean fichierFin = transaction == null;

        if(!fichierFin) {
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");

            if(!tokenizer.hasMoreTokens()) {
                fichierFin = false;
            } else if("exit".equals(tokenizer.nextToken())) {
                fichierFin = true;
            }
        }
        return fichierFin;
    }

    /**
     *Lit une chaîne de caractères de la transaction.
     *@param tokenizer - La transaction à décoder.
     *@return La chaîne de caractères lue.
     *@throws BibliothequeException - Si l'élément lu est manquant
     */

    static String readString(StringTokenizer tokenizer) throws BibliothequeException {

        if(!tokenizer.hasMoreElements()) {
            throw new BibliothequeException("autre paramètre attendu");
        }

        return tokenizer.nextToken();
    }

    /**
      *Lit un integer de la transaction.
      *@param tokenizer - La transaction à décoder.
      *@return Le integer lu.
      *@throws BibliothequeException - Si l'élément lu est manquant ou n'est pas un integer.
      */
    static int readInt(StringTokenizer tokenizer) throws BibliothequeException {

        if(!tokenizer.hasMoreElements()) {
            throw new BibliothequeException("autre paramètre attendu");
        }
        // 0 par default
        final int value;

        final String token = tokenizer.nextToken();
        try {
            value = Integer.valueOf(token).intValue();
        } catch(NumberFormatException e) {
            throw new BibliothequeException("Nombre attendu a la place de \""
                + token
                + "\"");
        }

        return value;
    }

    /**
      *Lit un long de la transaction.Lit un long de la transaction.
      *
      *@param tokenizer - La transaction à décoder.
      *@return Le long lu.
      *@throws BibliothequeException - Si l'élément lu est manquant ou n'est pas un long.
      */
    static long readLong(StringTokenizer tokenizer) throws BibliothequeException {

        if(!tokenizer.hasMoreElements()) {
            throw new BibliothequeException("autre paramètre attendu");
        }

        final long value;

        final String token = tokenizer.nextToken();
        try {
            value = Long.valueOf(token).longValue();
        } catch(NumberFormatException e) {
            throw new BibliothequeException("Nombre attendu a la place de \""
                + token
                + "\"");
        }

        return value;
    }

    /**
      * Lit une date au format YYYY-MM-DD de la transaction.
      *@param tokenizer - La transaction à décoder.
      *@return La date lue.
      *@throws BibliothequeException - Si l'élément lu est manquant ou n'est pas une date correctement formatée.
      */
    static String readDate(StringTokenizer tokenizer) throws BibliothequeException {

        if(!tokenizer.hasMoreElements()) {
            throw new BibliothequeException("autre paramètre attendu");
        }

        final String token = tokenizer.nextToken();
        try {
            FormatDate.convertirDate(token);
        } catch(ParseException e) {
            throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                + token
                + "\"");
        }

        return token;
    }

}
