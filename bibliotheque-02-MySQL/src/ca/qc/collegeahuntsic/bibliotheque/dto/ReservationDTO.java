// Fichier ResevationDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * DTO de la table reservation.
 * @author Team-merguez
 */

public class ReservationDTO extends DTO {

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
    *  Getter de la variable d'instance <code>this.idReservation</code>.
    *  @return idReservation La variable d'instance <code>this.idReservation</code>
    *  */
    public int getIdReservation() {
        return this.idReservation;
    }

    /**
    *  Setter de la variable d'instance <code>this.idReservation</code>.
    *  @param idReservation - La valeur à utiliser pour la variable d'instance <code>this.idReservation</code>
    *  */
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
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
    *  Getter de la variable d'instance <code>this.idMembre</code>.
    *  @return idMembre La variable d'instance <code>this.idMembre</code>
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
    *  Getter de la variable d'instance <code>this.dateReservation</code>.
    *  @return dateReservation La variable d'instance <code>this.dateReservation</code>
    *  */
    public Date getDateReservation() {
        return this.dateReservation;
    }

    /**
     *  Setter de la variable d'instance <code>this.dateReservation</code>.
     *  @param dateReservation - La valeur à utiliser pour la variable d'instance <code>this.dateReservation</code>
     *  */
    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

}
