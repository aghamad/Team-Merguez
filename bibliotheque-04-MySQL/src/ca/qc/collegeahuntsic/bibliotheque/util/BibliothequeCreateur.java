// Fichier BibliothequeCreateur.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.facade.implementations.LivreFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.implementations.MembreFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.implementations.PretFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.implementations.ReservationFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.ILivreFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.IMembreFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.IPretFacade;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.IReservationFacade;
import ca.qc.collegeahuntsic.bibliotheque.service.implementations.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.implementations.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.implementations.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.implementations.ReservationService;

/**
 * Utilitaire de création des outils de la bibliothèque.
 *
 * @author Team-Merguez
 */
public class BibliothequeCreateur {
    private Connexion connexion;

    private ILivreFacade livreFacade;

    private IMembreFacade membreFacade;

    private IPretFacade pretFacade;

    private IReservationFacade reservationFacade;

    /**
     * Crée les services nécessaires à l'application bibliothèque.
     *
     * @param typeServeur - Type de serveur SQL de la BD
     * @param schema - Nom du schéma de la base de données
     * @param nomUtilisateur - Nom d'utilistaeur sur le serveur SQL
     * @param password - Mot de passe sur le serveur SQL
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données.
     */
    @SuppressWarnings("resource")
    public BibliothequeCreateur(String typeServeur,
        String schema,
        String nomUtilisateur,
        String password) throws BibliothequeException {
        try {
            setConnexion(new Connexion(typeServeur,
                schema,
                nomUtilisateur,
                password));

            final LivreDAO livreDAO = new LivreDAO(LivreDTO.class);
            final MembreDAO membreDAO = new MembreDAO(MembreDTO.class);
            final ReservationDAO reservationDAO = new ReservationDAO(ReservationDTO.class);
            final PretDAO pretDAO = new PretDAO(PretDTO.class);

            setLivreFacade(new LivreFacade(new LivreService(livreDAO,
                membreDAO,
                pretDAO,
                reservationDAO)));
            setMembreFacade(new MembreFacade(new MembreService(membreDAO,
                pretDAO,
                reservationDAO)));
            setPretFacade(new PretFacade(new PretService(pretDAO,
                membreDAO,
                livreDAO,
                reservationDAO)));
            setReservationFacade(new ReservationFacade(new ReservationService(livreDAO,
                membreDAO,
                pretDAO,
                reservationDAO)));

        } catch(ConnexionException connecionException) {
            throw new BibliothequeException(connecionException);
        } catch(InvalidDAOException invalidDAOException) {
            throw new BibliothequeException(invalidDAOException);
        } catch(InvalidDTOClassException invalidDTOClassException) {
            throw new BibliothequeException(invalidDTOClassException);
        } catch(InvalidServiceException invalidServiceException) {
            throw new BibliothequeException(invalidServiceException);
        }
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.connexion</code>.
     *
     * @return La variable d'instance <code>this.connexion</code>
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    private void setConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     * Getter de la variable d'instance <code>this.pretFacade</code>.
     *
     * @return La variable d'instance <code>this.pretFacade</code>
     */
    public IPretFacade getPretFacade() {
        return this.pretFacade;
    }

    /**
     * Setter de la variable d'instance <code>this.pretFacade</code>.
     *
     * @param pretFacade La valeur à utiliser pour la variable d'instance <code>this.pretFacade</code>
     */
    private void setPretFacade(IPretFacade pretFacade) {
        this.pretFacade = pretFacade;
    }

    /**
     * Getter de la variable d'instance <code>this.membreFacade</code>.
     *
     * @return La variable d'instance <code>this.membreFacade</code>
     */
    public IMembreFacade getMembreFacade() {
        return this.membreFacade;
    }

    /**
     * Setter de la variable d'instance <code>this.membreFacade</code>.
     *
     * @param membreFacade La valeur à utiliser pour la variable d'instance <code>this.membreFacade</code>
     */
    private void setMembreFacade(IMembreFacade membreFacade) {
        this.membreFacade = membreFacade;
    }

    /**
     * Getter de la variable d'instance <code>this.livreFacade</code>.
     *
     * @return La variable d'instance <code>this.livreFacade</code>
     */
    public ILivreFacade getLivreFacade() {
        return this.livreFacade;
    }

    /**
     * Setter de la variable d'instance <code>this.livreFacade</code>.
     *
     * @param livreFacade La valeur à utiliser pour la variable d'instance <code>this.livreFacade</code>
     */
    private void setLivreFacade(ILivreFacade livreFacade) {
        this.livreFacade = livreFacade;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationFacade</code>.
     *
     * @return La variable d'instance <code>this.reservationFacade</code>
     */
    public IReservationFacade getReservationFacade() {
        return this.reservationFacade;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationFacade</code>.
     *
     * @param reservationFacade La valeur à utiliser pour la variable d'instance <code>this.reservationFacade</code>
     */
    private void setReservationFacade(IReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    // EndRegion Getters and Setters

    /**
     * Effectue un commit sur la connexion.
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public void commit() throws BibliothequeException {
        try {
            getConnexion().commit();
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }

    /**
     * Effectue un rollback sur la connexion.
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public void rollback() throws BibliothequeException {
        try {
            getConnexion().rollback();
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }

    /**
     * Ferme la connexion.
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public void close() throws BibliothequeException {
        try {
            getConnexion().close();
        } catch(Exception exception) {
            throw new BibliothequeException(exception);
        }
    }
}
