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

    /*
     * private static final String ADD_REQUEST = "INSERT INTO reservation "
        + "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    
    private static final String READ_REQUEST = "SELECT idReservation, idMembre, idLivre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";
    
    private static final String UPDATE_REQUEST = "UPDATE reservation "
        + "SET idMembre = ?, idLivre = ?, dateReservation = ? "
        + "WHERE idReservation = ?";
    
    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "WHERE idReservation = ?";
     */

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtExisteLivre;

    private PreparedStatement stmtExisteMembre;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtDelete;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.stmtExiste = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idReservation = ?");
            this.stmtExisteLivre = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idLivre = ? "
                + "order by dateReservation");
            this.stmtExisteMembre = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idMembre = ? ");
            this.stmtInsert = connexion.getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
                + "values (?,?,?,str_to_date(?,'%Y-%m-%d'))");
            this.stmtDelete = connexion.getConnection().prepareStatement("delete from reservation where idReservation = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
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
        try {
            this.stmtExiste.setInt(1,
                idReservation);
            reservationExiste = false;
            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {
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

        ReservationDTO tupleReservation;
        try {
            this.stmtExiste.setInt(1,
                idReservation);
            tupleReservation = null;

            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {
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
        try {
            this.stmtExisteLivre.setInt(1,
                idLivre);

            tupleReservation = null;

            try(
                ResultSet rset = this.stmtExisteLivre.executeQuery()) {
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
        try {
            this.stmtExisteMembre.setInt(1,
                idMembre);

            tupleReservation = null;

            try(
                ResultSet rset = this.stmtExisteMembre.executeQuery()) {
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
        try {
            this.stmtInsert.setInt(1,
                idReservation);
            this.stmtInsert.setInt(2,
                idLivre);
            this.stmtInsert.setInt(3,
                idMembre);
            this.stmtInsert.setString(4,
                dateReservation);
            this.stmtInsert.executeUpdate();
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
        try {
            this.stmtDelete.setInt(1,
                idReservation);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
