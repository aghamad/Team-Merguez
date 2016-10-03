// Fichier  ReservationService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table reservation.
 *
 * @author Team-Merguez
 */
public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table reservation.
     *
     * @param livreDAO Le DAO de la table livre
     * @param membreDAO Le DAO de la table membre
     * @param reservationDAO Le DAO de la table reservation
     */
    public ReservationService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
    }

    // Region Getters and Setters

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
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
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */

    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // EndRegion Getters and Setters

    /**
    *
    * Ajoute une nouvelle réservation.
    *
    * @param reservationDTO - La réservation à ajouter
    * @throws ServiceException - S'il y a une erreur avec la base de données
    */
    public void add(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Lit une réservation. Si aucune réservation n'est trouvée, null est retourné.
    *
    * @param idReservation Le id d'une reservation
    * @return La réservation lue ; null sinon
    * @throws ServiceException -S'il y a une erreur avec la base de données
    */
    public ReservationDTO read(int idReservation) throws ServiceException {
        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Met à jour une réservation.
    *
    * @param reservationDTO - La réservation à mettre à jour
    * @throws ServiceException - S'il y a une erreur avec la base de données
    */
    public void update(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Supprime une reservation.
    *
    * @param reservationDTO - La réservation à supprimer
    * @throws ServiceException - S'il y a une erreur avec la base de données
    */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Trouve toutes les réservations.
    *
    * @return La liste des réservations ; une liste vide sinon
    * @throws ServiceException - S'il y a une erreur avec la base de données
    */
    public List<ReservationDTO> getAll() throws ServiceException {
        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Trouve les réservations à partir d'un membre.
    *
    * @return La liste des réservations correspondantes ; une liste vide sinon
    * @param membreDTO Le membre à utiliser
    * @throws ServiceException S'il y a une erreur avec la base de données
    */
    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws ServiceException {
        try {
            return getReservationDAO().findByMembre(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
    *
    * Trouve les réservations à partir d'un livre.
    *
    * @return La liste des réservations correspondantes ; une liste vide sinon
    * @param livreDTO Le livre à utiliser
    * @throws ServiceException S'il y a une erreur avec la base de données
    */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws ServiceException {
        try {
            return getReservationDAO().findByLivre(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Réserve un livre.
     *
     * @param reservationDTO La réservation à créer
     * @param livreDTO Le livre à reserver
     * @param membreDTO Le membre qui réserve
     * @throws ServiceException Si la réservation existe déjà,
     *                          Si le membre n'existe pas,
     *                          Si le livre n'existe pas,
     *                          Si le livre n'a pas encore été prêté,
     *                          Si le livre est déjà prêté au membre,
     *                          Si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void reserver(ReservationDTO reservationDTO,
        LivreDTO livreDTO,
        MembreDTO membreDTO) throws ServiceException {
        try {

            // Si le membre n'existe pas
            if(getMembreDAO().read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }

            //Si le livre n'existe pas
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }

            // Si la réservation existe déjà
            if(getReservationDAO().findByLivre(livreDTO) != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            // Si le livre n'a pas encore été prêté
            if(livreDTO.getIdMembre() == 0) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'a pas encore été prêté");
            }

            // Si le livre est déjà prêté au membre,
            if(livreDTO.getIdMembre() == membreDTO.getIdMembre()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est déjà prêté au membre");
            }

            //  Si le membre en question a déjà réservé ce livre
            final List<ReservationDTO> listeDesReservations = getReservationDAO().findByMembre(membreDTO);
            if(!listeDesReservations.isEmpty()) {
                for(ReservationDTO reservation : listeDesReservations) {
                    if(reservation.getIdLivre() == livreDTO.getIdLivre()) {
                        throw new ServiceException("Le membre "
                            + livreDTO.getIdLivre()
                            + " a déjà réservé ce livre");
                    }
                }
            }

            final ReservationDTO reservation = new ReservationDTO();

            reservation.setIdLivre(livreDTO.getIdLivre());
            reservation.setIdMembre(membreDTO.getIdMembre());
            reservation.setIdReservation(reservationDTO.getIdReservation());
            add(reservation);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

}
