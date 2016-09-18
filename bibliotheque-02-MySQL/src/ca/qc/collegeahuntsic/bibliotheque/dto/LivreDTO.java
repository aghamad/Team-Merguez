// Fichier LivreDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de repr�senter un tuple de la table livre.
 *
*/

public class LivreDTO {

    private int idLivre;

    private String titre;

    private String auteur;

    private Date dateAcquisition;

    private int idMembre;

    private Date datePret;

    public int getIdLivre() {
        return this.idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public String getTitre() {
        return this.titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return this.auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Date getDateAcquisition() {
        return this.dateAcquisition;
    }

    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public int getIdMembre() {
        return this.idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public Date getDatePret() {
        return this.datePret;
    }

    public void setDatePret(Date datePret) {
        this.datePret = datePret;
    }

}
