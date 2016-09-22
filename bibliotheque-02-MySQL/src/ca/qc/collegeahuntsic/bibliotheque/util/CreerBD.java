// Fichier CreerBD.java
// Auteur : Sasha Benjamin
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *<pre>
 *
 *Permet de cr�er la BD utilis�e par Biblio.java.
 *
 *Param�tres:0- serveur SQL
 *           1- bd nom de la BD
 *           2- user id pour �tablir une connexion avec le serveur SQL
 *           3- mot de passe pour le user id
 *</pre>
 */

/**
 * TODO Auto-generated field javadoc.
 *
 * @author Sasha Benjamin
 */

final class CreerBD {
    /**
     * TODO Auto-generated field javadoc.
     *
     * Constructeur de classe de type private
     *
     */

    private CreerBD() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * TODO Auto-generated field javadoc.
     *
     * Main
     * @throws DAOException est le nom de l'exception qui est lancer.
     * @throws Exception est le nom de l'exception qui est lancer.
     * @param args tableau de string.
     */
    public static void main(String[] args) throws DAOException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        try {
            final Connexion cx = new Connexion(args[0],
                args[1],
                args[2],
                args[3]);

            try(
                Statement stmt = cx.getConnection().createStatement()) {

                stmt.executeUpdate("DROP TABLE IF EXISTS reservation CASCADE");

                stmt.executeUpdate("DROP TABLE IF EXISTS livre CASCADE");

                stmt.executeUpdate("DROP TABLE IF EXISTS membre CASCADE");

                stmt.executeUpdate("CREATE TABLE membre "
                    + "( "
                    + "idMembre INTEGER(3) CHECK(idMembre > 0), "
                    + "nom VARCHAR(10) NOT NULL,"
                    + "telephone BIGINT(10) , "
                    + "limitePret INTEGER(2) CHECK(limitePret > 0 AND limitePret <= 10) , "
                    + "nbpret INTEGER(2) DEFAULT 0 CHECK(nbpret >= 0) , "
                    + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                    + "CONSTRAINT limiteNbPret CHECK(nbpret <= limitePret) "
                    + ")");

                stmt.executeUpdate("CREATE TABLE livre "
                    + "( "
                    + "idLivre INTEGER(3) CHECK(idLivre > 0) , "
                    + "titre VARCHAR(10) NOT NULL, "
                    + "auteur VARCHAR(10) NOT NULL,"
                    + "dateAcquisition DATE NOT NULL,"
                    + "idMembre INTEGER(3) , "
                    + "datePret DATE , "
                    + "CONSTRAINT cleLivre PRIMARY KEY (idLivre), "
                    + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) "
                    + ")");

                stmt.executeUpdate("CREATE TABLE reservation "
                    + "( "
                    + "idReservation INTEGER(3) , "
                    + "idMembre INTEGER(3) , "
                    + "idLivre INTEGER(3) , "
                    + "dateReservation DATE , "
                    + "CONSTRAINT cleReservation PRIMARY KEY (idReservation) , "
                    + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) ,"
                    + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE , "
                    + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) ON DELETE CASCADE "
                    + ")");

                stmt.close();
            }

            cx.fermer();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
