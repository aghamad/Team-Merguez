// Fichier DAO.java
// Auteur : Team Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 *
 *la classe DAO.
 *
 * @author Mohamed Nassim Laleg
 *
 */
public class DAO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    /**
     *la connexion.
     *@param connexion la connexion
     */
    public DAO(Connexion connexion) {
        // TODO Auto-generated constructor stub
        setConnexion(connexion);
    }

    /**
     *
     * get connexion.
     *
     * @return connexion
     */
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
