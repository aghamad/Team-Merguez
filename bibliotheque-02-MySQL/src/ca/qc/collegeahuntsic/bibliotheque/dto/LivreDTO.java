// Fichier LivreDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de représenter un tuple de la table livre.
 * @author Team-Merguez
*/

public class LivreDTO {

    private int idLivre;

    private String titre;

    private String auteur;

    private Date dateAcquisition;

    private int idMembre;

    private Date datePret;

    /**
     * variable d'instance this.idLivre this.titre this.auteur this.dateAcquisition this.idMembre this.datePret.
     * @param idLivre ghfjgh
     * 
     * @param titre 
     * @param auteur 
     * @param dateAcquisition 
     * @param idMembre 
     * @param datePret 
     */
    public LivreDTO(int idLivre,
        String titre,
        String auteur,
        Date dateAcquisition,
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
        // TODO Auto-generated constructor stub
    }

    /**
    *  Getter de la variable d'instance this.idLivre.
    *  @return idLivre La variable d'instance this.idLivre
    *  */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
    *  Setter de la variable d'instance this.idLivre.
    *  @param idLivre - La valeur à utiliser pour la variable d'instance this.idLivre
    *  */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
    *  Getter de la variable d'instance this.titre.
    *  @return titre -La variable d'instance this.titre
    *  */
    public String getTitre() {
        return this.titre;
    }

    /**
    *  Setter de la variable d'instance this.titre.
    *  @param titre - La valeur à utiliser pour la variable d'instance this.titre
    *  */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
    *  Getter de la variable d'instance this.auteur.
    *  @return auteur - La variable d'instance this.auteur
    *  */
    public String getAuteur() {
        return this.auteur;
    }

    /**
    *  Setter de la variable d'instance this.auteur.
    *  @param auteur - La valeur à utiliser pour la variable d'instance this.auteur
    *  */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
    *  Getter de la variable d'instance this.dateAcquisition.
    *  @return dataAcquisition - La variable d'instance this.dateAcquisition.
    *  */
    public Date getDateAcquisition() {
        return this.dateAcquisition;
    }

    /**
    *  Setter de la variable d'instance this.dateAcquisition.
    *  @param dateAcquisition  - La valeur à utiliser pour la variable d'instance this.dateAcquisition
    *  */
    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    /**
    *  Getter de la variable d'instance this.idMembre.
    *  @return idMembre - La variable d'instance this.idMembre
    *  */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
    *  Setter de la variable d'instance this.idMembre.
    *  @param idMembre - La valeur à utiliser pour la variable d'instance this.idMembre
    *  */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
    *  Getter de la variable d'instance this.datePret.
    *  @return datePret - La variable d'instance this.datePret
    *  */
    public Date getDatePret() {
        return this.datePret;
    }

    /**
    *  Setter de la variable d'instance this.datePret.
    *  @param datePret - La valeur à utiliser pour la variable d'instance this.datePret
    *  */
    public void setDatePret(Date datePret) {
        this.datePret = datePret;
    }

}
