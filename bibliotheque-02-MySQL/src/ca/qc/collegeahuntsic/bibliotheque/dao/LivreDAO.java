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

    /*
    // stmtExiste select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?"
    private final static String READ_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre "
        + "WHERE idLivre = ?";
    
    
    private final static String ADD_REQUEST = "INSERT INTO livre "
        + "VALUES(?, ?, ?, ?, NULL, NULL)";
    
    private final static String UPDATE_REQUEST = "UPDATE livre "
        + "SET idMembre = ?, datePret = ?,"
        + "titre = ?, auteur = ?,"
        + "dateAcquisition = ?"
        + "WHERE idLivre = ?";
    
    private final static String EMPRUNT_REQUEST = "UPDATE livre "
        + "SET idMembre = ?, datePret = CURRENT_TIMESTAMP, "
        + "titre = ?, auteur = ?, "
        + "dateAcquisition = ? "
        + "WHERE idLivre = ?";
    
    private final static String RETOUR_REQUEST = "UPDATE livre "
        + "SET idMembre = null, datePret = null, "
        + "titre = ?, auteur = ?, "
        + "dateAcquisition = ? "
        + "WHERE idLivre = ?";
    
    private final static String DELETE_REQUEST = "DELETE FROM livre "
        + "WHERE idlivre = ?";
    */

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public LivreDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.stmtExiste = connexion.getConnection()
                .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.stmtInsert = connexion.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                + "values (?,?,?,?,null,null)");
            this.stmtUpdate = connexion.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
                + "where idLivre = ?");
            this.stmtDelete = connexion.getConnection().prepareStatement("delete from livre where idlivre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
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
     *
     * Lecture d'un livre.
     *
     * @param idLivre id du livre recherché
     * @return Objet de livre LivreDTO
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Rendre le livre disponible (non-prété).
     *
     * @param idLivre id du livre à rendre
     * @return resultat de la requete update
     * @throws DAOException S'il y a une erreur avec la base de données
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
     *
     * Suppression d'un livre.
     *
     * @param idLivre id du livre à vendre
     * @return resultat de ;a requete delete
     * @throws DAOException S'il y a une erreur avec la base de données
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
