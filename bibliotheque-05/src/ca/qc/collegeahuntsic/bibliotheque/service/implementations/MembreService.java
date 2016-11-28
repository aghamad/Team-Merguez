// Fichier MembreService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService;
import org.hibernate.Session;

/**
 * Service de la table membre.
 *
 * @author Team-Merguez
 */
public class MembreService extends Service implements IMembreService {

    private IMembreDAO membreDAO;

    /**
     *
     * Crée le service de la table membre.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    MembreService(IMembreDAO membreDAO) throws InvalidDAOException {

        super(membreDAO);
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }

        setMembreDAO(membreDAO);
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    public IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }
    // EndRegion Getters and Setters

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#findByNom(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    @Override
    public List<MembreDTO> findByNom(Session session,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return ((IMembreDAO) getDao()).findByNom(session,
                nom,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#inscrire(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO)
     */
    @Override
    public void inscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre DTO ne peut être null");
        }
        add(session,
            membreDTO);
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#desinscrire(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO)
     */
    @Override
    public void desinscrire(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre DTO ne peut être null");
        }

        if(!membreDTO.getPrets().isEmpty()) {
            throw new ExistingLoanException("Le membre "
                + membreDTO.getNom()
                + " (ID de membre : "
                + membreDTO.getIdMembre()
                + ") a encore des prêts");
        }

        if(!membreDTO.getReservations().isEmpty()) {
            throw new ExistingReservationException("Le membre "
                + membreDTO.getNom()
                + " (ID de membre : "
                + membreDTO.getIdMembre()
                + ") a des réservations");
        }

        delete(session,
            membreDTO);

    }

}
