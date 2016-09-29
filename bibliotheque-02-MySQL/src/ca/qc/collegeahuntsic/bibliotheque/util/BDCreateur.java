// Fichier CreerBD.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BDCreateurException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 *Permet de créer la BD utilisé par Bibliotheque.java.
 *Paramètres:0- serveur SQL
 *           1- bd nom de la BD
 *           2- user id pour établir une connexion avec le serveur SQL
 *           3- mot de passe pour le user id
 */

/**
 * Utilitaire de création de la base de données.
 *
 * @author Team-Merguez
 */
final class BDCreateur {

    /**
     *
     * Constructeur privé pour empêcher toute instanciation.
     *
     */
    private BDCreateur() {
        super();
    }

    /**
     *
     * Crée la base de données nécessaire à l'application bibliothèque.
     *
     * Paramètres :
     * 0 - Type de serveur SQL de la BD
     * 1 - Nom du schéma de la base de données
     * 2 - Nom d'utilisateur sur le serveur SQL
     * 3 - Mot de passe sur le serveur SQL
     *
     * @param args Les arguments du main
     * @throws BDCreateurException S'il y a une erreur avec la connexion ou s'il y a une erreur avec la base de données
     */
    public static void main(String[] args) throws BDCreateurException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        try(
            Connexion connexion = new Connexion(args[0],
                args[1],
                args[2],
                args[3])) {

            final Statement statement = connexion.getConnection().createStatement();

            statement.executeUpdate("DROP TABLE IF EXISTS reservation CASCADE");

            statement.executeUpdate("DROP TABLE IF EXISTS livre CASCADE");

            statement.executeUpdate("DROP TABLE IF EXISTS membre CASCADE");

            statement.executeUpdate("CREATE TABLE membre "
                + "( "
                + "idMembre INTEGER(3) CHECK(idMembre > 0), "
                + "nom VARCHAR(10) NOT NULL,"
                + "telephone BIGINT(10) , "
                + "limitePret INTEGER(2) CHECK(limitePret > 0 AND limitePret <= 10) , "
                + "nbpret INTEGER(2) DEFAULT 0 CHECK(nbpret >= 0) , "
                + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                + "CONSTRAINT limiteNbPret CHECK(nbpret <= limitePret) "
                + ")");

            statement.executeUpdate("CREATE TABLE livre "
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

            statement.executeUpdate("CREATE TABLE reservation "
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

            statement.close();
        } catch(ConnexionException connexionException) {
            throw new BDCreateurException(connexionException);
        } catch(Exception exception) {
            throw new BDCreateurException(exception);
        }

    }

}
