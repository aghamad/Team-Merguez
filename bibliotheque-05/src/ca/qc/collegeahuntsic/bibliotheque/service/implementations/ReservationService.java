// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-30

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IReservationService;
import org.hibernate.Session;

/**
 * Service de la table <code>réservation</code>.
 *
 * @author  Team-Merguez
 */
public class ReservationService extends Service implements IReservationService {

    private IReservationDAO reservationDAO;

    private ILivreDAO livreDAO;

    private IMembreDAO membreDAO;

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public ReservationService(ILivreDAO livreDAO,
        IMembreDAO membreDAO,
        IPretDAO pretDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        // TODO: Change the constructor visibility to package when switching to Spring
        super(reservationDAO);
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de livre ne peut être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setPretDAO(pretDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters

    /**
     *Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     *Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IMembreDAO getMembreDAO() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void placer(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {

        try {
            final ReservationDTO uneReservationDTO = (ReservationDTO) get(session,
                reservationDTO.getIdReservation());
            if(uneReservationDTO != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(session,
                reservationDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(session,
                reservationDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            final List<PretDTO> prets = getPretDAO().findByMembre(session,
                unMembreDTO.getIdMembre(),
                PretDTO.ID_PRET_COLUMN_NAME);
            if(!prets.isEmpty()) {
                for(PretDTO unPretDTO : prets) {
                    if(unPretDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                        throw new ServiceException("Le livre "
                            + unLivreDTO.getIdLivre()
                            + " est déjà prêté au membre "
                            + unMembreDTO.getIdMembre());
                    }
                }
            }

            // Si le membre a déjà réservé ce livre
            final List<ReservationDTO> reservations = getReservationDAO().findByMembre(session,
                unMembreDTO.getIdMembre(),
                PretDTO.ID_PRET_COLUMN_NAME);
            if(!prets.isEmpty()) {
                for(ReservationDTO unResDTO : reservations) {
                    if(unResDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                        throw new ServiceException("Le membre "
                            + unMembreDTO.getIdMembre()
                            + " a déjà réservé ce livre");
                    }
                }
            }

            final ReservationDTO nouvelleReservation = new ReservationDTO();
            nouvelleReservation.setMembreDTO(unMembreDTO);
            nouvelleReservation.setLivreDTO(unLivreDTO);
            add(session,
                reservationDTO);
        } catch(
            DAOException
            | InvalidPrimaryKeyException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliser(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ServiceException {
        try {

            final ReservationDTO uneReservationDTO = (ReservationDTO) getReservationDAO().get(session,
                reservationDTO.getIdReservation());

            if(uneReservationDTO != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(session,
                reservationDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(session,
                reservationDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            // Regarde si la réservation est la première dans la liste
            final ReservationDTO premiereReservationDTO = getReservationDAO().findByLivre(session,
                unLivreDTO.getIdLivre(),
                ReservationDTO.DATE_RESERVATION_COLUMN_NAME).get(0);

            final MembreDTO membrePremiereReservation = premiereReservationDTO.getMembreDTO();
            if(!membrePremiereReservation.equals(unMembreDTO)) {
                throw new ExistingLoanException("La reservation que le membre essaie d'utiliser"
                    + " ne lui appartient pas");
            }

            // Regarde si le livre n'est pas déjà prêté
            final List<PretDTO> listePrets = getPretDAO().findByLivre(session,
                unLivreDTO.getIdLivre(),
                PretDTO.ID_PRET_COLUMN_NAME);
            if(!listePrets.isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + unLivreDTO.getIdLivre()
                    + " a été prêté");
            }

            // Utiliser
            annuler(session,
                uneReservationDTO);
            getMembreDAO().update(session,
                unMembreDTO);
            final PretDTO unPretDTO = new PretDTO();
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            unPretDTO.setMembreDTO(unMembreDTO);
            unPretDTO.setLivreDTO(unLivreDTO);
            getPretDAO().add(session,
                unPretDTO);
        } catch(
            DAOException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | InvalidPrimaryKeyException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        final ReservationDTO uneReservationDTO;
        try {
            uneReservationDTO = (ReservationDTO) get(session,
                reservationDTO.getIdReservation());

            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            delete(session,
                uneReservationDTO);
        } catch(InvalidPrimaryKeyException e) {
            e.printStackTrace();
        }
    }
}
