// Fichier MembreDTO.java
// Auteur : Sasha Benjamin
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dto;

import java.util.Set;
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

    private String telephone;

    private String limitePret;

    private Set<PretDTO> prets;

    private Set<ReservationDTO> reservations;

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
    public String getTelephone() {
        return this.telephone;
    }

    /**
     * Setter de la variable d'instance <code>this.telephone</code>.
     *
     * @param string La valeur à utiliser pour la variable d'instance <code>this.telephone</code>
     */
    public void setTelephone(String string) {
        this.telephone = string;
    }

    /**
     * Getter de la variable d'instance <code>this.limitePret</code>.
     *
     * @return La variable d'instance <code>this.limitePret</code>
     */
    public String getLimitePret() {
        return this.limitePret;
    }

    /**
     * Setter de la variable d'instance <code>this.limitePret</code>.
     *
     * @param string La valeur à utiliser pour la variable d'instance <code>this.limitePret</code>
     */
    public void setLimitePret(String string) {
        this.limitePret = string;
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

    /**
     * Getter de la variable d'instance <code>this.prets</code>.
     *
     * @return La variable d'instance <code>this.prets</code>
    */
    public Set<PretDTO> getPrets() {
        return this.prets;
    }

    /**
     * Setter de la variable d'instance <code>this.prets</code>.
     *
     * @param prets La valeur à utiliser pour la variable d'instance <code>this.prets</code>
     */
    public void setPrets(Set<PretDTO> prets) {
        this.prets = prets;
    }

    /**
     * Getter de la variable d'instance <code>this.reservations</code>.
     *
     * @return La variable d'instance <code>this.reservations</code>
     */
    public Set<ReservationDTO> getReservations() {
        return this.reservations;
    }

    /**
     * Setter de la variable d'instance <code>this.reservations</code>.
    *
    * @param reservations La valeur à utiliser pour la variable d'instance <code>this.reservations</code>
     */
    public void setReservations(Set<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

}
