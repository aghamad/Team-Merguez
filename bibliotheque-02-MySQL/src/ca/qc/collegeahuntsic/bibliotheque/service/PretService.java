// Fichier PretService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table pret.
 *
 * @author Team-Merguez
 */

public class PretService {
    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**
     *
     * Crée le service de la table pret.
     *
     * @param livre Le DAO de la table membre
     * @param membre Le DAO de la table reservation
     * @param reservation Le DAO de la table reservation
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public PretService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws ServiceException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new ServiceException("Les instances de livre, de membre et de reservation n'utilisent pas la meme connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Pret du livre à un membre.
     *
     * @param idLivre id du livre à preter
     * @param idMembre paramètre id du membre qui désire preter le livre
     * @param datePret date du prêt du livre par le membre
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws ServiceException {
        try {
            /* Verfier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " deja prete a "
                    + tupleLivre.getIdMembre());
            }

            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new ServiceException("Limite de pret du membre "
                    + idMembre
                    + " atteinte");
            }

            /* Vérifie s'il existe une r�servation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                idMembre,
                datePret);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprimé par une autre transaction");
            }
            final int nb2 = this.membre.preter(idMembre);
            if(nb2 == 0) {
                throw new ServiceException("Membre supprimé par une autre transaction");
            }
            this.cx.commit();
        } catch(
            SQLException
            | DAOException exception) {
            try {
                this.cx.rollback();
            } catch(SQLException sqlException) {
                throw new ServiceException(sqlException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     *
     * Renouvellement d'un prêt.
     *
     * @param idLivre id du Livre à renouveler
     * @param datePret date de prêt du livre à renouveler
     *
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void renouveler(int idLivre,
        String datePret) throws ServiceException {
        try {
            /* Verifier si le livre est prété */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }

            /* Verifier si date renouvellement >= datePret */
            if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de renouvellement inferieure à la date de pret");
            }

            /* V�rifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                tupleLivre.getIdMembre(),
                datePret);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprime par une autre transaction");
            }
            this.cx.commit();
        } catch(
            SQLException
            | DAOException exception) {
            try {
                this.cx.rollback();
            } catch(SQLException sqlException) {
                throw new ServiceException(sqlException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     * Retourner un livre prêté.
     *
     * @param idLivre id du livre à prêté
     * @param dateRetour date de retour du livre prêté
     *
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void retourner(int idLivre,
        String dateRetour) throws ServiceException {
        try {
            /* Verifier si le livre est prété  */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas pr�t� ");
            }

            /* Verifier si date retour >= datePret */
            if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de retour inferieure � la date de pret");
            }

            /* Retour du pret. */
            final int nb1 = this.livre.retourner(idLivre);
            if(nb1 == 0) {
                throw new ServiceException("Livre supprim� par une autre transaction");
            }

            final int nb2 = this.membre.retourner(tupleLivre.getIdMembre());
            if(nb2 == 0) {
                throw new ServiceException("Livre supprim� par une autre transaction");
            }
            this.cx.commit();
        } catch(
            SQLException
            | DAOException exception) {
            try {
                this.cx.rollback();
            } catch(SQLException sqlException) {
                throw new ServiceException(sqlException);
            }
            throw new ServiceException(exception);
        }
    }
}
