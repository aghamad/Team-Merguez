// Fichier ResevationDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de repr�senter un tuple de la table membre.
 *
 */

public class ReservationDTO {

    private int idReservation;

    private int idLivre;

    private int idMembre;

    private Date dateReservation;

    public int getIdReservation() {
        return this.idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdLivre() {
        return this.idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public int getIdMembre() {
        return this.idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public Date getDateReservation() {
        return this.dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

}
