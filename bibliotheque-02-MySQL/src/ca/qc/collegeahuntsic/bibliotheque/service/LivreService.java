// Fichier LivreService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table livre. [CRUD]
 *
 * @author Team-Merguez
 **/
public class LivreService extends Service {

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private PreparedStatement stmtExiste;

    private Connexion conn;

    /**
     *
     * Crée le service de la table livre.
     *
     * @param conn La connexion à la base de données
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public LivreService(Connexion conn) throws ServiceException {
        this.conn = conn;
        try {
            this.stmtExiste = conn.getConnection()
                .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.stmtInsert = conn.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                + "values (?,?,?,?,null,null)");
            this.stmtUpdate = conn.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
                + "where idLivre = ?");
            this.stmtDelete = conn.getConnection().prepareStatement("delete from livre where idlivre = ?");
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }

    }

    /**
     *
     * Retourner la connexion associée.
     *
     * @return conn La connexion associée
     */
    public Connexion getConnexion() {
        return this.conn;
    }

    /**
     *
     * TODO Auto-generated method javadoc.
     *
     * @param idLivre
     * @return
     * @throws ServiceException
     */
    public boolean existe(int idLivre) throws ServiceException {

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
            throw new ServiceException(sqlException);
        }
        return livreExiste;
    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param idLivre
     * @return
     * @throws ServiceException
     */
    public LivreDTO getLivre(int idLivre) throws ServiceException {
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
            throw new ServiceException(sqlException);
        }

        return tupleLivre;
    }

    /**
     *
     * Acquiert un livre.
     *
     * @param idLivre Le id d'un livre à ajouter
     * @param titre Le titre d'un livre à ajouter
     * @param auteur L'auteur d'un livre à ajouter
     * @param dateAcquisition La date d'acquisition d'un livre à ajouter
     * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException {
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
            throw new ServiceException(sqlException);
        }
    }

    /**
     *  Le livre à emprunter
     *
     * @param idLivre Le
     * @param idMembre
     * @param datePret
     * @return
     * @throws ServiceException
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws ServiceException {
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
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param idLivre
     * @return
     * @throws ServiceException
     */
    public int retourner(int idLivre) throws ServiceException {
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
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param idLivre
     * @return
     * @throws ServiceException
     */
    public int vendre(int idLivre) throws ServiceException {
        /* Suppression du livre. */
        try {
            this.stmtDelete.setInt(1,
                idLivre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

}
