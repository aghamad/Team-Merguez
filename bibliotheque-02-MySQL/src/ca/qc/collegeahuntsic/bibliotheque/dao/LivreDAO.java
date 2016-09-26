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
 * DAO pour effectuer des CRUDs avec la table <code>livre</code>.
 * @author Team-Merguez
 */

public class LivreDAO extends DAO {

    /**
     * TODO Auto-generated field javadoc.
     * * @author Livre
     */
    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
      * Creation d'une instance. Des �nonc�s SQL pour chaque requ�te sont pr�compil�s.
      *
      * @param cx connexion
      *
      *@throws DAOException Exeption
      */
    public LivreDAO(Connexion cx) throws DAOException {
        super(cx);
        this.cx = super.getConnexion();
        try {
            this.stmtExiste = cx.getConnection().prepareStatement(
                "select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.stmtInsert = cx.getConnection().prepareStatement(
                "insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                    + "values (?,?,?,?,null,null)");
            this.stmtUpdate = cx.getConnection()
                .prepareStatement("update livre set idMembre = ?, datePret = ? "
                    + "where idLivre = ?");
            this.stmtDelete = cx.getConnection()
                .prepareStatement("delete from livre where idlivre = ?");
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
      * Verifie si un livre existe.
      *
      * @return livreExiste si le livre existe
      * @throws DAOException Exeptions
      * @param idLivre parametre id
      */
    public boolean existe(int idLivre) throws DAOException {

        boolean livreExiste;
        try {
            this.stmtExiste.setInt(1,
                idLivre);
            livreExiste = false;
            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {
                livreExiste = rset.next();
                rset.close();
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return livreExiste;
    }

    /**
      * Lecture d'un livre.
      *
      * @return tupleLivre si le livre existe
      * @throws DAOException Exeptions
      * @param idLivre parametre id
      */
    public LivreDTO getLivre(int idLivre) throws DAOException {
        // test
        LivreDTO tupleLivre;
        try {
            this.stmtExiste.setInt(1,
                idLivre);

            tupleLivre = null;

            try(
                ResultSet rset = this.stmtExiste.executeQuery()) {

                if(rset.next()) {
                    tupleLivre = new LivreDTO();
                    tupleLivre.setIdLivre(idLivre);
                    tupleLivre.setTitre(rset.getString(2));
                    tupleLivre.setAuteur(rset.getString(3));
                    tupleLivre.setDateAcquisition(rset.getDate(4));
                    tupleLivre.setIdMembre(rset.getInt(5));
                    tupleLivre.setDatePret(rset.getDate(6));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return tupleLivre;
    }

    /**
      * Ajout d'un nouveau livre dans la base de donnees.
      * @param idLivre le id d'un livre
      * @param titre le titre
      * @param auteur le auteur
      * @param dateAcquisition la dateAcquisition
      * @throws DAOException Exeptions
      */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        /* Ajout du livre. */
        try {
            this.stmtInsert.setInt(1,
                idLivre);
            this.stmtInsert.setString(2,
                titre);
            this.stmtInsert.setString(3,
                auteur);
            this.stmtInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            this.stmtInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Enregistrement de l'emprunteur d'un livre.
      *  @param idLivre le id d'un livre
      * @param idMembre le id d'un membre
      * @param datePret la date du pret
      *@throws DAOException Exeptions
      *@return update
      */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        /* Enregistrement du pret. */
        try {
            this.stmtUpdate.setInt(1,
                idMembre);
            this.stmtUpdate.setDate(2,
                Date.valueOf(datePret));
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Rendre le livre disponible (non-pr�t�).
      * @param idLivre le id d'un livre
      *@throws DAOException Exeptions
      *@return update
      */
    public int retourner(int idLivre) throws DAOException {
        /* Enregistrement du pret. */
        try {
            this.stmtUpdate.setNull(1,
                Types.INTEGER);
            this.stmtUpdate.setNull(2,
                Types.DATE);
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Suppression d'un livre.
      *
      *  @param idLivre le id d'un livre
      *@throws DAOException Exeptions
      *@return update
      */
    public int vendre(int idLivre) throws DAOException {
        /* Suppression du livre. */
        try {
            this.stmtDelete.setInt(1,
                idLivre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
