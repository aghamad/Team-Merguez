// Fichier MembreService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
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

    private IPretDAO pretDAO;

    private IReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table membre.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public MembreService(IMembreDAO membreDAO,
        IPretDAO pretDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        // TODO: Change the constructor visibility to package when switching to Spring
        super(membreDAO);
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setMembreDAO(membreDAO);
        setPretDAO(pretDAO);
        setReservationDAO(reservationDAO);
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

    /**
     * Getter de la variable d'instance <code>this.preDAO</code>.
     *
     * @return La variable d'instance <code>this.preDAO</code>
     */
    private IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
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
        try {
            if(get(session,
                membreDTO.getIdMembre()) != null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " existe déjà");
            }
            add(session,
                membreDTO);
        } catch(InvalidPrimaryKeyException invalidPrimaryKeyException) {
            throw new ServiceException(invalidPrimaryKeyException);
        }

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
        try {
            final MembreDTO unMembreDTO = (MembreDTO) get(session,
                membreDTO.getIdMembre());

            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }

            if(!getReservationDAO().findByMembre(session,
                unMembreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME).isEmpty()) {
                throw new ExistingReservationException("Le membre "
                    + membreDTO.getNom()
                    + " (ID de membre : "
                    + membreDTO.getIdMembre()
                    + ") a des réservations");
            }

            if(!getPretDAO().findByMembre(session,
                unMembreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME).isEmpty()) {
                throw new ExistingLoanException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a encore des prêts");
            }

            delete(session,
                unMembreDTO);
        } catch(
            DAOException
            | InvalidPrimaryKeyException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | MissingDTOException daoException) {
            throw new ServiceException(daoException);
        }

    }

}
