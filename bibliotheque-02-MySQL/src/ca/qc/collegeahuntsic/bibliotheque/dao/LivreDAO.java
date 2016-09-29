// Fichier LivreDAO.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table livre.
 *
 * @author Team-Merguez
 */

public class LivreDAO extends DAO {
    private static final long serialVersionUID = 1L;

    // stmtExiste select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?"

    private static final String READ_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre "
        + "WHERE idLivre = ?";

    private static final String ADD_REQUEST = "INSERT INTO livre "
        + "VALUES(?, ?, ?, ?, NULL, NULL)";

    private static final String UPDATE_REQUEST = "UPDATE livre "
        + "SET idMembre = ?, datePret = ?,"
        + "titre = ?, auteur = ?,"
        + "dateAcquisition = ?"
        + "WHERE idLivre = ?";

    private static final String DELETE_REQUEST = "DELETE FROM livre "
        + "WHERE idlivre = ?";

    private static final String EMPRUNT_REQUEST = "UPDATE livre "
        + "SET idMembre = ?, datePret = CURRENT_TIMESTAMP, "
        + "titre = ?, auteur = ?, "
        + "dateAcquisition = ? "
        + "WHERE idLivre = ?";

    private static final String RETOUR_REQUEST = "UPDATE livre "
        + "SET idMembre = null, datePret = null, "
        + "titre = ?, auteur = ?, "
        + "dateAcquisition = ? "
        + "WHERE idLivre = ?";

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public LivreDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     *
     * Verifie si le livre existe.
     *
     * @param idLivre id du livre recherché
     * @return boolean Si le livre existe ou n'existe pas
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public boolean existe(int idLivre) throws DAOException {

        try(
            PreparedStatement statementExist = getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {

            boolean livreExiste;
            statementExist.setInt(1,
                idLivre);

            livreExiste = false;

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                livreExiste = resultSet.next();
            }
            return livreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
    *
    * Lecture d'un livre.
    *
    * @param idLivre id du livre recherché
    * @return Objet de livre LivreDTO
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public LivreDTO getLivre(int idLivre) throws DAOException {

        LivreDTO tupleLivre = null;

        try(
            PreparedStatement statementExist = getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idLivre);

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                if(resultSet.next()) {
                    tupleLivre = new LivreDTO();
                    tupleLivre.setIdLivre(idLivre);
                    tupleLivre.setTitre(resultSet.getString(2));
                    tupleLivre.setAuteur(resultSet.getString(3));
                    tupleLivre.setDateAcquisition(resultSet.getDate(4));
                    tupleLivre.setIdMembre(resultSet.getInt(5));
                    tupleLivre.setDatePret(resultSet.getDate(6));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return tupleLivre;
    }

    /**
     *
     * Ajoute un nouveau livre.
     *
     * @param idLivre id du livre à ajouter
     * @param titre titre du livre à ajouter
     * @param auteur auteur du livre à ajouter
     * @param dateAcquisition date d'acquisition du livre à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        /* Ajout du livre. */
        try(
            PreparedStatement statementInsert = getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                idLivre);
            statementInsert.setString(2,
                titre);
            statementInsert.setString(3,
                auteur);
            statementInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Emprunte un livre.
     *
     * @param idLivre id du livre à emprunter
     * @param idMembre id du membre qui veut emprunter le livre
     * @param datePret date de pret du livre
     * @return resutat de la requete update
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        /* Enregistrement du pret. */
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(LivreDAO.EMPRUNT_REQUEST)) {
            statementUpdate.setInt(1,
                idMembre);
            statementUpdate.setDate(2,
                Date.valueOf(datePret));
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     *
     * Rendre le livre disponible (non-prété).
     *
     * @param idLivre id du livre à rendre
     * @return resultat de la requete update
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public int retourner(int idLivre) throws DAOException {
        /* Enregistrement du pret. */
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(LivreDAO.RETOUR_REQUEST)) {
            statementUpdate.setNull(1,
                Types.INTEGER);
            statementUpdate.setNull(2,
                Types.DATE);
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
    *
    * Suppression d'un livre.
    *
    * @param idLivre id du livre à vendre
    * @return resultat de la requete delete
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public int vendre(int idLivre) throws DAOException {
        /* Suppression du livre. */
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idLivre);
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
     * Methode CRUD Add pour ajouter un nouveau livre.
     *
     * @param livreDTO Une instance du livre a ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(LivreDTO livreDTO) throws DAOException {
        /* Ajout du livre. */
        try(
            PreparedStatement statementInsert = getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                livreDTO.getIdLivre());
            statementInsert.setString(2,
                livreDTO.getTitre());
            statementInsert.setString(3,
                livreDTO.getAuteur());
            statementInsert.setDate(4,
                livreDTO.getDateAcquisition());
            statementInsert.setDate(4,
                livreDTO.getDateAcquisition());
            statementInsert.setInt(5,
                livreDTO.getIdMembre());
            statementInsert.setDate(6,
                livreDTO.getDatePret());

            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Method CRUD Update pour modifier un livre.
     *
     * @param livreDTO Une instance du livre a modifier
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(LivreDTO livreDTO) throws DAOException {
        /* Update d'un livre. */
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {

            statementUpdate.setInt(1,
                livreDTO.getIdMembre());
            statementUpdate.setDate(2,
                livreDTO.getDatePret());
            statementUpdate.setString(3,
                livreDTO.getTitre());
            statementUpdate.setString(4,
                livreDTO.getAuteur());
            statementUpdate.setDate(5,
                livreDTO.getDateAcquisition());
            statementUpdate.setInt(6,
                livreDTO.getIdLivre());

            statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
    *
    * Methode CRUD Read pour lire un livre.
    *
    * @param idLivre id du livre recherché
    * @return Objet de livre LivreDTO
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public LivreDTO read(int idLivre) throws DAOException {
        // Cette methode est exactement comme la methode getLivre()
        LivreDTO livreDTO = null;

        try(
            PreparedStatement statementExist = getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idLivre);

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                if(resultSet.next()) {
                    livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(idLivre);
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getDate(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getDate(6));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return livreDTO;
    }

    /**
    *
    * Method CRUD pour supprimer un livre.
    *
    * @param livreDTO nom donne en paramettre pour instancier la classe LivreDTO.
    * @throws DAOException S'il y a une erreur avec la base de données.
    */
    public void delete(LivreDTO livreDTO) throws DAOException {
        // Cette methode est exactement comme la methode vendre()
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                livreDTO.getIdLivre());
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
