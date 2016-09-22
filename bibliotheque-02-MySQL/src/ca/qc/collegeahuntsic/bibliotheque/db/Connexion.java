// Fichier Connexion.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 *
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La m�thode serveursSupportes() indique les serveurs support�s.
 *
 * Pr�-condition
 *   le driver JDBC appropri� doit �tre accessible.
 *
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et s�rialisable,
 *   (s'il est support� par le serveur).
 * </pre>
 */
/**
 * La classe Connexion permet de recupere une connection ainsi que de la fermer.
 * @author Team-Merguez
 * */
public class Connexion implements AutoCloseable {

    private Connection conn;

    /**
     * Ouverture d'une connexion en mode autocommit false et s�rialisable (si support�).
     * @param serveur serveur SQL de la BD
     * @param bd nom de la base de donn�es
     * @param user id sur le serveur SQL
     * @param pass mot de passe sur le serveur SQL
     * @throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
     */
    public Connexion(String serveur,
        String bd,
        String user,
        String pass) throws SQLException {
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

            // mettre en mode s�rialisable si possible
            // (plus haut niveau d'integrit� l'acc�s concurrent aux donn�es)
            final DatabaseMetaData dbmd = this.conn.getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode s�rialisable :\n"
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
        } catch(SQLException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace(System.out);
            throw new SQLException("JDBC Driver non instanci�");
        }
    }

    /**
     *fermeture d'une connexion.
     *@throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
     */
    public void fermer() throws SQLException {
        this.conn.rollback();
        this.conn.close();
        System.out.println("Connexion ferm�e"
            + " "
            + this.conn);
    }

    /**
     *commit.
     *@throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
     */
    public void commit() throws SQLException {
        this.conn.commit();
    }

    /**
     *rollback.
     *@throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
     */
    public void rollback() throws SQLException {
        this.conn.rollback();
    }

    /**
     *retourne la Connection jdbc.
     *@return Connection
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
      * Retourne la liste des serveurs support�s par ce gestionnaire de connexions.
      * @return String
      */
    public static String serveursSupportes() {
        return "local : MySQL install� localement\n"
            + "distant : Oracle install� au D�partement d'Informatique du Coll�ge Ahuntsic\n"
            + "postgres : Postgres install� localement\n"
            + "access : Microsoft Access install� localement et inscrit dans ODBC";
    }

    /**
     * {@inheritDoc}}
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
