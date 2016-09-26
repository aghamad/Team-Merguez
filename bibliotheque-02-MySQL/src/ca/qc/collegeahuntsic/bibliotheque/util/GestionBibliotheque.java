// Fichier GestionBibliotheque.java
// Auteur : Sasha Benjamin
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
 *<pre>
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
 * </pre>
 */

/**
 * Classe GestionBibliotheque.
 * @author Sasha Benjamin
 */
public class GestionBibliotheque {

    private Connexion conn;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private LivreService gestionLivre;

    private MembreService gestionMembre;

    private PretService gestionPret;

    private ReservationService gestionReservation;

    private GestionInterrogation gestionInterrogation;

    /**
     *
     * Crée les services nécessaires à l'application bibliothèque.
     *
     * @param serveur SQL de la BD
     * @param bd le nom de la BD
     * @param user Nom d'utilisateur sur le serveur SQL
     * @param password Mot de passe sur le serveur SQL
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     * @throws DAOException lance une exception si une erreur survient via la librairie DAO
     */

    public GestionBibliotheque(String serveur,
        String bd,
        String user,
        String password) throws BibliothequeException,
        DAOException {
        // allocation des objets pour le traitement des transactions
        try {
            try {
                this.conn = new Connexion(serveur,
                    bd,
                    user,
                    password);
            } catch(ConnexionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.livre = new LivreDAO(this.conn);
            this.membre = new MembreDAO(this.conn);
            this.reservation = new ReservationDAO(this.conn);
            this.gestionLivre = new LivreService(this.livre,
                this.reservation);
            this.gestionMembre = new MembreService(this.membre,
                this.reservation);
            try {
                this.gestionPret = new PretService(this.livre,
                    this.membre,
                    this.reservation);
            } catch(ServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                this.gestionReservation = new ReservationService(this.livre,
                    this.membre,
                    this.reservation);
            } catch(ServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.gestionInterrogation = new GestionInterrogation(this.conn);
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /** Set de conn.
     * @param conn nom de la variable conn.
     */

    /*
       public void setconn(Connexion conn) {
        this.conn = conn;
       }
       */
    /** Get de Livre.
     * @return this.livre retourne la valeur de this.livre.
     */
    public LivreDAO getLivre() {
        return this.livre;
    }

    /** Set de Livre.
     * @param livre initialisation de la variable LivreDAO.
     */
    public void setLivre(LivreDAO livre) {
        this.livre = livre;
    }

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
     */
    public MembreDAO getMembre() {
        return this.membre;
    }

    /** Set de Membre.
     * @param membre initialisation de la variable membre.
     */
    public void setMembre(MembreDAO membre) {
        this.membre = membre;
    }

    /** Get de reservation.
     * @return this.reservation retourne la valeur de this.reservation.
     */
    public ReservationDAO getReservation() {
        return this.reservation;
    }

    /** Set de reservation.
     * @param reservation initialisation de la variable reservation.
     */
    public void setReservation(ReservationDAO reservation) {
        this.reservation = reservation;
    }

    /** Get de LivreService.
     * @return this.gestionLivre retourne la valeur de this.gestionLivre.
     */
    public LivreService getGestionLivre() {
        return this.gestionLivre;
    }

    /** Set de gestionLivre.
     * @param gestionLivre initialisation de la variable gestionLivre.
     */
    public void setGestionLivre(LivreService gestionLivre) {
        this.gestionLivre = gestionLivre;
    }

    /** Get de MembreService.
     * @return this.gestionMembre retourne la valeur de this.GesitonMembre.
     */
    public MembreService getGestionMembre() {
        return this.gestionMembre;
    }

    /** Set de gestionMembre.
     * @param gestionMembre initialisation de la variable gestionMembre.
     */
    public void setGestionMembre(MembreService gestionMembre) {
        this.gestionMembre = gestionMembre;
    }

    /** Get de PretService.
     * @return this.gestionPret retourne la valeur de this.gestionPret.
     */
    public PretService getGestionPret() {
        return this.gestionPret;
    }

    /** Set de gestionPret.
     * @param gestionPret initialisation de la variable gestionPret.
     */
    public void setGestionPret(PretService gestionPret) {
        this.gestionPret = gestionPret;
    }

    /** Get de ReservationService.
     * @return this.gestionReservation retourne la valeur de this.gestionReservation.
     */
    public ReservationService getGestionReservation() {
        return this.gestionReservation;
    }

    /** Set de gestionReservation.
     * @param gestionReservation initialisation de la variable gestionReservation.
     */
    public void setGestionReservation(ReservationService gestionReservation) {
        this.gestionReservation = gestionReservation;
    }

    /** Get de GestionInterrogation.
     * @return this.gestionInterrogation retourne la valeur de this.gestionInterrogation.
     */
    public GestionInterrogation getGestionInterrogation() {
        return this.gestionInterrogation;
    }

    /** Set de gestionInterrogation.
     * @param gestionInterrogation initialisation de la variable gestionInterrogation.
     */
    public void setGestionInterrogation(GestionInterrogation gestionInterrogation) {
        this.gestionInterrogation = gestionInterrogation;
    }

    /**
      * Ouvre une connexion avec la BD relationnelle et
      * alloue les gestionnaires de transactions et de tables.
    
      */
    /**
     * Ferme la conn.
     * @throws SQLException est l'exception lancer.

     */
    public void fermer() throws SQLException {
        // fermeture de la connexion
        try {
            this.conn.fermer();
        } catch(ConnexionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
