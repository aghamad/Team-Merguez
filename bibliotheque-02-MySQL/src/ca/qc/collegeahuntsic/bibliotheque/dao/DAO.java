// Fichier DAO.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 *
 * Classe de base pour tous les DAOs.
 *
 * @author Team-Merguez
 */
public class DAO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public DAO(Connexion connexion) {
        super();
        setConnexion(connexion);
    }

    /**
     *
     * Getter de la variable d'instance this.connexion.
     *
     * @return connexion La variable d'instance this.connexion
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     *
     * Setter de la variable d'instance this.connexion.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance this.connexion
     */
    public void setConnexion(final Connexion connexion) {
        this.connexion = connexion;
    }

}
