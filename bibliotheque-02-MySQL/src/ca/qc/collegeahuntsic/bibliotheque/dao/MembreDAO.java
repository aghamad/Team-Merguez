// Fichier MembreDAO.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les acc�s � la table membre.
 * Cette classe g�re tous les acc�s � la table membre.
 * @author Team-Merguez
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
      * Creation d'une instance. Pr�compilation d'�nonc�s SQL.
      * @param cx la connexion
      * @throws DAOException l'exception du SQL
      */
    public MembreDAO(Connexion cx) throws DAOException {

        super(cx);
        this.cx = super.getConnexion();

        try {
            this.stmtExiste = cx.getConnection().prepareStatement(
                "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
            this.stmtInsert = cx.getConnection().prepareStatement(
                "insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
                    + "values (?,?,?,?,0)");
            this.stmtUpdateIncrNbPret = cx.getConnection()
                .prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
            this.stmtUpdateDecNbPret = cx.getConnection()
                .prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
            this.stmtDelete = cx.getConnection()
                .prepareStatement("delete from membre where idmembre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Retourner la connexion associ�e.
      */
    @Override
    public Connexion getConnexion() {

        return this.cx;
    }

    /**
      * Verifie si un membre existe.
      *  @return MembreExiste si le membre existe
      * @throws DAOException Exeptions
      * @param idMembre parametre id
      */
    public boolean existe(int idMembre) throws DAOException {
        boolean membreExiste;
        try {
            this.stmtExiste.setInt(1,
                idMembre);
            membreExiste = false;
            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {
                membreExiste = rset.next();
                rset.close();
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membreExiste;
    }

    /**
      * Lecture d'un membre.
      *
      * @return tupleMembre si le livre existe
      * @throws DAOException Exeptions
      * @param idMembre parametre id
      */
    public MembreDTO getMembre(int idMembre) throws DAOException {
        MembreDTO tupleMembre;
        try {
            this.stmtExiste.setInt(1,
                idMembre);

            tupleMembre = null;

            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {

                if(rset.next()) {
                    tupleMembre = new MembreDTO();
                    tupleMembre.setIdMembre(idMembre);
                    tupleMembre.setNom(rset.getString(2));
                    tupleMembre.setTelephone(rset.getLong(3));
                    tupleMembre.setLimitePret(rset.getInt(4));
                    tupleMembre.setNbPret(rset.getInt(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return tupleMembre;
    }

    /**
      * Ajout d'un nouveau membre.
      *
      * @param idMembre le id d'un membre
      * @param nom le nom
      * @param telephone le telephone
      * @param limitePret la limite du Pret
      *  @throws DAOException Exeptions
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {
        /* Ajout du membre. */
        try {
            this.stmtInsert.setInt(1,
                idMembre);
            this.stmtInsert.setString(2,
                nom);
            this.stmtInsert.setLong(3,
                telephone);
            this.stmtInsert.setInt(4,
                limitePret);
            this.stmtInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Incrementer le nb de pret d'un membre.
      *
      *  @param idMembre le id du membre
      *@throws DAOException Exeptions
      *@return update
      */
    public int preter(int idMembre) throws DAOException {
        try {
            this.stmtUpdateIncrNbPret.setInt(1,
                idMembre);
            return this.stmtUpdateIncrNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Decrementer le nb de pret d'un membre.
      *
      * @param idMembre le id du membre
      *@throws DAOException Exeptions
      *@return update
      */
    public int retourner(int idMembre) throws DAOException {
        try {
            this.stmtUpdateDecNbPret.setInt(1,
                idMembre);
            return this.stmtUpdateDecNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Suppression d'un membre.
      *
      * @param idMembre le id du membre
      *@throws DAOException Exeptions
      *@return update
      */
    public int desinscrire(int idMembre) throws DAOException {
        try {
            this.stmtDelete.setInt(1,
                idMembre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
