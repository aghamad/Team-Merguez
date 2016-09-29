// Fichier ReservationDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Team-Merguez
 */
public class ReservationDAO extends DAO {

    private static final String ADD_REQUEST = "INSERT INTO reservation "
        + "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String READ_REQUEST = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "WHERE idReservation = ?";

    private static final String UPDATE_REQUEST = "UPDATE Reservation SET idReservation = ?, idMembre = ?, idLivre = ?, dateReservation = ? WHERE idReservation = ?";

    private static final String FIND_BY_LIVRE = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ? "
        + "ORDER BY dateReservation ASC";

    private static final String FIND_BY_MEMBRE = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ?";

    // private static final String UPDATE_REQUEST = "UPDATE reservation "
    //+ "SET idMembre = ?, idLivre = ?, dateReservation = ? "
    // + "WHERE idReservation = ?";

    private static final long serialVersionUID = 1L;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDAO(Connexion connexion) throws DAOException {
        super(connexion);

    }

    /**
     *
     * Verifie si une reservation existe.
     *
     * @param idReservation id de la reservation
     * @return boolean si la reservation existe ou pas
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public boolean existe(int idReservation) throws DAOException {

        boolean reservationExiste;
        try(
            PreparedStatement StatementExist = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            StatementExist.setInt(1,
                idReservation);
            reservationExiste = false;
            try(
                ResultSet rset = StatementExist.executeQuery()) {
                reservationExiste = rset.next();
                rset.close();
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservationExiste;
    }

    /**
     *
     * Lecture d'une reservation.
     *
     * @param idReservation id de la reservation
     * @return Objet de reservation ReservationDTO
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservation(int idReservation) throws DAOException {

        ReservationDTO tupleReservation = null;

        try(
            PreparedStatement StatementExist = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            StatementExist.setInt(1,
                idReservation);

            try(
                ResultSet rset = StatementExist.executeQuery()) {
                if(rset.next()) {
                    tupleReservation = new ReservationDTO();
                    tupleReservation.setIdReservation(rset.getInt(1));
                    tupleReservation.setIdLivre(rset.getInt(2));
                    tupleReservation.setIdMembre(rset.getInt(3));
                    tupleReservation.setDateReservation(rset.getDate(4));

                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return tupleReservation;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idLivre id du livre
     * @return Objet de reservation ReservationDTO
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservationLivre(int idLivre) throws DAOException {

        ReservationDTO tupleReservation;
        try(
            PreparedStatement StatementLivreExist = getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE)) {
            StatementLivreExist.setInt(1,
                idLivre);

            tupleReservation = null;

            try(
                ResultSet rset = StatementLivreExist.executeQuery()) {
                if(rset.next()) {
                    tupleReservation = new ReservationDTO();
                    tupleReservation.setIdReservation(rset.getInt(1));
                    tupleReservation.setIdLivre(rset.getInt(2));
                    tupleReservation.setIdMembre(rset.getInt(3));
                    tupleReservation.setDateReservation(rset.getDate(4));

                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return tupleReservation;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idMembre id du membre
     * @return Objet de reservation ReservationDTO
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservationMembre(int idMembre) throws DAOException {

        ReservationDTO tupleReservation;
        try(
            PreparedStatement statementMembreExist = getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE)) {
            statementMembreExist.setInt(1,
                idMembre);

            tupleReservation = null;

            try(
                ResultSet rset = statementMembreExist.executeQuery()) {
                if(rset.next()) {
                    tupleReservation = new ReservationDTO();
                    tupleReservation.setIdReservation(rset.getInt(1));
                    tupleReservation.setIdLivre(rset.getInt(2));
                    tupleReservation.setIdMembre(rset.getInt(3));
                    tupleReservation.setDateReservation(rset.getDate(4));
                    return tupleReservation;
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return tupleReservation;
    }

    /**
     *
     * Réservation d'un livre.
     *
     * @param idReservation id de la réservation
     * @param idLivre id du livre à reserver
     * @param idMembre id du memebre qui reserve
     * @param dateReservation date de reservation du livre
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws DAOException {
        try(
            PreparedStatement statementInsert = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                idReservation);
            statementInsert.setInt(2,
                idLivre);
            statementInsert.setInt(3,
                idMembre);
            statementInsert.setString(4,
                dateReservation);
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Suppression d'une reservation.
     *
     * @param idReservation id de la reservation à supprimer
     * @return resultat de la requete delete
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public int annulerRes(int idReservation) throws DAOException {
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idReservation);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /*
     * Methodes CRUD add/read/update/delete Mergeuz Represent
     *
     */

    /**
     *
     * Methode CRUD Add pour ajouter une nouvelle reservation.
     *
     * @param reservationDTO Une instance ReservationDTO du reservation a ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement statementAdd = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            statementAdd.setInt(1,
                reservationDTO.getIdReservation());
            statementAdd.setInt(2,
                reservationDTO.getIdLivre());
            statementAdd.setInt(3,
                reservationDTO.getIdMembre());
            statementAdd.setDate(4,
                reservationDTO.getDateReservation());
            statementAdd.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
    *
    * Method CRUD Update pour modifier les informations d'une reservation.
    *
    * @param reservationDTO Une instance ReservationDTO du reservation a ajouter
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public void update(ReservationDTO reservationDTO) throws DAOException {
        /* Update d'une reservation. */
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            statementUpdate.setInt(1,
                reservationDTO.getIdReservation());
            statementUpdate.setInt(2,
                reservationDTO.getIdMembre());
            statementUpdate.setInt(3,
                reservationDTO.getIdLivre());
            statementUpdate.setDate(4,
                reservationDTO.getDateReservation());
            statementUpdate.setInt(5,
                reservationDTO.getIdReservation());
            statementUpdate.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
    *
    * Methode CRUD Read pour lire les informations d'une reservation.
    *
    * @param idReservation id de la reservation recherchée
    * @return Une instance MembreDTO du membre a lire
    * @throws DAOException S'il y a une erreur avec la base de données
    */
    public ReservationDTO read(int idReservation) throws DAOException {
        // Cette methode est exactement comme la methode getMembre()
        ReservationDTO reservationDTO = null;

        try(
            PreparedStatement statementExist = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {

            statementExist.setInt(1,
                idReservation);

            try(
                ResultSet resultSet = statementExist.executeQuery()) {
                if(resultSet.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(idReservation);
                    reservationDTO.setIdMembre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getDate(3));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return reservationDTO;
    }

    /**
     * Method CRUD pour supprimer une reservation.
     *
     * @param reservationDTO La reservation à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws DAOException {
        // Cette methode est exactement comme la methode deinscrire
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                reservationDTO.getIdReservation());
            statementDelete.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
