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

  public int    idReservation;
  public int    idLivre;
  public int    idMembre;
  public Date   dateReservation;
}
