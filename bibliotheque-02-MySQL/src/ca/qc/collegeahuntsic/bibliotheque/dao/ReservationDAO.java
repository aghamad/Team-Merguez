// Fichier ReservationDAO.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les acc�s � la table reservation.
 *
 *
 * Cette classe g�re tous les acc�s � la table reservation.
 *
 * @author Team-Merguez
 */

public class ReservationDAO extends DAO {

    /**
     * TODO Auto-generated field javadoc.
     */
    private static final long serialVersionUID = 1L;

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

    private Connexion cx;

    /**
      * Creation d'une instance.
      *
      * @param cx connexion
      *
      *@throws DAOException Exeption
      */
    public ReservationDAO(Connexion cx) throws DAOException {
        super(cx);
        this.cx = super.getConnexion();
        try {
            this.stmtExiste = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idReservation = ?");
            this.stmtExisteLivre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idLivre = ? "
                + "order by dateReservation");
            this.stmtExisteMembre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idMembre = ? ");
            this.stmtInsert = cx.getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
                + "values (?,?,?,str_to_date(?,'%Y-%m-%d'))");
            this.stmtDelete = cx.getConnection().prepareStatement("delete from reservation where idReservation = ?");
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
      * Verifie si une reservation existe.
      *
      * @return ReservationExiste si le Reservation existe
      * @throws DAOException Exeptions
      * @param idReservation parametre id
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
      * Lecture d'une reservation.
      *
      * @return tupleReservation si le reservation existe
      * @throws DAOException Exeptions
      * @param idReservation parametre id
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
      * Lecture de la premi�re reservation d'un livre.
      *
      * @return tupleReservation si le livre a reserver existe
      * @throws DAOException Exeptions
      * @param idLivre parametre id
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
      * Lecture de la premi�re reservation d'un livre.
      *
      * @return tupleReservation si le membre qui veut reserver existe
      * @throws DAOException Exeptions
      * @param idMembre parametre id
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
      * R�servation d'un livre.
      *
      *  @param idReservation le id d'une reservation
      * @param idLivre le id d'un livre
      * @param idMembre le id d'un membre
      * @param dateReservation la date de la reservation
      *@throws DAOException Exeptions
      *
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
      * Suppression d'une reservation.
      *
      * @param idReservation la id de la reservation
      *@throws DAOException Exeptions
      *@return delete
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
