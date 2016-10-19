// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.Collections;
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
 * Service de la table <code>pret</code>.
 *
 * @author Team-Merguez
 */
public class PretService extends Service {
    private static final long serialVersionUID = 1L;

    private PretDAO pretDAO;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pret Le DAO de la table pret
     * @param livre Le DAO de la table livre
     * @param membre Le DAO de la table membre
     * @param reservation Le DAO de la table reservation
     */
    public PretService(PretDAO pret,
        MembreDAO membre,
        LivreDAO livre,
        ReservationDAO reservation) {

        super();
        setPretDAO(pret);
        setMembreDAO(membre);
        setLivreDAO(livre);
        setReservationDAO(reservation);
    }

    /**
     * Ajoute un nouveau pret.
     *
     * @param pretDTO Le pret à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().add(pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * Supprime un pret.
     *
     * @param pretDTO Le pret à supprimer
     * @throws ServiceException S'il y a une erreur dans la base de données
     */
    public void delete(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().delete(pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * Met à jour un pret.
     *
     * @param pretDTO Le pret à mettre à jour
     * @throws ServiceException S'il y a une erreur dans la base de données
     */
    public void update(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().update(pretDTO);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
    }

    /**
     * Lit un pret.
     *
     * @param idPret L'ID du pret à lire
     * @return Le pret lu
     * @throws ServiceException S'il y a une erreur dans la base de données
     */
    public PretDTO read(int idPret) throws ServiceException {

        PretDTO pretDTO = null;

        try {
            pretDTO = getPretDAO().read(idPret);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }

        return pretDTO;
    }

    /**
     * Trouve tous les prets.
     *
     * @return La liste des prets; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> getAll() throws ServiceException {

        List<PretDTO> listeDTO = Collections.emptyList();

        try {
            listeDTO = getPretDAO().getAll();
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listeDTO;
    }

    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre Le id du membre à utiliser
     * @return La liste des prets correspondants; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByMembre(int idMembre) throws ServiceException {

        List<PretDTO> listePrets = Collections.emptyList();
        try {
            listePrets = getPretDAO().findByMembre(idMembre);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * Trouve les prêts non terminés à partir d'un livre.
     *
     * @param idLivre Le id du livre à utiliser
     * @return La liste des prets correspondants; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByLivre(int idLivre) throws ServiceException {

        List<PretDTO> listePrets = Collections.emptyList();
        try {
            listePrets = getPretDAO().findByLivre(idLivre);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * Trouve les prêts non terminés à partir d'une date de pret.
     *
     * @param datePret La date de pret à utiliser
     * @return La liste des prets correspondants; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDatePret(Timestamp datePret) throws ServiceException {

        List<PretDTO> listePrets = Collections.emptyList();
        try {
            listePrets = getPretDAO().findByDatePret(datePret);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * Trouve les prêts non terminés à partir d'une date de retour.
     *
     * @param dateRetour La date de retour à utiliser
     * @return La liste des prets correspondants; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDateRetour(Timestamp dateRetour) throws ServiceException {

        List<PretDTO> listePrets = Collections.emptyList();

        try {
            listePrets = getPretDAO().findByDatePret(dateRetour);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param pretDTO Le prêt à renouveler
     * @throws ServiceException Si le prêt n'existe pas, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre a été prêter à quelqu'un d'autre, si le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    public void renouveler(PretDTO pretDTO) throws ServiceException {
        try {

            final PretDTO unPretDTO = getPretDAO().read(pretDTO.getIdPret());
            // Vérifie si le prêt existe
            if(unPretDTO == null) {
                throw new ServiceException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            final LivreDTO unLivreDTO = getLivreDAO().read(unPretDTO.getLivreDTO().getIdLivre());
            // Vérifie si le livre existe
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + unPretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            final MembreDTO unMembreDTO = getMembreDAO().read(unPretDTO.getMembreDTO().getIdMembre());
            // Vérifie si le livre existe
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + unPretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            // Vérifie si le livre est emprunté par le membre qui tente de renouveler
            final List<PretDTO> prets = findByMembre(unMembreDTO.getIdMembre());

            if(!prets.isEmpty()) {
                // Les prets du membre
                boolean pretDumembre = false;
                for(final PretDTO pret : prets) {
                    if(pret.getMembreDTO().getIdMembre() == unPretDTO.getMembreDTO().getIdMembre()) {
                        pretDumembre = true;
                    }
                }
                if(!pretDumembre) {
                    throw new ServiceException("Le pret "
                        + pretDTO.getIdPret()
                        + " n'est pas emprunter par ce membre");
                }

            }

            // Vérifie si le livre est réservé par quelqu'un d'autre
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(unLivreDTO.getIdLivre());
            if(!reservations.isEmpty()) {
                throw new ServiceException("Le livre "
                    + unPretDTO.getLivreDTO().getIdLivre()
                    + " est réservé");
            }

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(unPretDTO);

        } catch(final DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param pretDTO Le prêt à retourner
     * @throws ServiceException Si le prêt n'existe pas,
     *                          Si le livre n'a pas encore été prêté,
     *                          Si le livre a été prêté à quelqu'un d'autre ou
     *                          S'il y a une erreur avec la base de données
     */
    public void retourner(PretDTO pretDTO) throws ServiceException {
        try {

            final PretDTO unPretDTO = getPretDAO().read(pretDTO.getIdPret());
            // Vérifie si le prêt existe
            if(unPretDTO == null) {
                throw new ServiceException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }

            final LivreDTO unLivreDTO = getLivreDAO().read(unPretDTO.getLivreDTO().getIdLivre());
            // Vérifie si le livre existe
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + unPretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            final MembreDTO unMembreDTO = getMembreDAO().read(unPretDTO.getMembreDTO().getIdMembre());
            // Vérifie si le livre existe
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + unPretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            // Vérifie si le livre a été prêté
            final List<PretDTO> prets = findByMembre(unMembreDTO.getIdMembre());
            if(prets.isEmpty()) {
                throw new ServiceException("le livre "
                    + unPretDTO.getLivreDTO().getIdLivre()
                    + " n'a pas encore été prêté.");
            }

            // Vérifie si le livre a été prêté par quelqu'un d'autre.
            boolean pretDuMembre = false;
            for(final PretDTO pret : prets) {
                pretDuMembre = pret.equals(unPretDTO);
                if(!pretDuMembre) {
                    throw new ServiceException("Le pret "
                        + pretDTO.getIdPret()
                        + " n'est pas emprunter par ce membre");
                }
            }

            getMembreDAO().retourner(unMembreDTO);
            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(unPretDTO);

        } catch(final DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * Commence un prêt.
     *
     * @param pretDTO Le prêt à commencer
     * @throws ServiceException  Si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre a été prêté,
     * si le livre a été réservé,
     * si le membre a atteint sa limite de prêt
     * ou s'il y a une erreur avec la base de données
     *
     */
    public void commencer(PretDTO pretDTO) throws ServiceException {
        try {

            final LivreDTO livreDTO = getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre());
            final MembreDTO membreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());

            // Vérifie si le membre n'existe pas
            if(membreDTO == null) {
                throw new ServiceException("Le membre "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            // Vérifie si le livre n'existe pas
            if(livreDTO == null) {
                throw new ServiceException("Le livre "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            // Vérifie si le livre est déjà prêté
            final List<PretDTO> prets = getPretDAO().findByLivre(livreDTO.getIdLivre());
            if(!prets.isEmpty()) {
                for(final PretDTO unPretDTO : prets) {
                    if(unPretDTO.getDateRetour() == null) {
                        throw new ServiceException("Le livre "
                            + livreDTO.getTitre()
                            + " (ID du livre : "
                            + livreDTO.getIdLivre()
                            + ")"
                            + " est déjà prêté");
                    }
                }
            }

            // Vérifie si le livre est réservé par quelqu'un.
            if(!getReservationDAO().findByLivre(livreDTO.getIdLivre()).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est réservé");
            }

            getMembreDAO().update(membreDTO);
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(pretDTO);
        } catch(final DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    public PretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    public MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    public void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    public ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // EndRegion Getters and Setters

}
