// Fichier ReservationService.java
// Auteur : Sasha Benjamin
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
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Cette classe avec la Connexion reserve, prend une reservation et annule une reservation d'un livre.
 * @author Team-Marquez
 * */
public class ReservationService extends Service {

    private static final long serialVersionUID = 1L;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**
      * Creation d'une instance.
      * La connection de l'instance de livre et de membre doit �tre la m�me que cx,
      * afin d'assurer l'int�grit� des transactions.
      * @param livre Instance de la classe dao LivreDAO
      * @param membre Instance de la classe dao MembreDAO
      * @param reservation Instance de la classe dao ReservationDAO
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      */
    public ReservationService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws BibliothequeException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BibliothequeException("Les instances de livre, de membre et de reservation n'utilisent pas la m�me connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
      * R�servation d'un livre par un membre.
      * Le livre doit �tre pr�t�.
      * @param idReservation Le id d'une reservation
      * @param idLivre Le id d'un livre
      * @param idMembre Le id d'un membre
      * @param dateReservation La date de reservation d'un livre
      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws ServiceException,
        BibliothequeException,
        Exception {
        try {
            /* Verifier que le livre est pret� */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(tupleLivre.getIdMembre() == idMembre) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " deja prete a ce membre");
            }

            /* V�rifier que le membre existe */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date reservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new BibliothequeException("Date de reservation inferieure � la date de pret");
            }

            /* V�rifier que la r�servation n'existe pas */
            if(this.reservation.existe(idReservation)) {
                throw new BibliothequeException("R�servation "
                    + idReservation
                    + " existe deja");
            }

            /* Creation de la reservation */
            this.reservation.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Prise d'une r�servation.
      * Le livre ne doit pas �tre pr�t�.
      * Le membre ne doit pas avoir d�pass� sa limite de pret.
      * La r�servation doit la �tre la premi�re en liste.
      * @param idReservation Le id d'une reservation
      * @param datePret La date d'acquisition du livre par le membre
      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void prendreRes(int idReservation,
        String datePret) throws ServiceException,
        BibliothequeException,
        Exception {
        try {
            /* V�rifie s'il existe une r�servation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new BibliothequeException("R�servation inexistante : "
                    + idReservation);
            }

            /* V�rifie que c'est la premi�re r�servation pour le livre */
            final ReservationDTO tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new BibliothequeException("La r�servation n'est pas la premi�re de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(tupleReservation.getIdLivre());
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + tupleReservation.getIdLivre());
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new BibliothequeException("Livre "
                    + tupleLivre.getIdLivre()
                    + " deja pr�t� � "
                    + tupleLivre.getIdMembre());
            }

            /* V�rifie si le membre existe et sa limite de pret */
            final MembreDTO tupleMembre = this.membre.getMembre(tupleReservation.getIdMembre());
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + tupleReservation.getIdMembre());
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new BibliothequeException("Limite de pr�t du membre "
                    + tupleReservation.getIdMembre()
                    + " atteinte");
            }

            /* Verifier si datePret >= tupleReservation.dateReservation */
            if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
                throw new BibliothequeException("Date de pr�t inf�rieure � la date de r�servation");
            }

            /* Enregistrement du pret. */
            if(this.livre.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new BibliothequeException("Livre supprim� par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.getIdMembre()) == 0) {
                throw new BibliothequeException("Membre supprim� par une autre transaction");
            }
            /* Eliminer la r�servation */
            this.reservation.annulerRes(idReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Annulation d'une r�servation.
      * La r�servation doit exister.
      * @param idReservation Le id d'une reservation
      * @throws ServiceException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void annulerRes(int idReservation) throws ServiceException,
        BibliothequeException,
        Exception {
        try {

            /* V�rifier que la r�servation existe */
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new BibliothequeException("R�servation "
                    + idReservation
                    + " n'existe pas");
            }

            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
