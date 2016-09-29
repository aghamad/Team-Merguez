// Fichier MembreDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private static final long serialVersionUID = 1L;

    private static final String READ_REQUEST = "SELECT * FROM membre "
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

    private static final String GET_ALL_REQUEST = "SELECT * FROM membre";

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
            PreparedStatement statementExist = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

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
            PreparedStatement statementExist = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

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
            PreparedStatement statementInsert = getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
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
            PreparedStatement statementUpdate = getConnection().prepareStatement(MembreDAO.EMPRUNT_REQUEST)) {
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
            PreparedStatement statementUpdate = getConnection().prepareStatement(MembreDAO.RETOUR_REQUEST)) {
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
            PreparedStatement statementDelete = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
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
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {

            addPreparedStatement.setInt(1,
                membreDTO.getIdMembre());
            addPreparedStatement.setString(2,
                membreDTO.getNom());
            addPreparedStatement.setLong(3,
                membreDTO.getTelephone());
            addPreparedStatement.setInt(4,
                membreDTO.getLimitePret());
            addPreparedStatement.setInt(5,
                membreDTO.getNbPret());
            addPreparedStatement.executeUpdate();

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
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
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
            PreparedStatement statementExist = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idMembre);

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
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
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Emprend d'un livre par un membre / Increment son nombre de pret.
     *
     * @param membreDTO Le livre que le membre à emprunter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void emprunter(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement emprunterPreparedStatement = getConnection().prepareStatement(MembreDAO.EMPRUNT_REQUEST)) {

            emprunterPreparedStatement.setString(1,
                membreDTO.getNom());
            emprunterPreparedStatement.setLong(2,
                membreDTO.getTelephone());
            emprunterPreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            emprunterPreparedStatement.setInt(4,
                membreDTO.getIdMembre());

            emprunterPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Retourne d'un livre par un membre / Decrement son nombre de pret.
     *
     * @param membreDTO Le membre à retourn
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void retourner(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement retournerPreparedStatement = getConnection().prepareStatement(MembreDAO.RETOUR_REQUEST)) {

            retournerPreparedStatement.setString(1,
                membreDTO.getNom());
            retournerPreparedStatement.setLong(2,
                membreDTO.getTelephone());
            retournerPreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            retournerPreparedStatement.setInt(4,
                membreDTO.getIdMembre());

            retournerPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<MembreDTO> getAll() throws DAOException {
        List<MembreDTO> listeDTO = Collections.EMPTY_LIST;
        MembreDTO membreDTO = null;

        try(
            PreparedStatement getAllMembrePreparedStatement = getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet rset = getAllMembrePreparedStatement.executeQuery();) {
                if(rset.next()) {
                    listeDTO = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(rset.getInt(1));
                        membreDTO.setNom(rset.getString(2));
                        membreDTO.setTelephone(rset.getLong(3));
                        membreDTO.setLimitePret(rset.getInt(4));
                        membreDTO.setNbPret(rset.getInt(5));
                        listeDTO.add(membreDTO);
                    } while(rset.next());
                }

            } catch(SQLException sqlException1) {
                throw new DAOException(sqlException1);
            }
        } catch(SQLException sqlException2) {
            throw new DAOException(sqlException2);
        }

        return listeDTO;
    }

}
