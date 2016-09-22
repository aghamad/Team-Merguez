// Fichier ResevationDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de repr�senter un tuple de la table membre.
 * @author Team-merguez
 */

public class ReservationDTO {

    private int idReservation;

    private int idLivre;

    private int idMembre;

    private Date dateReservation;

    /**para pour idReservation.
     * @param idReservation 
     * @param idLivre para idLivre
     * @param idMembre para IdMembre
     * @param dateReservation para dateReservation
     */
    public ReservationDTO(int idReservation,
        int idLivre,
        int idMembre,
        Date dateReservation) {
        super();
        this.idReservation = idReservation;
        this.idLivre = idLivre;
        this.idMembre = idMembre;
        this.dateReservation = dateReservation;
    }

    /**
     * Ceci est le  constructeur vide.
     */
    public ReservationDTO() {
        // TODO Auto-generated constructor stub
    }

    /**
    *  elle set l'id Reservation de pret.
    *  @return idReservation
    *  */
    public int getIdReservation() {
        return this.idReservation;
    }

    /**
    *  elle set l'id Reservation de pret.
    *  @param idReservation Le id d'un Reservation
    *  */
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    /**
    *  elle set l'id Livre de pret.
    *  @return idLivre
    *  */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
    *  elle set l'id Livre de pret.
    *  @param idLivre Le id d'un livre
    *  */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
    *  elle set l'id Membre de pret.
    *  @return idMembre
    *  */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
    *  elle set l'id Membre de pret.
    *  @param idMembre Le id d'un membre
    *  */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
    *  elle set la dateReservation de pret.
    *  @return dateReservation
    *  */
    public Date getDateReservation() {
        return this.dateReservation;
    }

    /**
    *  elle set la dateReservation de pret.
    *  @param dateReservation La Date de reservation
    *  */
    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

}
