
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

public class DAO implements Serializable {

    private Connexion connexion;

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public DAO(Connexion connexion) {
        // TODO Auto-generated constructor stub
        super();
    }

    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    public void setConnexion(final Connexion connexion) {
        this.connexion = connexion;
    }

}
