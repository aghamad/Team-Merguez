// Fichier LivreService.java
// Auteur : Sasha Benjamin
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Cette classe avec la Connexion acqueit et vend un livre.
 * @author Team-Marquez
 * */
public class LivreService {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**
      * Creation d'une instance.
      * @param livre Instance de la classe dao LivreDAO
      * @param reservation Instance de la classe dao ReservationDAO
      */
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau livre dans la base de donn�es.
      * S'il existe deja, une exception est lev�e.
      * @param titre Le titre d'un livre
      * @param idLivre Le id d'un livre
      * @param auteur L'auteur d'un livre
      * @param dateAcquisition La date d'acquisition du livre par le membre
      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      * @throws SQLException Une exception qui fournit des informations sur une erreur vague
      */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException,
        Exception {
        try {
            /* V�rifie si le livre existe d�ja */
            if(this.livre.existe(idLivre)) {
                throw new ServiceException("Livre existe deja: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.cx.commit();

        } catch(DAOException e) {
            this.cx.rollback();
            throw new ServiceException(e);
        }
    }

    /**
      * Vente d'un livre.
      * @param idLivre ID du Livre
      * @throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws Exception  Une exception qui fournit des informations sur une erreur vague
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws ServiceException Une exception de Serivce DAO
      */
    public void vendre(int idLivre) throws ServiceException,
        Exception {
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
                    + " r�serv� ");
            }

            /* Suppression du livre. */
            final int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(DAOException e) {
            this.cx.rollback();
            throw new ServiceException(e);
        }
    }
}
