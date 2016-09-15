// Fichier MembreDTO.java
// Auteur: Abdel Lee h.
// Date de création : 15-09-2016
package ca.qc.collegeahuntsic.bibliotheque.dto;
/**
 * Permet de représenter un tuple de la table membre.
 * 
 */

public class MembreDTO {

	private int    idMembre;
	private String nom;
	private long   telephone;
	private int    limitePret;
	private int    nbPret;
	  
	public int getIdMembre() {
		return idMembre;
	}
	public void setIdMembre(int idMembre) {
		this.idMembre = idMembre;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public long getTelephone() {
		return telephone;
	}
	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}
	public int getLimitePret() {
		return limitePret;
	}
	public void setLimitePret(int limitePret) {
		this.limitePret = limitePret;
	}
	public int getNbPret() {
		return nbPret;
	}
	public void setNbPret(int nbPret) {
		this.nbPret = nbPret;
	}
  
  
}
