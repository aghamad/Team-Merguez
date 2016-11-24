// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-30

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
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

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     * <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public ReservationService(IPretDAO pretDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        super(reservationDAO);
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setPretDAO(pretDAO);
    }

    // Region Getters and Setters

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
            final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
            final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();
            final List<PretDTO> prets = new ArrayList<>(unMembreDTO.getPrets());

            // Si le membre a déjà prété ce livre
            if(!prets.isEmpty()) {
                for(PretDTO unPretDTO : prets) {
                    if(unPretDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                        throw new ExistingLoanException("Le livre "
                            + unLivreDTO.getIdLivre()
                            + " est déjà prêté au membre "
                            + unMembreDTO.getIdMembre());
                    }
                }
            }

            // Si le membre a déjà réservé ce livre
            final List<ReservationDTO> reservations = new ArrayList<>(unMembreDTO.getReservations());
            if(!reservations.isEmpty()) {
                for(ReservationDTO unResDTO : reservations) {
                    if(unResDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                        throw new ExistingReservationException("Le membre "
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
        } catch(ExistingReservationException daoException) {
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

            final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
            final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();

            final List<ReservationDTO> reservations = new ArrayList<>(unMembreDTO.getReservations());
            if(!reservations.isEmpty()) {
                for(ReservationDTO reservDTO : reservations) {
                    if(reservDTO.getLivreDTO() == unLivreDTO) {
                        throw new ExistingReservationException("La réservation "
                            + reservationDTO.getIdReservation()
                            + " existe déjà");
                    }
                }
            }

            // Regarde si le livre n'est pas déjà prêté
            final List<PretDTO> listePrets = new ArrayList<>(unLivreDTO.getPrets());
            if(!listePrets.isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + unLivreDTO.getIdLivre()
                    + " a été prêté");
            }

            // Utiliser
            final PretDTO unPretDTO = new PretDTO();
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            unPretDTO.setDateRetour(null);
            unPretDTO.setMembreDTO(unMembreDTO);
            unPretDTO.setLivreDTO(unLivreDTO);
            getPretDAO().add(session,
                unPretDTO);
            annuler(session,
                reservationDTO);
        } catch(
            DAOException
            | ExistingReservationException
            | ExistingLoanException daoException) {
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

        try {
            final ReservationDTO uneReservationDTO = (ReservationDTO) get(session,
                reservationDTO.getIdReservation());

            if(uneReservationDTO == null) {
                throw new MissingLoanException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            delete(session,
                uneReservationDTO);
        } catch(
            InvalidPrimaryKeyException
            | MissingLoanException e) {
            e.printStackTrace();
        }
    }
}
