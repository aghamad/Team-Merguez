// Fichier PretService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Gilles Bénichou
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

        List<PretDTO> listeDTO = Collections.EMPTY_LIST;

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

        List<PretDTO> listePrets = Collections.EMPTY_LIST;
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

        List<PretDTO> listePrets = Collections.EMPTY_LIST;
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

        List<PretDTO> listePrets = Collections.EMPTY_LIST;
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

        List<PretDTO> listePrets = Collections.EMPTY_LIST;

        try {
            listePrets = getPretDAO().findByDatePret(dateRetour);
        } catch(final DAOException sqlException) {
            throw new ServiceException();
        }
        return listePrets;
    }

    // region Getter - Setter
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
}
