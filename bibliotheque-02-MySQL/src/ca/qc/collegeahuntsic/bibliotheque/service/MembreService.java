// Fichier MembreService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table membre.
 *
 * @author Team-Merguez
 */
public class MembreService {

    private Connexion cx;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**
     *
     * Crée le service de la table livre.
     *
     * @param membre Le DAO de la table membre
     * @param reservation Le DAO de la table reservation
     */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     *
     * Inscrit un membre.
     *
     * @param idMembre id du membre à inscrire
     * @param nom nom du membre à inscrire
     * @param telephone telephone du membre à inscrire
     * @param limitePret limitPret du membre à inscrire
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {
        try {
            if(this.membre.existe(idMembre)) {
                throw new ServiceException("Membre existe deja: "
                    + idMembre);
            }

            /* Ajout du membre. */
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(DAOException daoException) {
            try {
                this.cx.rollback();
            } catch(SQLException sqlException) {
                throw new ServiceException(sqlException);
            }
            throw new ServiceException(daoException);
        } catch(SQLException sqlException2) {
            throw new ServiceException(sqlException2);
        }
    }

    /**
     *
     * Désincrit un membre.
     *
     * @param idMembre id du membre à désinscrire
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void desinscrire(int idMembre) throws ServiceException {
        try {
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(DAOException daoException) {
            try {
                this.cx.rollback();
            } catch(SQLException sqlException) {
                throw new ServiceException(sqlException);
            }
            throw new ServiceException(daoException);
        } catch(SQLException sqlException2) {
            throw new ServiceException(sqlException2);
        }
    }

}
