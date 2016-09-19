// Fichier MembreDAO.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;

/**
 * Permet d'effectuer les acc�s � la table membre.
 * Cette classe g�re tous les acc�s � la table membre.
 * @author Team-Merguez
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
      * Creation d'une instance. Pr�compilation d'�nonc�s SQL.
      * @param cx la connexion
      * @throws SQLException l'exception du SQL
      */
    public MembreDAO(Connexion cx) throws SQLException {

        super(cx);
        this.cx = super.getConnexion();

        this.stmtExiste = cx.getConnection().prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
            + "values (?,?,?,?,0)");
        this.stmtUpdateIncrNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
        this.stmtUpdateDecNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from membre where idmembre = ?");
    }

    /**
      * Retourner la connexion associ�e.
      */
    @Override
    public Connexion getConnexion() {

        return this.cx;
    }

    /**
      * Verifie si un membre existe.
      *  @return MembreExiste si le membre existe
      * @throws SQLException Exeptions
      * @param idMembre parametre id
      */
    public boolean existe(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);
        boolean membreExiste = false;
        try(
            ResultSet rset = this.stmtExiste.executeQuery()) {
            membreExiste = rset.next();
            rset.close();
        }
        return membreExiste;
    }

    /**
      * Lecture d'un membre.
      *
      * @return tupleMembre si le livre existe
      * @throws SQLException Exeptions
      * @param idMembre parametre id
      */
    public MembreDTO getMembre(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);

        MembreDTO tupleMembre = null;

        try(
            ResultSet rset = this.stmtExiste.executeQuery()) {

            if(rset.next()) {
                tupleMembre = new MembreDTO();
                tupleMembre.setIdMembre(idMembre);
                tupleMembre.setNom(rset.getString(2));
                tupleMembre.setTelephone(rset.getLong(3));
                tupleMembre.setLimitePret(rset.getInt(4));
                tupleMembre.setNbPret(rset.getInt(5));
            }
        }
        return tupleMembre;
    }

    /**
      * Ajout d'un nouveau membre.
      *
      * @param idMembre le id d'un membre
      * @param nom le nom
      * @param telephone le telephone
      * @param limitePret la limite du Pret
      *  @throws SQLException Exeptions
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException {
        /* Ajout du membre. */
        this.stmtInsert.setInt(1,
            idMembre);
        this.stmtInsert.setString(2,
            nom);
        this.stmtInsert.setLong(3,
            telephone);
        this.stmtInsert.setInt(4,
            limitePret);
        this.stmtInsert.executeUpdate();
    }

    /**
      * Incrementer le nb de pret d'un membre.
      *
      *  @param idMembre le id du membre
      *@throws SQLException Exeptions
      *@return update
      */
    public int preter(int idMembre) throws SQLException {
        this.stmtUpdateIncrNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateIncrNbPret.executeUpdate();
    }

    /**
      * Decrementer le nb de pret d'un membre.
      *
      * @param idMembre le id du membre
      *@throws SQLException Exeptions
      *@return update
      */
    public int retourner(int idMembre) throws SQLException {
        this.stmtUpdateDecNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateDecNbPret.executeUpdate();
    }

    /**
      * Suppression d'un membre.
      *
      * @param idMembre le id du membre
      *@throws SQLException Exeptions
      *@return update
      */
    public int desinscrire(int idMembre) throws SQLException {
        this.stmtDelete.setInt(1,
            idMembre);
        return this.stmtDelete.executeUpdate();
    }
}
