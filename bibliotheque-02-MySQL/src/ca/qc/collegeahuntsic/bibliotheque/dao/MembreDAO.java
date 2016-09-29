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

    private static final String READ_REQUEST = "SELECT * FROM membre "
        + "WHERE idMembre = ?";

    private static final String ADD_REQUEST = "INSERT INTO membre "
        + "VALUES (?,?,?,?,?)";

    private static final String UPDATE_REQUEST = "UPDATE membre SET idMembre = ?, nom = ?, telephone = ?, limitePret = ?, nbPret = ? WHERE idMembre = ?";

    private static final String EMPRUNT_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = nbPret + 1 "
        + "WHERE idMembre = ?";

    private static final String RETOUR_REQUEST = "UPDATE membre "
        + "SET nom = ?, telephone = ?, limitePret = ?, nbPret = nbPret - 1 "
        + "WHERE idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE FROM membre "
        + "WHERE idMembre = ?";

    private static final long serialVersionUID = 1L;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion  La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public MembreDAO(Connexion connexion) throws DAOException {
        super(connexion);
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

        try(
            PreparedStatement statementExist = this.getConnexion().getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

            boolean membreExiste;
            statementExist.setInt(1,
                idMembre);
            membreExiste = false;
            try(
                ResultSet rset = statementExist.executeQuery()) {
                membreExiste = rset.next();
                rset.close();
            }
            return membreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

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
        MembreDTO tupleMembre = null;
        try(
            PreparedStatement statementExist = this.getConnexion().getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idMembre);

            try(
                ResultSet rset = statementExist.executeQuery()) {

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
        try(
            PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                idMembre);
            statementInsert.setString(2,
                nom);
            statementInsert.setLong(3,
                telephone);
            statementInsert.setInt(4,
                limitePret);
            statementInsert.executeUpdate();
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
        try(
            PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(MembreDAO.EMPRUNT_REQUEST)) {
            statementUpdate.setInt(1,
                idMembre);
            return statementUpdate.executeUpdate();
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
        try(
            PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(MembreDAO.RETOUR_REQUEST)) {
            statementUpdate.setInt(1,
                idMembre);
            return statementUpdate.executeUpdate();
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
        try(
            PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idMembre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /*
     * Methodes CRUD add/read/update/delete
     *
     */

    /**
     *
     * Methode CRUD Add pour ajouter un nouveau membre.
     *
     * @param membreDTO Une instance MembreDTO du membre a ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws DAOException {
        /* Ajout d'un membre. */
        try(
            PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                membreDTO.getIdMembre());
            statementInsert.setString(2,
                membreDTO.getNom());
            statementInsert.setLong(3,
                membreDTO.getTelephone());
            statementInsert.setInt(4,
                membreDTO.getLimitePret());
            statementInsert.setInt(5,
                membreDTO.getNbPret());
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
    *
    * Method CRUD Update pour modifier les informations d'un membre.
    *
    * @param membreDTO Une instance MembreDTO du membre a modifier
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public void update(MembreDTO membreDTO) throws DAOException {
        /* Update d'un livre. */
        try(
            PreparedStatement preparedStatement = getConnexion().getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.setString(2,
                membreDTO.getNom());
            preparedStatement.setLong(3,
                membreDTO.getTelephone());
            preparedStatement.setInt(4,
                membreDTO.getLimitePret());
            preparedStatement.setInt(5,
                membreDTO.getNbPret());
            preparedStatement.setInt(6,
                membreDTO.getIdMembre());

            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
    *
    * Methode CRUD Read pour lire les informations d'un membre.
    *
    * @param idMembre id du membre recherché
    * @return Une instance MembreDTO du membre a lire
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public MembreDTO read(int idMembre) throws DAOException {
        // Cette methode est exactement comme la methode getMembre()
        MembreDTO membreDTO = null;

        try(
            PreparedStatement statementExist = this.getConnexion().getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idMembre);

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(idMembre);
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return membreDTO;
    }

    /**
     * Method CRUD pour supprimer un membre.
     *
     * @param membreDTO Le membre à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws DAOException {
        // Cette methode est exactement comme la methode deinscrire
        try(
            PreparedStatement preparedStatement = getConnexion().getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
