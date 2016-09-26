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

    /**
     * ceci est le  constructeur.
     * @param idMembre  param id membre
     * @param nom para nom
     * @param telephone para tel
     * @param limitePret para limite de pret
     * @param nbPret para nombre pret
     */
    public MembreDTO(int idMembre,
        String nom,
        long telephone,
        int limitePret,
        int nbPret) {
        super();
        this.idMembre = idMembre;
        this.nom = nom;
        this.telephone = telephone;
        this.limitePret = limitePret;
        this.nbPret = nbPret;
    }

    /**
     * Ceci est le  constructeur vide.
     */
    public MembreDTO() {

    }

    /**
     *  Getter de la variable d'instance <code>this.idMembre</code>.
     *  @return idMembre La variable d'instance <code>this.idMembre</code>
     **/
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
     *  Getter de la variable d'instance <code>this.nom</code>.
     *  @return nom La variable d'instance <code>this.nom</code>
     *  */
    public String getNom() {
        return this.nom;
    }

    /**
     *  Setter de la variable d'instance <code>this.nom</code>.
     *  @param nom - La valeur à utiliser pour la variable d'instance <code>this.nom</code>
     *  */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *  Getter de la variable d'instance <code>this.telephone</code>.
     *  @return telephone La variable d'instance <code>this.telephone</code>
     *  */
    public long getTelephone() {
        return this.telephone;
    }

    /**
     *  Setter de la variable d'instance <code>this.telephone</code>.
     *  @param telephone - La valeur à utiliser pour la variable d'instance <code>this.telephone</code>
     *  */
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    /**
     *  Getter de la variable d'instance <code>this.limitePret</code>.
     *  @return La variable d'instance <code>this.limitePret</code>
     *  */


    public int getLimitePret() {
        return this.limitePret;
    }

    /**
     *  Setter de la variable d'instance <code>this.limitePret</code>.
     *  @param limitePret - La valeur à utiliser pour la variable d'instance <code>this.limitePret</code>
     *  */
    public void setLimitePret(int limitePret) {
        this.limitePret = limitePret;
    }

    /**
     *  Getter de la variable d'instance <code>this.nbPret</code>.
     *  @return getNbPret La variable d'instance<code> this.nbPret</code>
     *  */

    public int getNbPret() {
        return this.nbPret;
    }

    /**
     *  Setter de la variable d'instance <code>this.nbPret</code>.
     *  @param nbPret - La valeur à utiliser pour la variable d'instance <code>this.nbPret</code>
     *  */
    public void setNbPret(int nbPret) {
        this.nbPret = nbPret;
    }

}
