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

    private static final String FIND_BY_LIVRE = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ? "
        + "ORDER BY dateReservation ASC";

    private static final String FIND_BY_MEMBRE = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ?";

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
            PreparedStatement StatementExist = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
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
            PreparedStatement StatementExist = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
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
            PreparedStatement StatementLivreExist = this.getConnexion().getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE)) {
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
            PreparedStatement statementMembreExist = this.getConnexion().getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE)) {
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
            PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
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
            PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idReservation);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
