// Fichier MembreDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Team-Merguez
 */

public class MembreDAO extends DAO {

    /*
     * private static final String READ_REQUEST = "SELECT * FROM membre "
        + "WHERE idMembre = ?";
    
    private static final String ADD_REQUEST = "INSERT INTO membre "
        + "VALUES (?,?,?,?,?)";
    
    private static final String UPDATE_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = ?"
        + "WHERE idMembre = ?";
    
    private static final String EMPRUNT_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = nbPret + 1 "
        + "WHERE idMembre = ?";
    
    private static final String RETOUR_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = nbPret - 1 "
        + "WHERE idMembre = ?";
    
    private static final String DELETE_REQUEST = "DELETE FROM membre "
        + "WHERE idMembre = ?";
    
    
     * */

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion  La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public MembreDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.stmtExiste = connexion.getConnection().prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
            this.stmtInsert = connexion.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
                + "values (?,?,?,?,0)");
            this.stmtUpdateIncrNbPret = connexion.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
            this.stmtUpdateDecNbPret = connexion.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
            this.stmtDelete = connexion.getConnection().prepareStatement("delete from membre where idmembre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Verifie si un livre existe.
     *
     * @param idMembre id du membre
     * @return Si le membre existe ou pas
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Lecture d'un membre.
     *
     * @param idMembre id du memebre
     * @return Objet de membre MemebreDTO
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Ajout d'un nouveau membre.
     *
     * @param idMembre id du membre à inscrire
     * @param nom nom du membre à inscrire
     * @param telephone numero de telephone du membre à inscrire
     * @param limitePret limite de pret du membre à inscrire
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Incrementer le nb de pret d'un membre.
     *
     * @param idMembre id du membre
     * @return resultat de la requete update en int
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     *  Decrementer le nb de pret d'un membre.
     *
     * @param idMembre id du memebre
     * @return résultat de la requete update en int
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Suppression d'un membre.
     *
     * @param idMembre id du membre à desinscrire
     * @return résulat de requete delete
     * @throws DAOException S'il y a une erreur avec la base de données
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
