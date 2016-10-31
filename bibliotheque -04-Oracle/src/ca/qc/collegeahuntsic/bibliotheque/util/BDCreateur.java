// Fichier BDCreateur.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.db.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.util.BDCreateurException;

/**
 * Utilitaire de création de la base de données.
 *
 * @author Team-Merguez
 */
public final class BDCreateur {
    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private BDCreateur() {
        super();
    }

    /**
     * Crée la base de données nécessaire à l'application bibliothèque.<br /><br />
     *
     * Paramètres :<br />
     *              0 - Type de serveur SQL de la BD<br />
     *              1 - Nom du schéma de la base de données<br />
     *              2 - Nom d'utilisateur sur le serveur SQL<br />
     *              3 - Mot de passe sur le serveur SQL
     *
     * @param arguments Les arguments du main
     * @throws BDCreateurException S'il y a une erreur avec la connexion ou s'il y a une erreur avec la base de données
     */
    public static void main(String[] arguments) throws BDCreateurException {
        if(arguments.length < 4) {
            System.out.println("Usage : java BDCreateur <type_serveur> <nom_schema> <nom_utilisateur> <mot_passe>");
        } else {
            try(
                Connexion connexion = new Connexion(arguments[0],
                    arguments[1],
                    arguments[2],
                    arguments[3])) {

                try(
                    Statement statement = connexion.getConnection().createStatement()) {

                    statement.executeUpdate("DROP TABLE reservation CASCADE CONSTRAINTS PURGE");
                    statement.executeUpdate("DROP TABLE pret CASCADE CONSTRAINTS PURGE");
                    statement.executeUpdate("DROP TABLE livre CASCADE CONSTRAINTS PURGE");
                    statement.executeUpdate("DROP TABLE membre CASCADE CONSTRAINTS PURGE");

                    statement.executeUpdate("CREATE TABLE membre "
                        + "("
                        + "idMembre NUMBER(3) CHECK(idMembre > 0),"
                        + "nom VARCHAR(100) NOT NULL,telephone NUMBER(10),"
                        + "limitePret NUMBER(2) CHECK(limitePret > 0 AND limitePret <= 10),"
                        + "nbpret NUMBER(2) DEFAULT 0 CHECK(nbpret >= 0),"
                        + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                        + "CONSTRAINT limiteNbPret check(nbpret <= limitePret)"
                        + ")");

                    statement.executeUpdate("CREATE TABLE livre "
                        + "("
                        + "idLivre NUMBER(3) CHECK(idLivre > 0), "
                        + "titre VARCHAR(100) NOT NULL, "
                        + "auteur VARCHAR(100) NOT NULL, "
                        + "dateAcquisition TIMESTAMP NOT NULL, "
                        + "CONSTRAINT cleLivre PRIMARY KEY (idLivre)"
                        + ")");

                    statement.executeUpdate("CREATE TABLE reservation "
                        + "("
                        + "idReservation NUMBER(3) CHECK(idReservation > 0), "
                        + "idMembre NUMBER(3) CHECK(idMembre > 0), "
                        + "idLivre NUMBER(3) CHECK(idLivre > 0), "
                        + "dateReservation TIMESTAMP(3) NOT NULL,"
                        + "CONSTRAINT cleReservation PRIMARY KEY (idReservation), "
                        + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre), "
                        + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE, "
                        + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) ON DELETE CASCADE "
                        + ")");

                    statement.executeUpdate("CREATE TABLE pret "
                        + "("
                        + "idPret NUMBER(3) CHECK(idPret > 0),"
                        + "idMembre NUMBER(3) CHECK(idMembre > 0),"
                        + "idLivre NUMBER(3) CHECK(idLivre > 0),"
                        + "datePret TIMESTAMP(3),dateRetour TIMESTAMP(3),"
                        + "CONSTRAINT clePrimairePret PRIMARY KEY (idPret),"
                        + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre),"
                        + "CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre)"
                        + ")");

                    connexion.commit();

                }
            } catch(ConnexionException connexionException) {
                throw new BDCreateurException(connexionException);
            } catch(SQLException sqlException) {
                throw new BDCreateurException(sqlException);
            } catch(Exception exception) {
                throw new BDCreateurException(exception);
            }
        }
    }
}
