// Fichier MembreService.java
// Auteur :Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table membre.
 *
 * @author Team-Merguez
 */
public class MembreService extends Service {
    private static final long serialVersionUID = 1L;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    private LivreDAO livreDAO;

    /**
     *
     * Crée le service de la table livre.
     *
     * @param membreDAO Le DAO de la table membre
     * @param reservationDAO Le DAO de la table reservation
     * @param livreDAO Le DAO de la table Livre
     */
    public MembreService(MembreDAO membreDAO,
        ReservationDAO reservationDAO,
        LivreDAO livreDAO) {
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
        setLivreDAO(livreDAO);
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

    // EndRegion Getters and Setters

    /**
     *
     * Ajout d'un nouveau membre.
     *
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Supprime un membre.
     *
     * @param membreDTO Le membre à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Met à jour un membre.
     *
     * @param membreDTO Le membre à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().update(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Regarde si le membre existe. Si aucun membre n'est trouvé, null est retourné.
     *
     * @param idMembre Le id d'un membre
     * @return Un membre s'il existe; null s'il n'existe pas
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public MembreDTO read(int idMembre) throws ServiceException {
        try {
            return getMembreDAO().read(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Trouve tous les membres.
     *
     * @return La liste des membres; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<MembreDTO> getAll() throws ServiceException {
        try {
            return getMembreDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Emprunt d'un livre par un membre.
     *
     * @param membreDTO Le membre qui emprunte un livre
     * @param livreDTO Le livre à emprunter
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {

            // Si le membre n'existe pas
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "n'existe pas");
            }

            // Si le livre n'existe pas
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'existe pas");
            }

            // Si le livre a été réservé
            if(!getReservationDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "est déjà réservé");
            }

            // Si le membre a atteint sa limite de prêt
            if(membreDTO.getNbPret() == membreDTO.getLimitePret()) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + "a atteint sa limite de pret");
            }

            // if id membre de LivreDTO est egale a 0 alors il peut l'emprunter
            // Si le livre a été prêté
            if(livreDTO.getIdMembre() != 0
                && read(livreDTO.getIdMembre()) != null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "est déjà prêté");
            }

            getMembreDAO().emprunter(membreDTO);
            getLivreDAO().emprunter(livreDTO);
            livreDTO.setIdMembre(membreDTO.getIdMembre());
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Retourne un livre du membre.
     *
     * @param membreDTO Le membre qui retourne un livre
     * @param livreDTO Le livre à retourner
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre a été prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données
     */
    public void retourner(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {

            // Si le membre n'existe pas
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "n'existe pas");
            }

            // Si le livre n'existe pas
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'existe pas");
            }

            // Un livreDTO a un idMembre. Celui qui a emprunter ce livre
            // Si le livre n'a pas encore été prêté,
            if(livreDTO.getIdMembre() == 0
                || read(livreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'a pas était emprunter");
            }

            final MembreDTO emprunteur = read(livreDTO.getIdMembre());

            if(emprunteur != null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getTitre()
                    + " (ID de livre: "
                    + livreDTO.getIdLivre()
                    + ") à été prêté à "
                    + emprunteur.getNom()
                    + " (ID du membre "
                    + emprunteur.getIdMembre()
                    + " )");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            if(livreDTO.getIdMembre() != membreDTO.getIdMembre()) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'a pas était prêté par celui qui souhaite de le retourner");
            }

            getMembreDAO().retourner(membreDTO);
            getLivreDAO().retourner(livreDTO);
            livreDTO.setIdMembre(0);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Renouvelle le prêt d'un livre.
     *
     * @param membreDTO Le membre qui renouvelle
     * @param livreDTO Le livre à renouveler
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre a été prêté à quelqu'un d'autre, si le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    public void renouveler(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {

            // Si le membre n'existe pas
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "n'existe pas");
            }

            // Si le livre n'existe pas
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'existe pas");
            }

            // Si le livre a été réservé
            // Quand cette methode return une list non vide; Alors le livre est réservé pour quelqu'un d'autre
            if(!getReservationDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + "est déjà réservé");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            // N'apparteint pas au membre alors il ne peut pas le renouveler
            if(membreDTO.getIdMembre() != livreDTO.getIdMembre()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + "n'apparteint pas au membre "
                    + membreDTO.getIdMembre());
            }

            // Si le livre n'a pas encore été prêté
            if(membreDTO.getIdMembre() == 0
                || read(livreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Livre "
                    + livreDTO.getIdLivre()
                    + "n'a pas était emprunter afin d'etre renouveler");
            }

            getLivreDAO().emprunter(livreDTO);
            getMembreDAO().emprunter(membreDTO);
            livreDTO.setIdMembre(membreDTO.getIdMembre());
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Inscrit un membre.
     *
     * @param membreDTO Le membre à inscrire
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void inscrire(MembreDTO membreDTO) throws ServiceException {
        // if membre existe deja
        if(read(membreDTO.getIdMembre()) != null) {
            throw new ServiceException("Le Membre "
                + membreDTO.getIdMembre()
                + " existe déjà");
        }
        add(membreDTO);
    }

    /**
     *
     * Désincrit un membre.
     *
     * @param membreDTO Le membre à désinscrire
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {
        try {
            // if membre n'existe pas
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "n'existe pas");
            }

            if(membreDTO.getNbPret() > 0) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "a encore des prets");
            }

            if(!getReservationDAO().findByMembre(membreDTO).isEmpty()) {
                throw new ServiceException("Le Membre "
                    + membreDTO.getIdMembre()
                    + "a encore des reservations");
            }

            delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

}
