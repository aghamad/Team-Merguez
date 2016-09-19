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
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Syst�me de gestion d'une biblioth�que
 *
 *<pre>
 * Ce programme permet de g�rer les transaction de base d'une
 * biblioth�que.  Il g�re des livres, des membres et des
 * r�servations. Les donn�es sont conserv�es dans une base de
 * donn�es relationnelles acc�d�e avec JDBC.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * </pre>
 */

/**
 * TODO Auto-generated field javadoc.
 *
 * @author Sasha Benjamin
 */
public class GestionBibliotheque {
    private Connexion cx;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private LivreService gestionLivre;

    private MembreService gestionMembre;

    private PretService gestionPret;

    private ReservationService gestionReservation;

    private GestionInterrogation gestionInterrogation;

    /** Set de connection.
     * @param cx nom de la variable connection.
     */
    public void setCx(Connexion cx) {
        this.cx = cx;
    }

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

    /** Set de Livre.
     * @param membre initialisation de la variable membre.
     */
    public void setMembre(MembreDAO membre) {
        this.membre = membre;
    }

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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

    /** Get de Membre.
     * @return this.membre retourne la valeur de this.membre.
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
      * <pre>
      *
      * @param serveur SQL
      * @param bd nom de la bade de donn�es
      * @param user user id pour �tablir une connexion avec le serveur SQL
      * @param password mot de passe pour le user id
      *</pre>
      */
    /** Constructeur de classe.
     * @throws SQLException est le nom de l'exception qui est lancer.
     * @throws BibliothequeException est le nom de l'exception qui est lancer.
     */

    public GestionBibliotheque(String serveur,
        String bd,
        String user,
        String password) throws BibliothequeException,
        SQLException {
        // allocation des objets pour le traitement des transactions
        this.cx = new Connexion(serveur,
            bd,
            user,
            password);
        this.livre = new LivreDAO(this.cx);
        this.membre = new MembreDAO(this.cx);
        this.reservation = new ReservationDAO(this.cx);
        this.gestionLivre = new LivreService(this.livre,
            this.reservation);
        this.gestionMembre = new MembreService(this.membre,
            this.reservation);
        this.gestionPret = new PretService(this.livre,
            this.membre,
            this.reservation);
        this.gestionReservation = new ReservationService(this.livre,
            this.membre,
            this.reservation);
        this.gestionInterrogation = new GestionInterrogation(this.cx);
    }

    public void fermer() throws SQLException {
        // fermeture de la connexion
        this.cx.fermer();
    }
}
