// Fichier MembreDTO.java
// Auteur: Abdel Lee h.
// Date de cr�ation : 15-09-2016

package ca.qc.collegeahuntsic.bibliotheque.dto;

/**
 * Permet de repr�senter un tuple de la table membre.
 * @author [Team-Merguez]
 */

public class MembreDTO {

    private int idMembre;

    private String nom;

    private long telephone;

    private int limitePret;

    private int nbPret;
    
    

/**.
 * ceci est le  constructeur
 * @param idMembre  param id membre 
 * @param nom para nom
 * @param telephone para tel
 * @param limitePret para limite de pret 
 * @param nbPret para nombre pret 
 */
    public MembreDTO(int idMembre, String nom, long telephone, int limitePret, int nbPret) {
        super();
        this.idMembre = idMembre;
        this.nom = nom;
        this.telephone = telephone;
        this.limitePret = limitePret;
        this.nbPret = nbPret;
    }
 /**.
 *  elle get l'id Membre de pret
 *  @return idMembre 
 **/
    public int getIdMembre() {
        return this.idMembre;
    }
    /**.
     *  elle set l'id membre de pret
     *  @param idMembre 
     *  */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }
    /**.
     *  elle get le nom de pret
     *  @return nom 
     *  */
    public String getNom() {
        return this.nom;
    }
    /**.
     *  elle set le nom de pret
     *  @param nom 
     *  */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**.
     *  elle get le telephone de pret
     *  @return telephone 
     *  */
    public long getTelephone() {
        return this.telephone;
    }
    /**.
     *  elle set le nombre de pret
     *  @param telephone 
     *  */
    
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }
    
    /**.
     *  elle get le nombre de pret
     *  @return limitePret 
     *  */
    
    public int getLimitePret() {
        return this.limitePret;
    }
    /**.
     *  elle set le nombre de pret
     *  @param limitePret 
     *  */
    public void setLimitePret(int limitePret) {
        this.limitePret = limitePret;
    }

    /**.
     *  elle get le nombre de pret
     *  @return getNbPret 
     *  */
    
    public int getNbPret() {
        return this.nbPret;
    }
    
    /**.
     *  elle set le nombre de pret
     *  @param nbPret 
     *  */
    public void setNbPret(int nbPret) {
        this.nbPret = nbPret;
    }

}
