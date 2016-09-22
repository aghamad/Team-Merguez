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
 * Interface du syst�me de gestion d'une biblioth�que +.
 *
 * Ce programme permet d'appeler les transactions de base d'une
 * biblioth�que.  Il g�re des livres, des membres et des
 * r�servations. Les donn�es sont conserv�es dans une base de
 * donn�es relationnelles acc�d�e avec JDBC. Pour une liste des
 * transactions trait�es, voir la m�thode afficherAide().
 *
 * Param�tres
 * 0- site du serveur SQL ("local", "distant" ou "postgres")
 * 1- nom de la BD
 * 2- user id pour �tablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non sp�cifi�, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * @author Team-Merguez
 */
public final class Biblio {
    private static GestionBibliotheque gestionBiblio;

    private static boolean lectureAuClavier;

    /** Constructeur de la classe Biblio. **/
    private Biblio() {
        super();
    }

    /**
     * Ouverture de la BD,
     * traitement des transactions et
     * fermeture de la BD.
     * @throws Exception exeption
     * @param argv parametre
     */
    public static void main(String[] argv) throws Exception {
        // validation du nombre de param�tres
        if(argv.length < 4) {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password> [<fichier-transactions>]");
            System.out.println(Connexion.serveursSupportes());
            return;
        }

        try {
            // ouverture du fichier de transactions
            // s'il est sp�cifi� comme argument
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

            //  try-with-resources Statement
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
      * Traitement des transactions de la biblioth�que.
      *@param reader lire
      *@throws Exception exeption
      */
    static void traiterTransactions(BufferedReader reader) throws Exception {
        afficherAide();
        String transaction = lireTransaction(reader);
        while(!finTransaction(transaction)) {
            /* d�coupage de la transaction en mots*/
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                executerTransaction(tokenizer);
            }
            transaction = lireTransaction(reader);
        }
    }

    /**
      * Lecture d'une transaction.
      * @param reader lire
    *@throws IOException Exeption
    *@return transaction
      */
    static String lireTransaction(BufferedReader reader) throws IOException {
        System.out.print("> ");
        final String transaction = reader.readLine();
        /* echo si lecture dans un fichier */
        if(!lectureAuClavier
            && transaction != null) {
            System.out.println(transaction);
        }
        return transaction;
    }

    /**
      * D�codage et traitement d'une transaction.
      *
      *@throws Exception exeption
      *@param tokenizer reader
      */
    static void executerTransaction(StringTokenizer tokenizer) throws Exception {
        try {
            final String command = tokenizer.nextToken();

            /* ******************* */
            /*         HELP        */
            /* ******************* */
            if("aide".startsWith(command)) {
                afficherAide();
            } else if("acquerir".startsWith(command)) {
                gestionBiblio.getGestionLivre().acquerir(readInt(tokenizer) /* idLivre */,
                    readString(tokenizer) /* titre */,
                    readString(tokenizer) /* auteur */,
                    readDate(tokenizer) /* dateAcquisition */);
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

    /** Affiche le menu des transactions accept�es par le syst�me .*/
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
     * Verifie si la fin du traitement des transactions est en atteinte.
     *
     * @param transaction transaction
     * @return fichierFin Boolean
     */
    static boolean finTransaction(String transaction) {
        /* fin de fichier atteinte */
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

    /** lecture d'une cha�ne de caract�res de la transaction entr�e � l'�cran .
     *
     * @param tokenizer token
     *  @throws BibliothequeException exeption
     *  @return tokenizer token
     *
     * */

    static String readString(StringTokenizer tokenizer) throws BibliothequeException {

        if(!tokenizer.hasMoreElements()) {
            throw new BibliothequeException("autre paramètre attendu");
        }

        return tokenizer.nextToken();
    }

    /**
      * lecture d'un int java de la transaction entr�e � l'�cran.
      *
      * @param tokenizer token
     *  @throws BibliothequeException exeption
     *  @return tokenizer token
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
      * lecture d'un long java de la transaction entr�e � l'�cran.
      *
      *  @param tokenizer token
     *  @throws BibliothequeException exeption
     *  @return tokenizer token
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
      * lecture d'une date en format YYYY-MM-DD.
      *
      *  @param tokenizer token
     *  @throws BibliothequeException exeption
     *  @return tokenizer token
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
