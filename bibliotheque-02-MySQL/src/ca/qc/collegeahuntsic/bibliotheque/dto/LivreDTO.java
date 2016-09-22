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

    /**.
     * @param idLivre ghfjgh
     * @param titre ytjyt
     * @param auteur jyjyh
     * @param dateAcquisition jytk
     * @param idMembre ytj
     * @param datePret jytj
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
    * TODO Auto-generated constructor javadoc.
    *
    */
    public LivreDTO() {
        // TODO Auto-generated constructor stub
    }

    /**.
    *  elle get l'id Livre de pret
    *  @return idLivre
    *  */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**.
    *  elle set l'id Livre de pret
    *  @param idLivre initialise idLivre
    *  */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**.
    *  elle get le titre de pret
    *  @return titre
    *  */
    public String getTitre() {
        return this.titre;
    }

    /**.
    *  elle set le titre de pret
    *  @param titre initialise titre
    *  */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**.
    *  elle get le auteur de pret
    *  @return auteur retourne la valeur auteur
    *  */
    public String getAuteur() {
        return this.auteur;
    }

    /**.
    *  elle set le auteur de pret
    *  @param auteur initialise auteur
    *  */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**.
    *  elle get le dateAcquisition de pret
    *  @return dataAcquisition
    *  */
    public Date getDateAcquisition() {
        return this.dateAcquisition;
    }

    /**.
    *  elle set le dateAquisition de pret
    *  @param dateAcquisition initialise dateAcquisition
    *  */
    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    /**.
    *  elle get l'Id membre de pret
    *  @return idMembre
    *  */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**.
    *  elle set l'id membre de pret
    *  @param idMembre initialise idMembre
    *  */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**.
    *  elle get le date pret de pret
    *  @return datePret
    *  */
    public Date getDatePret() {
        return this.datePret;
    }

    /**.
    *  elle set le datePret de pret
    *  @param datePret initialise datePret
    *  */
    public void setDatePret(Date datePret) {
        this.datePret = datePret;
    }

}
