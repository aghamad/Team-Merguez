// Fichier MembreDTO.java
// Auteur : Sasha Benjamin
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO de la table <code>membre</code>.
 *
 * @author Gilles Bénichou
 */
public class MembreDTO extends DTO {

    public static final String ID_MEMBRE_COLUMN_NAME = "idMembre";

    public static final String NOM_COLUMN_NAME = "nom";

    public static final String TELEPHONE_COLUMN_NAME = "telephone";

    public static final String LIMITE_PRET_COLUMN_NAME = "limitePret";

    private static final long serialVersionUID = 1L;

    private String idMembre;

    private String nom;

    private long telephone;

    private int limitePret;

    /**
     * Constructeur par défaut.
     */
    public MembreDTO() {
        super();
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public String getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.nom</code>.
     *
     * @return La variable d'instance <code>this.nom</code>
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Setter de la variable d'instance <code>this.nom</code>.
     *
     * @param nom La valeur à utiliser pour la variable d'instance <code>this.nom</code>
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de la variable d'instance <code>this.telephone</code>.
     *
     * @return La variable d'instance <code>this.telephone</code>
     */
    public long getTelephone() {
        return this.telephone;
    }

    /**
     * Setter de la variable d'instance <code>this.telephone</code>.
     *
     * @param telephone La valeur à utiliser pour la variable d'instance <code>this.telephone</code>
     */
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    /**
     * Getter de la variable d'instance <code>this.limitePret</code>.
     *
     * @return La variable d'instance <code>this.limitePret</code>
     */
    public int getLimitePret() {
        return this.limitePret;
    }

    /**
     * Setter de la variable d'instance <code>this.limitePret</code>.
     *
     * @param limitePret La valeur à utiliser pour la variable d'instance <code>this.limitePret</code>
     */
    public void setLimitePret(int limitePret) {
        this.limitePret = limitePret;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof MembreDTO;
            if(equals) {
                final MembreDTO membreDTO = (MembreDTO) obj;
                final EqualsBuilder equalsBuilder = new EqualsBuilder();
                equalsBuilder.appendSuper(super.equals(membreDTO));
                equalsBuilder.append(getIdMembre(),
                    membreDTO.getIdMembre());
                equals = equalsBuilder.isEquals();
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(461,
            451);
        hashCodeBuilder.appendSuper(super.hashCode());
        hashCodeBuilder.append(getIdMembre());
        return hashCodeBuilder.toHashCode();
    }
}
