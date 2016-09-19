// Fichier PretService.java
// Auteur : Sasha Benjamin
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
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;

/**
 * Cette classe avec la Connexion renouvele, retourne et pret un livre pour un membre.
 * @author Team-Marquez
 * */

public class PretService {
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
    public PretService(LivreDAO livre,
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
      * Pret d'un livre � un membre.
      * Le livre ne doit pas �tre pr�t�.
      * Le membre ne doit pas avoir d�pass� sa limite de pret.
      * @param idLivre Le id d'un livre
      * @param idMembre Le id d'un membre
      * @param datePret La date d'acquisition du livre par le membre
      * @throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* Verfier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " deja prete a "
                    + tupleLivre.getIdMembre());
            }

            /* V�rifie si le membre existe et sa limite de pret */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new BibliothequeException("Limite de pret du membre "
                    + idMembre
                    + " atteinte");
            }

            /* V�rifie s'il existe une r�servation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BibliothequeException("Livre r�serv� par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                idMembre,
                datePret);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre supprim� par une autre transaction");
            }
            final int nb2 = this.membre.preter(idMembre);
            if(nb2 == 0) {
                throw new BibliothequeException("Membre supprim� par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Renouvellement d'un pret.
      * Le livre doit �tre pr�t�.
      * Le livre ne doit pas �tre r�serv�.
      * @param idLivre Le id d'un livre
      * @param datePret La date d'acquisition du livre par le membre
      * @throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void renouveler(int idLivre,
        String datePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* Verifier si le livre est pr�t� */
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

            /* Verifier si date renouvellement >= datePret */
            if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
                throw new BibliothequeException("Date de renouvellement inferieure � la date de pret");
            }

            /* V�rifie s'il existe une r�servation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BibliothequeException("Livre r�serv� par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            final int nb1 = this.livre.preter(idLivre,
                tupleLivre.getIdMembre(),
                datePret);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre supprime par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
      * Retourner un livre pr�t�
      * Le livre doit �tre pr�t�.
      * @param idLivre Le id d'un livre
      * @param dateRetour La date du retour du livre
      * @throws SQLException Une exception qui fournit des informations sur une erreur d'accès de base de données ou d'autres erreurs
      * @throws BibliothequeException Une exception qui fournit des informations sur une erreur de la bibliotheque ou d'autres erreurs
      * @throws Exception Une exception qui fournit des informations sur une erreur vague
      */
    public void retourner(int idLivre,
        String dateRetour) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            /* Verifier si le livre est pr�t� */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " n'est pas pr�t� ");
            }

            /* Verifier si date retour >= datePret */
            if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
                throw new BibliothequeException("Date de retour inferieure � la date de pret");
            }

            /* Retour du pret. */
            final int nb1 = this.livre.retourner(idLivre);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre supprim� par une autre transaction");
            }

            final int nb2 = this.membre.retourner(tupleLivre.getIdMembre());
            if(nb2 == 0) {
                throw new BibliothequeException("Livre supprim� par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
