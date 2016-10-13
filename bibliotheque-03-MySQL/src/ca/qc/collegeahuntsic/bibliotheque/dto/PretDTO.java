// Fichier PretDTO.java
// Auteur: Abdel Lee h.
// Date de création : 2016-10-13
package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;

/**
 * DTO de la table Pret.
 * @author Team-merguez
 *
 */
public class PretDTO extends DTO {

    private static final long serialVersionUID = 1L;

    
    private int idLivre;

    private int idPret;

    private int idMembre;
    
    private Timestamp dateRetour;

    private Timestamp datePret;
    
    
    /**
     * variable d'instance this.idLivre this.titre this.auteur this.dateAcquisition this.idMembre this.datePret.
     * @param idLivre id du livre
     * @param idPret pret du livre
     * @param dateRetour  la date de retour
     * @param idMembre id du membre
     * @param datePret la date de pret
     */
    public PretDTO(int idLivre,
        int idPret,
        int idMembre,
        Timestamp dateRetour,
        Timestamp datePret) {
        super();
        this.idLivre = idLivre;
        this.idPret = idPret;
        this.idMembre = idMembre;
        this.dateRetour = dateRetour;
        this.datePret = datePret;
        
    }
    
    /**
    *Constructeur par défaut.
    *
    */
    
    public PretDTO() {

    }

    /**
    *  Getter de la variable d'instance <code>this.idLivre</code>.
    *  @return idLivre La variable d'instance <code>this.idLivre</code>
    *  */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
    *  Setter de la variable d'instance <code>this.idLivre</code>.
    *  @param idLivre - La valeur à utiliser pour la variable d'instance <code>this.idLivre</code>
    *  */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
     *  Getter de la variable d'instance <code>this.idPret</code>.
     *  @return idPret La variable d'instance <code>this.idPret</code>
     *  */
    public int getIdPret() {
        return this.idPret;
    }

    /**
    *  Setter de la variable d'instance <code>this.idPret</code>.
    *  @param idPret - La valeur à utiliser pour la variable d'instance <code>this.idPret</code>
    *  */
    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    /**
    *  Getter de la variable d'instance <code>this.idMembre</code>.
    *  @return idMembre - La variable d'instance <code>this.idMembre</code>
    *  */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
    *  Setter de la variable d'instance <code>this.idMembre</code>.
    *  @param idMembre - La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
    *  */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
    *  Getter de la variable d'instance <code>this.dateRetour</code>.
    *  @return dateRetour - La variable d'instance <code>this.dateRetour</code>
    *  */
    public Timestamp getDateRetour() {
        return this.dateRetour;
    }

    /**
    *  Setter de la variable d'instance <code>this.dateRetour</code>.
    *  @param dateRetour - La valeur à utiliser pour la variable d'instance <code>this.dateRetour</code>
    *  */
    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
    *  Getter de la variable d'instance <code>this.datePret</code>.
    *  @return datePret - La variable d'instance <code>this.datePret</code>
    *  */
    public Timestamp getDatePret() {
        return this.datePret;
    }

    /**
    *  Setter de la variable d'instance <code>this.datePret</code>.
    *  @param datePret - La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
    *  */
    public void setDatePret(Timestamp datePret) {
        this.datePret = datePret;
    }

}
