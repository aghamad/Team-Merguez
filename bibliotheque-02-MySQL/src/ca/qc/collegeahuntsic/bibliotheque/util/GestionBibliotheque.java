// Fichier GestionBibliotheque.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Système de gestion d'une bibliothèque
 *
 *
 * Ce programme permet de gérer les transaction de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédée avec JDBC.
 *
 * Pré-condition
 *   la base de données de la bibliothéque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 *
 * @author Team-Merguez
 */

public class GestionBibliotheque {

    private Connexion connexion;

    private LivreDAO livreDao;

    private MembreDAO membreDao;

    private ReservationDAO reservationDao;

    private LivreService gestionLivre;

    private MembreService gestionMembre;

    private PretService gestionPret;

    private ReservationService gestionReservation;

    private GestionInterrogation gestionInterrogation;

    /**
     *
     * Crée les services nécessaires à l'application bibliothèque.
     *
     * @param serveur serveur de la BD
     * @param bd le nom de la BD
     * @param user Nom d'utilisateur sur le serveur SQL
     * @param password Mot de passe sur le serveur SQL
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public GestionBibliotheque(String serveur,
        String bd,
        String user,
        String password) throws BibliothequeException {
        // allocation des objets pour le traitement des transactions
        try {
            try {
                this.connexion = new Connexion(serveur,
                    bd,
                    user,
                    password);

            } catch(ConnexionException connectionExeption) {
                throw new BibliothequeException(connectionExeption);
            }

            this.livreDao = new LivreDAO(this.connexion);
            this.membreDao = new MembreDAO(this.connexion);
            this.reservationDao = new ReservationDAO(this.connexion);
            this.gestionLivre = new LivreService(this.livreDao,
                this.reservationDao);
            this.gestionMembre = new MembreService(this.membreDao,
                this.reservationDao);

            this.gestionPret = new PretService(this.livreDao,
                this.membreDao,
                this.reservationDao);

            this.gestionReservation = new ReservationService(this.livreDao,
                this.membreDao,
                this.reservationDao);

            this.gestionInterrogation = new GestionInterrogation(this.connexion);
        } catch(
            SQLException
            | ServiceException
            | DAOException exception) {
            throw new BibliothequeException(exception);
        }
    }

    /**
     * Getter de la variable d'instance this.connexion.
     * @return La variable d'instance this.connexion
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance this.connnexion.
     *
     * @param conn La variable d'instance this.connexion
     */
    public void setconn(Connexion conn) {
        this.connexion = conn;
    }

    /**
     * Getter de l'instance this.livreDAO.
     * @return this.livreDAO retourne la valeur de this.livre.
     */
    public LivreDAO getLivre() {
        return this.livreDao;
    }

    /**
     * Setter de l'instance this.livreDAO.
     * @param livre initialisation de la variable LivreDAO.
     */
    public void setLivre(LivreDAO livre) {
        this.livreDao = livre;
    }

    /**
     * Getter de l'instance membreDAO.
     * @return this.membreDAO retourne la valeur de this.membre.
     */
    public MembreDAO getMembre() {
        return this.membreDao;
    }

    /**
     * Setter de l'instance this.membreDAO.
     * @param membre initialisation de la variable membre
     */
    public void setMembre(MembreDAO membre) {
        this.membreDao = membre;
    }

    /**
     * Getter de reservation.
     * @return this.reservation retourne la valeur de this.reservation.
     */
    public ReservationDAO getReservation() {
        return this.reservationDao;
    }

    /**
     * Setter de reservation.
     * @param reservation initialisation de la variable reservation.
     */
    public void setReservation(ReservationDAO reservation) {
        this.reservationDao = reservation;
    }

    /**
     * Getter de LivreService.
     * @return this.gestionLivre retourne la valeur de this.gestionLivre.
     */
    public LivreService getGestionLivre() {
        return this.gestionLivre;
    }

    /**
     * Setter de gestionLivre.
     * @param gestionLivre initialisation de la variable gestionLivre.
     */
    public void setGestionLivre(LivreService gestionLivre) {
        this.gestionLivre = gestionLivre;
    }

    /**
     * Getter de MembreService.
     * @return this.gestionMembre retourne la valeur de this.GesitonMembre.
     */
    public MembreService getGestionMembre() {
        return this.gestionMembre;
    }

    /**
     * Setter de gestionMembre.
     * @param gestionMembre initialisation de la variable gestionMembre.
     */
    public void setGestionMembre(MembreService gestionMembre) {
        this.gestionMembre = gestionMembre;
    }

    /**
     * Getter de PretService.
     * @return this.gestionPret retourne la valeur de this.gestionPret.
     */
    public PretService getGestionPret() {
        return this.gestionPret;
    }

    /**
     * Setter de gestionPret.
     * @param gestionPret initialisation de la variable gestionPret.
     */
    public void setGestionPret(PretService gestionPret) {
        this.gestionPret = gestionPret;
    }

    /**
     * Getter de ReservationService.
     * @return this.gestionReservation retourne la valeur de this.gestionReservation.
     */
    public ReservationService getGestionReservation() {
        return this.gestionReservation;
    }

    /** Setter de gestionReservation.
     * @param gestionReservation initialisation de la variable gestionReservation.
     */
    public void setGestionReservation(ReservationService gestionReservation) {
        this.gestionReservation = gestionReservation;
    }

    /**
     * Getter de GestionInterrogation.
     * @return this.gestionInterrogation retourne la valeur de this.gestionInterrogation.
     */
    public GestionInterrogation getGestionInterrogation() {
        return this.gestionInterrogation;
    }

    /**
     * Setter de gestionInterrogation.
     * @param gestionInterrogation initialisation de la variable gestionInterrogation.
     */
    public void setGestionInterrogation(GestionInterrogation gestionInterrogation) {
        this.gestionInterrogation = gestionInterrogation;
    }

    /**
     * Fermeture de la connexion.
     * @throws BibliothequeException est l'exception lancer en cas d'une erreur.
     */
    public void fermer() throws BibliothequeException {
        // fermeture de la connexion
        try {
            this.connexion.fermer();
        } catch(ConnexionException connectionExeption) {
            throw new BibliothequeException(connectionExeption);
        }
    }
}
