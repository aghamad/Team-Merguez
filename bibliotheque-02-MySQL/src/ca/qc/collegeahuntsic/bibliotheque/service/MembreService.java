// Fichier MembreService.java
// Auteur : Sasha Benjamin
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

//import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Cette classe avec la Connexion inscrit et deinscrit un membre.
 * @author Team-Marquez
 * */
public class MembreService {

    private Connexion cx;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**
      * Creation d'une instance.
      * @param membre Instance de la classe dao MembreDAO
      * @param reservation Instance de la classe dao ReservationDAO
      */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
      * Ajout d'un nouveau membre dans la base de donnees.
      * S'il existe deja, une exception est levee.
      * @param idMembre Le id d'un membre
      * @param nom Le nom d'un membre
      * @param telephone Le numero de telephone d'un membre
      * @param limitePret La limit de pret d'un membre
      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie si le membre existe d�ja */
            if(this.membre.existe(idMembre)) {
                throw new BibliothequeException("Membre existe deja: "
                    + idMembre);
            }

            /* Ajout du membre. */
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Suppression d'un membre de la base de donnees.
      * @param idMembre Le id d'un membre

      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void desinscrire(int idMembre) throws ServiceException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie si le membre existe et son nombre de pret en cours */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new BibliothequeException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " a des r�servations");
            }

            /* Suppression du membre */
            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

}
