// Fichier LivreDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * DTO de la table livre.
 * @author Team-Merguez
*/

public class LivreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idLivre;

    private String titre;

    private String auteur;

    private Timestamp dateAcquisition;

    private int idMembre;

    private Date datePret;

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
    public LivreDTO(int idLivre,
        String titre,
        String auteur,
        Timestamp dateAcquisition,
        int idMembre,
        Date datePret) {
        super();
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.dateAcquisition = dateAcquisition;
        this.idMembre = idMembre;
        this.datePret = datePret;
    }

    /**
    *Constructeur par défaut.
    *
    */
    public LivreDTO() {

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
    *  Getter de la variable d'instance <code>this.titre</code>.
    *  @return titre -La variable d'instance <code>this.titre</code>
    *  */
    public String getTitre() {
        return this.titre;
    }

    /**
    *  Setter de la variable d'instance <code>this.titre</code>.
    *  @param titre - La valeur à utiliser pour la variable d'instance <code>this.titre</code>
    *  */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
    *  Getter de la variable d'instance <code>this.auteur</code>.
    *  @return auteur - La variable d'instance <code>this.auteur</code>
    *  */
    public String getAuteur() {
        return this.auteur;
    }

    /**
    *  Setter de la variable d'instance <code>this.auteur</code>.
    *  @param auteur - La valeur à utiliser pour la variable d'instance <code>this.auteur</code>
    *  */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
    *  Getter de la variable d'instance <code>this.dateAcquisition</code>.
    *  @return dataAcquisition - La variable d'instance <code>this.dateAcquisition</code>.
    *  */
    public Timestamp getDateAcquisition() {
        return this.dateAcquisition;
    }

    /**
    *  Setter de la variable d'instance <code>this.dateAcquisition</code>.
    *  @param timestamp  - La valeur à utiliser pour la variable d'instance <code>this.dateAcquisition</code>
    *  */
    public void setDateAcquisition(Timestamp timestamp) {
        this.dateAcquisition = timestamp;
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
    *  Getter de la variable d'instance <code>this.datePret</code>.
    *  @return datePret - La variable d'instance <code>this.datePret</code>
    *  */
    public Date getDatePret() {
        return this.datePret;
    }

    /**
    *  Setter de la variable d'instance <code>this.datePret</code>.
    *  @param datePret - La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
    *  */
    public void setDatePret(Date datePret) {
        this.datePret = datePret;
    }

}
