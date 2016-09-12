import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

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
class CreerBD {
    public static void main(String args[]) throws Exception,
        SQLException,
        IOException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        Connexion cx = new Connexion(args[0],
            args[1],
            args[2],
            args[3]);

        Statement stmt = cx.getConnection().createStatement();

        stmt.executeUpdate("DROP TABLE membre CASCADE CONSTRAINTS PURGE");

        stmt.executeUpdate("DROP TABLE livre CASCADE CONSTRAINTS PURGE");

        stmt.executeUpdate("DROP TABLE reservation CASCADE CONSTRAINTS PURGE");

        stmt.executeUpdate("CREATE TABLE membre "
            + "( "
            + "idMembre        NUMBER(3) CHECK(idMembre > 0), "
            + "nom             varchar(10) NOT NULL, "
            + "telephone       NUMBER(10) , "
            + "limitePret      NUMBER(2) CHECK(limitePret > 0 AND limitePret <= 10) , "
            + "nbpret          NUMBER(2) DEFAULT 0 CHECK(nbpret >= 0) , "
            + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
            + "CONSTRAINT limiteNbPret CHECK(nbpret <= limitePret) "
            + ")");

        stmt.executeUpdate("CREATE TABLE livre "
            + "( "
            + "idLivre         NUMBER(3) CHECK(idLivre > 0) , "
            + "titre           varchar(10) NOT NULL, "
            + "auteur          varchar(10) NOT NULL, "
            + "dateAcquisition DATE NOT NULL, "
            + "idMembre        NUMBER(3) , "
            + "datePret        DATE , "
            + "CONSTRAINT cleLivre PRIMARY KEY (idLivre), "
            + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)"
            + ")");

        stmt.executeUpdate("CREATE TABLE reservation "
            + "( "
            + "idReservation   NUMBER(3) , "
            + "idMembre        NUMBER(3) , "
            + "idLivre         NUMBER(3) , "
            + "dateReservation DATE, "
            + "CONSTRAINT cleReservation PRIMARY KEY (idReservation) , "
            + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , "
            + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)"
            + "  ON DELETE CASCADE , "
            + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) "
            + "  ON DELETE CASCADE "
            + ")");

        stmt.close();
        cx.fermer();
    }
}
