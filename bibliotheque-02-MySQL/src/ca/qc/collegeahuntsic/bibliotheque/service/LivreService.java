// Fichier LivreService.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table livre.
 *
 * @author Team-Merguez
 */
public class LivreService {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion connexion;

    /**
     *
     * Crée le service de la table livre.
     *
     * @param livre Le DAO de la table livre
     * @param reservation Le DAO de la table reservation
     */
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.connexion = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
    * Ajoute un nouveau livre.
    *
    * @param idLivre id d'un livre à ajouter
    * @param titre titre d'un livre à ajouter
    * @param auteur auteur d'un livre à ajouter
    * @param dateAcquisition date d'acquisition d'un livre à ajouter
    * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
    */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException {
        try {
            if(this.livre.existe(idLivre)) {
                throw new ServiceException("Livre existe deja: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.connexion.commit();

        } catch(DAOException daoException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(daoException);

        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }

    }

    /**
    * Vendre un livre.
    *
    * @param idLivre id d'un livre à vendre
    * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
    */
    public void vendre(int idLivre) throws ServiceException {
        try {
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.getIdMembre());
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " réservé ");
            }

            /* Suppression du livre. */
            final int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.connexion.commit();
        } catch(DAOException daoException) {

            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(daoException);

        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }
    }
}
