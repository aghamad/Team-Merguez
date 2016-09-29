// Fichier  ReservationService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
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

    private Connexion connexion;

    /**
     *
     * Crée le service de la table reservation.
     *
     * @param livre Le DAO de la table livre
     * @param membre Le DAO de la table membre
     * @param reservation Le DAO de la table reservation
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public ReservationService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws ServiceException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new ServiceException("Les instances de livre, de membre et de reservation n'utilisent pas la même connexion au serveur");
        }
        this.connexion = livre.getConnexion();
        this.livreDAO = livre;
        this.membreDAO = membre;
        this.reservationDAO = reservation;
    }

    /**
     *
     * Réserve un livre.
     *
     * @param idReservation id de la reservation
     * @param idLivre id du livre à réserver
     * @param idMembre id du membre qui à réserver
     * @param dateReservation date de reservation du livre
     * @throws ServiceException  Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws ServiceException {
        try {
            /* Verifier que le livre est preté */
            final LivreDTO tupleLivre = this.livreDAO.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(tupleLivre.getIdMembre() == idMembre) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " deja prete a ce membre");
            }

            /* Vérifier que le membre existe */
            final MembreDTO tupleMembre = this.membreDAO.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date reservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de reservation inferieure à la date de pret");
            }

            /* Vérifier que la réservation n'existe pas */
            if(this.reservationDAO.existe(idReservation)) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " existe deja");
            }

            /* Creation de la reservation */
            this.reservationDAO.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     *
     * Prise d'une réservation.
     *
     * @param idReservation id de la reservation
     * @param datePret date de pret de la reservation
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void prendreRes(int idReservation,
        String datePret) throws ServiceException {
        try {
            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservationDAO.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new ServiceException("Réservation inexistante : "
                    + idReservation);
            }

            /* Vérifie que c'est la première réservation pour le livre */
            final ReservationDTO tupleReservationPremiere = this.reservationDAO.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new ServiceException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livreDAO.getLivre(tupleReservation.getIdLivre());
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + tupleReservation.getIdLivre());
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + tupleLivre.getIdLivre()
                    + " deja prété "
                    + tupleLivre.getIdMembre());
            }

            /* Vérifie si le membre existe et sa limite de pret */
            final MembreDTO tupleMembre = this.membreDAO.getMembre(tupleReservation.getIdMembre());
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + tupleReservation.getIdMembre());
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + tupleReservation.getIdMembre()
                    + " atteinte");
            }

            /* Verifier si datePret >= tupleReservation.dateReservation */
            if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
                throw new ServiceException("Date de prêt inférieure à la date de réservation");
            }

            /* Enregistrement du pret. */
            if(this.livreDAO.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            if(this.membreDAO.preter(tupleReservation.getIdMembre()) == 0) {
                throw new ServiceException("Membre supprimé par une autre transaction");
            }
            /* Eliminer la réservation */
            this.reservationDAO.annulerRes(idReservation);
            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     *
     * Annule une réservation.
     *
     * @param idReservation id de la réservation à annuler
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void annulerRes(int idReservation) throws ServiceException {
        try {

            /* Vérifier que la réservation existe */
            if(this.reservationDAO.annulerRes(idReservation) == 0) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.connexion.commit();
        } catch(
            DAOException
            | ConnexionException exception) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }
}
