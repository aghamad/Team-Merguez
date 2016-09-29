// Fichier Connexion.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 *
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La méthode serveursSupportes() indique les serveurs supportés.
 *
 * Pré-condition
 * le driver JDBC approprié doit être accessible.
 *
 * Post-condition
 * la connexion est ouverte en mode autocommit false et sérialisable,
 * (s'il est supporté par le serveur).
 */

/**
 * La classe Connexion permet de recupere une connection ainsi que de la fermer.
 *
 * @author Team-Merguez
 */
public class Connexion implements AutoCloseable {

    private Connection conn;

    /**
     * Crée une connexion en mode autocommit false.
     * @param serveur serveur SQL de la BD
     * @param bd nom de la base de données
     * @param user id sur le serveur SQL
     * @param pass Mot de passe sur le serveur SQL
     * @throws ConnexionException Si le driver n'existe pas, S'il y a une erreur avec la base de données ou si typeServeur n'est pas valide
     */
    public Connexion(String serveur,
        String bd,
        String user,
        String pass) throws ConnexionException {
        final Driver d;
        try {

            if("local".equals(serveur)) {
                d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + bd,
                    user,
                    pass);
            } else if("distant".equals(serveur)) {
                d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:oracle:thin:@collegeahuntsic.info:1521:"
                    + bd,
                    user,
                    pass);
            } else if("postgres".equals(serveur)) {
                d = (Driver) Class.forName("org.postgresql.Driver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:postgresql:"
                    + bd,
                    user,
                    pass);
            }
            //    else {
            //     // access
            //        d = (Driver) Class.forName("org.postgresql.Driver").newInstance();
            //        DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
            //        conn = DriverManager.getConnection(
            //            "jdbc:odbc:" + bd,
            //            "", "");
            //        }

            // mettre en mode de commit manuel
            this.conn.setAutoCommit(false);

            // mettre en mode sérialisable si possible
            // (plus haut niveau d'integrité l'accés concurrent aux données)
            final DatabaseMetaData dbmd = this.conn.getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode sérialisable :\n"
                    + "Estampille "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            } else {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
                    + "Heure "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            }
        } catch(
            SQLException
            | InstantiationException
            | IllegalAccessException
            | ClassNotFoundException exception) {
            throw new ConnexionException(exception);
        }
    }

    /**
     * Fermeture de la connexion.
     *
     * @throws ConnexionException Si le driver n'existe pas, S'il y a une erreur avec la base de données ou si typeServeur n'est pas valide
     */
    public void fermer() throws ConnexionException {
        try {
            this.conn.rollback();
            this.conn.close();
            System.out.println("Connexion fermée"
                + " "
                + this.conn);
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     *
     * Effectue un commit sur la Connection JDBC.
     *
     * @throws ConnexionException Si le driver n'existe pas, S'il y a une erreur avec la base de données ou si typeServeur n'est pas valide
     */
    public void commit() throws ConnexionException {
        try {
            this.conn.commit();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     *
     * Effectue un rollback sur la Connection JDBC.
     *
     * @throws ConnexionException Si le driver n'existe pas, S'il y a une erreur avec la base de données ou si typeServeur n'est pas valide
     */
    public void rollback() throws ConnexionException {
        try {
            this.conn.rollback();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     *
     * Getter de la variable d'instance this.connection.
     *
     * @return La variable d'instance this.connection
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Retourne la liste des serveurs supportés par ce gestionnaire de connexion.
     * local : MySQL installé localement
     * distant : Oracle installé au Département d'Informatique du Collège Ahuntsic
     * postgres : Postgres installé localement
     * access : Microsoft Access installé localement et inscrit dans ODBC
     *
     * @return String La liste des serveurs supportés par ce gestionnaire de connexion
     */
    public static String serveursSupportes() {
        return "local : MySQL installé localement\n"
            + "distant : Oracle installé au Département d'Informatique du Collège Ahuntsic\n"
            + "postgres : Postgres installé localement\n"
            + "access : Microsoft Access installé localement et inscrit dans ODBC";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        rollback();
        getConnection().close();
        System.out.println("\nConnexion fermée"
            + " "
            + getConnection());
    }
}
