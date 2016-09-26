// Fichier ResevationDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * DTO de la table reservation.
 * @author Team-merguez
 */

public class ReservationDTO {

    private int idReservation;

    private int idLivre;

    private int idMembre;

    private Date dateReservation;

    /**para pour idReservation.
     * @param idReservation idReservation
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
     * Constructeur par défaut.
     */
    public ReservationDTO() {

    }

    /**
    *  Getter de la variable d'instance this.idReservation.
    *  @return idReservation La variable d'instance this.idReservation
    *  */
    public int getIdReservation() {
        return this.idReservation;
    }

    /**
    *  Setter de la variable d'instance this.idReservation.
    *  @param idReservation - La valeur à utiliser pour la variable d'instance this.idReservation
    *  */
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
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
    *  Getter de la variable d'instance this.idMembre.
    *  @return idMembre La variable d'instance this.idMembre
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
    *  Getter de la variable d'instance this.dateReservation.
    *  @return dateReservation La variable d'instance this.dateReservation
    *  */
    public Date getDateReservation() {
        return this.dateReservation;
    }

    /**
     *  Setter de la variable d'instance this.dateReservation.
     *  @param dateReservation - La valeur à utiliser pour la variable d'instance this.dateReservation
     *  */
    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

}
