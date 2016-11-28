// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
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
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Team-Merguez
 */
public class PretService extends Service implements IPretService {

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table pret
     * @throws InvalidDAOException Si un des DTOs passés en paramètres sont null
     */
    PretService(IPretDAO pretDAO) throws InvalidDAOException {
        super(pretDAO);

        setPretDAO(pretDAO);

    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
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
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDatePret(session,
                datePret,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDateRetour(session,
                dateRetour,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException {
        final LivreDTO livreDTO = pretDTO.getLivreDTO();
        pretDTO.getMembreDTO();

        final List<PretDTO> prets = new ArrayList<>(livreDTO.getPrets());
        if(!prets.isEmpty()) {
            final PretDTO unPretDTO = prets.get(0);
            throw new ExistingLoanException("Le livre "
                + livreDTO.getTitre()
                + " (ID du livre : "
                + livreDTO.getIdLivre()
                + ")"
                + " est déjà prêté à "
                + unPretDTO.getMembreDTO().getNom());
        }

        final List<ReservationDTO> reservations = new ArrayList<>(livreDTO.getReservations());
        // Vérifie si le livre est réservé par quelqu'un.
        if(reservations.isEmpty()) {
            throw new ExistingReservationException("Le livre "
                + livreDTO.getIdLivre()
                + " est réservé");
        }

        pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        pretDTO.setDateRetour(null);
        add(session,
            pretDTO);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        ServiceException {
        try {
            final PretDTO unPretDTO = (PretDTO) getPretDAO().get(session,
                pretDTO.getIdPret());

            final MembreDTO membreDTO = unPretDTO.getMembreDTO();
            final LivreDTO livreDTO = unPretDTO.getLivreDTO();

            if(livreDTO.getPrets().isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + pretDTO.getIdPret()
                    + " n'a pas été prêté");
            }

            if(!livreDTO.getReservations().isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + pretDTO.getIdPret()
                    + "a déjà été reservé");
            }

            boolean belongsToMember = false;

            final List<PretDTO> prets = new ArrayList<>(membreDTO.getPrets());
            // Regard si le livre en question appartient au membre
            for(PretDTO pretDuMembre : prets) {
                if(livreDTO.equals(pretDuMembre.getLivreDTO())) {
                    belongsToMember = true;
                }
            }

            if(!belongsToMember) {
                throw new MissingLoanException("Le livre "
                    + livreDTO.getTitre()
                    + " n'est pas prêté par"
                    + membreDTO.getIdMembre()
                    + ")");
            }

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);

        } catch(final
            DAOException
            | InvalidPrimaryKeyException
            | ExistingLoanException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException {
        try {
            final PretDTO unPretDTO = (PretDTO) getPretDAO().get(session,
                pretDTO.getIdPret());

            final LivreDTO livreDTO = unPretDTO.getLivreDTO();
            final MembreDTO membreDTO = unPretDTO.getMembreDTO();

            final List<PretDTO> prets = new ArrayList<>(membreDTO.getPrets());

            // Vérifie s'il y a au moins un pret associé à livre.
            if(prets.isEmpty()) {
                throw new MissingLoanException("le livre "
                    + livreDTO.getTitre()
                    + " n'a pas encore été prêté.");
            }

            // Vérifie si le livre a été prêté par quelqu'un d'autre.
            boolean pretDuMembre = false;
            for(final PretDTO pret : prets) {
                pretDuMembre = pret.equals(unPretDTO);
            }

            if(!pretDuMembre) {
                throw new ExistingLoanException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'est pas emprunter par ce membre");
            }

            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);
        } catch(final
            DAOException
            | InvalidPrimaryKeyException
            | ExistingLoanException daoException) {
            throw new ServiceException(daoException);
        }
    }

}
