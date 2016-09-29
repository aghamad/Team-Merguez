// Fichier Biblio.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatDate;
import ca.qc.collegeahuntsic.bibliotheque.util.GestionBibliotheque;

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
    private static GestionBibliotheque gestionBiblio;

    private static boolean lectureAuClavier;

    /**
     *Constructeur privé pour empêcher toute instanciation.
     *
     */

    private Bibliotheque() {
        super();
    }

    /**
     *Crée une connexion sur la base de données, traite toutes les transactions et détruit la connexion.
     *@param argv Les arguments du main.
     *@throws Exception - Si une erreur survient.
     */
    public static void main(String[] argv) throws Exception {

        if(argv.length < 4) {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password> [<fichier-transactions>]");
            System.out.println(Connexion.serveursSupportes());
            return;
        }

        try {
            /**
             *  ouverture du fichier de transactions
             * s'il est spécifié comme argument
             */
            lectureAuClavier = true;
            InputStream sourceTransaction = System.in;
            if(argv.length > 4) {
                sourceTransaction = new FileInputStream(argv[4]);
                lectureAuClavier = false;
            }

            gestionBiblio = new GestionBibliotheque(argv[0],
                argv[1],
                argv[2],
                argv[3]);
            /*
             * try-with-resources Statement
             */

            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {
                traiterTransactions(reader);
            }

        } catch(Exception e) {
            e.printStackTrace(System.out);
        } finally {
            gestionBiblio.fermer();
        }
    }

    /**
      *Traite le fichier de transactions.
      *@param reader - Le flux d'entrée à lire.
      *@throws Exception - Si une erreur survient.
      */
    static void traiterTransactions(BufferedReader reader) throws Exception {
        afficherAide();
        String transaction = lireTransaction(reader);
        while(!finTransaction(transaction)) {
            /*
             * découpage de la transaction en mots.
             */

            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                executerTransaction(tokenizer);
            }
            transaction = lireTransaction(reader);
        }
    }

    /**
      *Lit une transaction.
      *@param reader - Le flux d'entrée à lire
      *@return La transaction lue.
      *@throws IOException - Si une erreur de lecture survient.
      */
    static String lireTransaction(BufferedReader reader) throws IOException {
        System.out.print("> ");
        final String transaction = reader.readLine();
        /*
         * echo si lecture dans un fichier.
         */
        if(!lectureAuClavier
            && transaction != null) {
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
    static void executerTransaction(StringTokenizer tokenizer) throws Exception {
        try {
            final String command = tokenizer.nextToken();

            if("aide".startsWith(command)) {
                afficherAide();
            } else if("acquerir".startsWith(command)) {
                gestionBiblio.getGestionLivre().acquerir(readInt(tokenizer),
                    readString(tokenizer),
                    readString(tokenizer),
                    readDate(tokenizer));
            } else if("vendre".startsWith(command)) {
                gestionBiblio.getGestionLivre().vendre(readInt(tokenizer) /* idLivre */);
            } else if("preter".startsWith(command)) {
                gestionBiblio.getGestionPret().preter(readInt(tokenizer) /* idLivre */,
                    readInt(tokenizer) /* idMembre */,
                    readDate(tokenizer) /* dateEmprunt */);
            } else if("renouveler".startsWith(command)) {
                gestionBiblio.getGestionPret().renouveler(readInt(tokenizer) /* idLivre */,
                    readDate(tokenizer) /* dateRenouvellement */);
            } else if("retourner".startsWith(command)) {
                gestionBiblio.getGestionPret().retourner(readInt(tokenizer) /* idLivre */,
                    readDate(tokenizer) /* dateRetour */);
            } else if("inscrire".startsWith(command)) {
                gestionBiblio.getGestionMembre().inscrire(readInt(tokenizer) /* idMembre */,
                    readString(tokenizer) /* nom */,
                    readLong(tokenizer) /* tel */,
                    readInt(tokenizer) /* limitePret */);
            } else if("desinscrire".startsWith(command)) {
                gestionBiblio.getGestionMembre().desinscrire(readInt(tokenizer) /* idMembre */);
            } else if("reserver".startsWith(command)) {
                gestionBiblio.getGestionReservation().reserver(readInt(tokenizer) /* idReservation */,
                    readInt(tokenizer) /* idLivre */,
                    readInt(tokenizer) /* idMembre */,
                    readDate(tokenizer) /* dateReservation */);
            } else if("prendreRes".startsWith(command)) {
                gestionBiblio.getGestionReservation().prendreRes(readInt(tokenizer) /* idReservation */,
                    readDate(tokenizer) /* dateReservation */);
            } else if("annulerRes".startsWith(command)) {
                gestionBiblio.getGestionReservation().annulerRes(readInt(tokenizer) /* idReservation */);
            } else if("listerLivres".startsWith(command)) {
                gestionBiblio.getGestionInterrogation().listerLivres();
            } else if("listerLivresTitre".startsWith(command)) {
                gestionBiblio.getGestionInterrogation().listerLivresTitre(readString(tokenizer) /* mot */);
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
            throw new BibliothequeException("Date en format YYYY-MM-DD attendue � la place  de \""
                + token
                + "\"");
        }

        return token;
    }

}
