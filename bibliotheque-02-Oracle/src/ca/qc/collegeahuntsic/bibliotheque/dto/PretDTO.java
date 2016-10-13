// Fichier PretDTO.java
// Auteur: Abdel Lee h.
// Date de création : 2016-10-13
package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;

/**
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
     *
     * @param titre titre du livre
     * @param auteur auteur du livre
     * @param dateAcquisition  la date de l'aquisition
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

 
    public int getIdPret() {
        return idPret;
    }


    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }


    public int getIdMembre() {
        return idMembre;
    }


    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }


    public Timestamp getDateRetour() {
        return dateRetour;
    }


    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }


    public Timestamp getDatePret() {
        return datePret;
    }


    public void setDatePret(Timestamp datePret) {
        this.datePret = datePret;
    }

}
