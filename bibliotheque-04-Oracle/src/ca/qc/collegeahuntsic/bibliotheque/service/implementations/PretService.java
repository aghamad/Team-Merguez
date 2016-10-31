// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService;

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
     * @param pret Le DAO de la table pret
     * @param membre Le DAO de la table membre
     * @param livre Le DAO de la table livre
     * @param reservation Le DAO de la table reservation
     * @throws InvalidDAOException Si un des DTOs passés en paramètres sont null
     */
    public PretService(IPretDAO pret,
        IMembreDAO membre,
        ILivreDAO livre,
        IReservationDAO reservation) throws InvalidDAOException {
        super();
        if(pret == null) {
            throw new InvalidDAOException("le pret ne peut pas être null");
        }

        setPretDAO(pret);
        setMembreDAO(membre);
        setLivreDAO(livre);
        setReservationDAO(reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getPretDAO().add(connexion,
                pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getPretDAO().delete(connexion,
                pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getPretDAO().update(connexion,
                pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO get(Connexion connexion,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        PretDTO pretDTO = null;

        try {
            pretDTO = (PretDTO) getPretDAO().get(connexion,
                idPret);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return pretDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = (List<PretDTO>) getPretDAO().getAll(connexion,
                sortByPropertyName);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = getPretDAO().findByMembre(connexion,
                idMembre,
                sortByPropertyName);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = getPretDAO().findByLivre(connexion,
                idLivre,
                sortByPropertyName);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = getPretDAO().findByDatePret(connexion,
                datePret,
                sortByPropertyName);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = getPretDAO().findByDatePret(connexion,
                dateRetour,
                sortByPropertyName);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException {
        try {

            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(connexion,
                pretDTO.getLivreDTO().getIdLivre());
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(connexion,
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
            final List<PretDTO> prets = getPretDAO().findByLivre(connexion,
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
            if(!getReservationDAO().findByLivre(connexion,
                livreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME).isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }

            getMembreDAO().update(connexion,
                membreDTO);
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(connexion,
                pretDTO);
        } catch(final DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouveler(Connexion connexion,
        PretDTO pretDTO) throws ServiceException,
        InvalidDTOException,
        InvalidHibernateSessionException,
        MissingLoanException,
        ExistingLoanException,
        MissingDTOException,
        ExistingReservationException,
        InvalidPrimaryKeyException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        InvalidDTOClassException {
        try {

            final PretDTO unPretDTO = (PretDTO) getPretDAO().get(connexion,
                pretDTO.getIdPret());

            // Vérifie si le pret existe
            if(unPretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            // vérifie si le livre est emprunté par le membre qui tente de renouveler
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(connexion,
                unPretDTO.getMembreDTO().getIdMembre());
            final List<PretDTO> prets = findByMembre(connexion,
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
            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(connexion,
                unPretDTO.getLivreDTO().getIdLivre());
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(connexion,
                livreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME);
            if(!reservations.isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(connexion,
                unPretDTO);

        } catch(final DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        InvalidDTOClassException,
        ServiceException {
        try {
            final PretDTO unPretDTO = (PretDTO) getPretDAO().get(connexion,
                pretDTO.getIdPret());

            // Vérifie si le pret n'existe pas
            if(unPretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            final LivreDTO livreDTO = (LivreDTO) getLivreDAO().get(connexion,
                unPretDTO.getLivreDTO().getIdLivre());
            final MembreDTO membreDTO = (MembreDTO) getMembreDAO().get(connexion,
                unPretDTO.getMembreDTO().getIdMembre());
            final List<PretDTO> prets = findByMembre(connexion,
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

            getMembreDAO().update(connexion,
                membreDTO);

            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(connexion,
                unPretDTO);
        } catch(final DAOException daoException) {
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
