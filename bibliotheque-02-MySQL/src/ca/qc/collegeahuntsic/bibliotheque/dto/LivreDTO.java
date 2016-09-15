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

  public int    idLivre;
  public String titre;
  public String auteur;
  public Date   dateAcquisition;
  public int    idMembre;
  public Date   datePret;
}
