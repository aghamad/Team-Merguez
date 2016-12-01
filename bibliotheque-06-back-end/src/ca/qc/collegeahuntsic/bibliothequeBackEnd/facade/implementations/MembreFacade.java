// Fichier LivreFacade.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IMembreFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IMembreService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de membres.
 *
 * @author Gilles Bénichou
 */
public class MembreFacade extends Facade implements IMembreFacade {
    private IMembreService membreService;

    /**
     * Crée la façade de la table <code>membre</code>.
     *
     * @param membreService Le service de la table <code>membre</code>
     * @throws InvalidServiceException Si le service de membre est <code>null</code>
     */
    MembreFacade(IMembreService membreService) throws InvalidServiceException {
        super(membreService);
        if(membreService == null) {
            throw new InvalidServiceException("Le service de livres ne peut être null");
        }
        setMembreService(membreService);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.membreService</code>.
     *
     * @return La variable d'instance <code>this.membreService</code>
     */
    public IMembreService getMembreService() {
        return this.membreService;
    }

    /**
     * Setter de la variable d'instance <code>this.membreService</code>.
     *
     * @param membreService La valeur à utiliser pour la variable d'instance <code>this.membreService</code>
     */
    public void setMembreService(IMembreService membreService) {
        this.membreService = membreService;
    }

    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void inscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException {
        try {
            getMembreService().inscrire(session,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void desinscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            getMembreService().desinscrire(session,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);

        }
    }
}
