// Fichier ReservationService.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table <code>reservation</code>.
 *
 * @author Team-Merguez
 */
public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private ReservationDAO reservationDAO;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private PretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     */
    public ReservationService(ReservationDAO reservationDAO,
        LivreDAO livreDAO,
        MembreDAO membreDAO,
        PretDAO pretDAO) {
        super();
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setPretDAO(pretDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private PretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    // EndRegion Getters and Setters

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO La réservation à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit une réservation. Si aucune réservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param idReservation L'ID de la réservation à lire
     * @return La réservation lue ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws ServiceException {
        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO La réservation à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime une réservation.
     *
     * @param reservationDTO La réservation à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve toutes les réservations.
     *
     * @return La liste des réservations ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws ServiceException {
        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param idLivre L'ID du livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(int idLivre) throws ServiceException {
        try {
            return getReservationDAO().findByLivre(idLivre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les réservations à partir d'un membre.
     *
     * @param idMembre L'ID du membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByMembre(int idMembre) throws ServiceException {
        try {
            return getReservationDAO().findByMembre(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Réserve un livre.
     *
     * @param reservationDTO La réservation à créer
     * @param membreDTO Le membre qui réserve
     * @param livreDTO Le livre à réserver
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore
     *         été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de
     *         données
     */
    public void reserver(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            if(uneReservationDTO != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }
            final MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }

            // Si le livre n'a pas été réservé à lui même
            final List<ReservationDTO> reservations = findByLivre(unLivreDTO.getIdLivre());
            for(final ReservationDTO uneReservation : reservations) {
                if(unMembreDTO.equals(uneReservation.getMembreDTO())) {
                    throw new ServiceException("Le membre "
                        + unLivreDTO.getTitre()
                        + "a déja réservé ce livre");
                }
            }
            // Si le livre n'a pas encore été prêté
            final List<PretDTO> prets = getPretDAO().findByLivre(reservationDTO.getLivreDTO().getIdLivre());
            if(prets.isEmpty()) {
                throw new ServiceException("Le livre "
                    + reservationDTO.getLivreDTO().getTitre()
                    + "n'a pas encore été prêté");
            }

            // Si le membre a déjà réservé ce livre
            final List<ReservationDTO> listeReservations = findByMembre(reservationDTO.getMembreDTO().getIdMembre());
            if(!listeReservations.isEmpty()) {
                for(final ReservationDTO uneReservation : listeReservations) {
                    if(uneReservation.getLivreDTO().getIdLivre() == reservationDTO.getLivreDTO().getIdLivre()) {
                        throw new ServiceException("Le membre "
                            + uneReservation.getMembreDTO().getIdMembre()
                            + " a déjà réservé le livre "
                            + reservationDTO.getLivreDTO().getIdLivre());
                    }
                }
            }

            final ReservationDTO unReservationDTO = new ReservationDTO();

            unReservationDTO.setIdReservation(reservationDTO.getIdReservation());
            unReservationDTO.setMembreDTO(unMembreDTO);
            unReservationDTO.setLivreDTO(unLivreDTO);
            add(unReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Utilise une réservation.
     *
     * @param reservationDTO La réservation à utiliser
     * @throws ServiceException Si la réservation n'existe pas,
     *  si le membre n'existe pas,
     *  si le livre n'existe pas,
     *  si la réservation n'est pas la première de la liste,
     *  si le livre est déjà prété,
     *  si le membre a atteint sa limite de prêt ou
     *  s'il y a une erreur avec la base de données
     */
    public void utiliser(ReservationDTO reservationDTO) throws ServiceException {
        try {

            ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            final LivreDTO unLivreDTO = getLivreDAO().read(uneReservationDTO.getLivreDTO().getIdLivre());
            final MembreDTO unMembreDTO = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());

            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(unLivreDTO.getIdLivre());
            if(!reservations.isEmpty()) {
                uneReservationDTO = reservations.get(0);
                if(uneReservationDTO.getMembreDTO().getIdMembre() != unMembreDTO.getIdMembre()) {
                    final MembreDTO booker = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());
                    throw new ServiceException("Le livre "
                        + unLivreDTO.getTitre()
                        + " (ID de livre : "
                        + unLivreDTO.getIdLivre()
                        + ") est réservé pour "
                        + booker.getNom()
                        + " (ID de membre : "
                        + booker.getIdMembre()
                        + ")");
                }
            }
            //Vérifie si le livre n'est pas déjà prêté.
            final List<PretDTO> listePret = getPretDAO().findByLivre(unLivreDTO.getIdLivre());
            if(!listePret.isEmpty()) {
                for(final PretDTO pret : listePret) {
                    if(pret.getDateRetour() == null) {
                        throw new ServiceException("Le livre "
                            + unLivreDTO.getIdLivre()
                            + " a été prêté");
                    }
                }
            }
            // Vérifier si datePret >= tupleReservation.dateReservation
            final List<PretDTO> prets = getPretDAO().findByLivre(unLivreDTO.getIdLivre());
            for(final PretDTO pretDTO : prets) {
                if(pretDTO.getLivreDTO().equals(uneReservationDTO.getLivreDTO())) {
                    if(pretDTO.getDatePret().before(uneReservationDTO.getDateReservation())) {
                        throw new ServiceException("Date de prêt inférieure à la date de réservation");
                    }
                }
            }
            // Cas éliminé en utilisant la date de prêt et de réservation comme étant la date système de la base de données

            /* Verifier si datePret >= tupleReservation.dateReservation */
            // if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
            //     throw new BibliothequeException("Date de prêt inférieure à la date de réservation");
            // }

            getMembreDAO().update(unMembreDTO);
            final PretDTO unPretDTO = new PretDTO();
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            unPretDTO.setMembreDTO(uneReservationDTO.getMembreDTO());
            unPretDTO.setLivreDTO(uneReservationDTO.getLivreDTO());
            getPretDAO().add(unPretDTO);
            annuler(uneReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Annule une réservation.
     *
     * @param reservationDTO Le reservation à annuler
     * @throws ServiceException Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    public void annuler(ReservationDTO reservationDTO) throws ServiceException {
        final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
        if(uneReservationDTO == null) {
            throw new ServiceException("La réservation "
                + reservationDTO.getIdReservation()
                + " n'existe pas");
        }
        delete(uneReservationDTO);
    }
}
