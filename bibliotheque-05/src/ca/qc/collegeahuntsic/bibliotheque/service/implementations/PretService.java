// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-27

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
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
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

    private IMembreDAO membreDAO;

    private ILivreDAO livreDAO;

    private IReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table pret
     * @param membreDAO Le DAO de la table membre
     * @param livreDAO Le DAO de la table livre
     * @param reservationDAO Le DAO de la table reservation
     * @throws InvalidDAOException Si un des DTOs passés en paramètres sont null
     */
    public PretService(IPretDAO pretDAO,
        IMembreDAO membreDAO,
        ILivreDAO livreDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        super(pretDAO);
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de livre ne peut être null");
        }

        setPretDAO(pretDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);

    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#findByMembre(org.hibernate.Session, java.lang.String, java.lang.String)
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

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#findByDatePret(org.hibernate.Session, java.sql.Timestamp, java.lang.String)
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

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#findByDateRetour(org.hibernate.Session, java.sql.Timestamp, java.lang.String)
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

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#commencer(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO)
     */
    @Override
    public void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException {
        try {

            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(session,
                pretDTO.getLivreDTO().getIdLivre());
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(session,
                pretDTO.getMembreDTO().getIdMembre());

            // Vérifie si le membre n'existe pas
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            // Vérifie si le livre n'existe pas
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            // Vérifie si le livre est déjà prêté
            final List<PretDTO> prets = getPretDAO().findByLivre(session,
                livreDTO.getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);
            if(!prets.isEmpty()) {
                for(final PretDTO unPretDTO : prets) {
                    if(unPretDTO.getDateRetour() == null) {
                        throw new ExistingLoanException("Le livre "
                            + livreDTO.getTitre()
                            + " (ID du livre : "
                            + livreDTO.getIdLivre()
                            + ")"
                            + " est déjà prêté");
                    }
                }
            }

            // Vérifie si le livre est réservé par quelqu'un.
            if(!getReservationDAO().findByLivre(session,
                livreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME).isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }

            getMembreDAO().update(session,
                membreDTO);
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(session,
                pretDTO);
        } catch(final
            DAOException
            | InvalidPrimaryKeyException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | MissingDTOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#renouveler(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO)
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

            // Vérifie si le pret existe
            if(unPretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            // Vérifie si le livre est emprunté par le membre qui tente de renouveler
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre());
            final List<PretDTO> prets = findByMembre(session,
                membreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);
            if(!prets.isEmpty()) {
                boolean pretDuMembre = false;
                for(final PretDTO pret : prets) {
                    if(pret.getMembreDTO().getIdMembre().equals(unPretDTO.getMembreDTO().getIdMembre())) {
                        pretDuMembre = true;
                    }
                }
                if(!pretDuMembre) {
                    throw new ExistingLoanException("Le pret "
                        + pretDTO.getIdPret()
                        + " n'est pas emprunter par ce membre");
                }
            }

            // Vérifie si le livre est réservé par quelqu'un.
            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre());
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(session,
                livreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME);
            if(!reservations.isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);

        } catch(final
            DAOException
            | InvalidPrimaryKeyException
            | ExistingLoanException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | MissingDTOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService#terminer(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO)
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

            // Vérifie si le pret n'existe pas
            if(unPretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre());
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre());
            final List<PretDTO> prets = findByMembre(session,
                membreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);

            //Vérifie s'il y a au moins un pret associé à livre.
            if(prets.isEmpty()) {
                throw new MissingLoanException("le livre "
                    + livreDTO.getTitre()
                    + " n'a pas encore été prêté.");
            }

            //Vérifie si le livre a été prêté par quelqu'un d'autre.
            boolean pretDuMembre = false;
            for(final PretDTO pret : prets) {
                pretDuMembre = pret.equals(unPretDTO);
            }

            if(!pretDuMembre) {
                throw new ExistingLoanException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'est pas emprunter par ce membre");
            }

            getMembreDAO().update(session,
                membreDTO);
            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);
        } catch(final
            DAOException
            | InvalidPrimaryKeyException
            | ExistingLoanException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | MissingDTOException daoException) {
            throw new ServiceException(daoException);
        }
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
    public void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    public IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    public void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

}
