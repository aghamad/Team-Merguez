// Fichier LivreDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * DAO pour effectuer des CRUDs avec la table <code>livre</code>. Acquiri et vrendre sont la parce que c les seuls methodes qui ont besion de livreservice
 * @author Team-Merguez
 */

public class LivreDAO extends DAO {

    /**
     * DAO pour effectuer des CRUDs avec la table livre.
     *
     * @author Ahmad Agha
     */
    private static final long serialVersionUID = 1L;

    private Connexion cx;

    private LivreService livreService;

    // faire reservationService demain faut que tu switch les deux
    private ReservationService reservationService;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param livreService
     * @param reservationService
     */
    public LivreDAO(LivreService livreService,
        ReservationService reservationService) {
        super(livreService.getConnexion());
        this.cx = super.getConnexion();
        this.livreService = livreService;
        this.reservationService = reservationService;

    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws DAOException
     * @throws Exception
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException,
        Exception {
        try {
            /* V�rifie si le livre existe d�ja */
            if(this.livreService.existe(idLivre)) {
                throw new ServiceException("Livre existe deja: "
                    + idLivre);
            }

            /* Ajout du livre dans la table des livres */
            this.livreService.acquerir(idLivre,
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
     *
     * TODO Auto-generated method javadoc
     *
     * @param idLivre
     * @throws DAOException
     */
    public void vendre(int idLivre) throws DAOException {
        try {
            final LivreDTO tupleLivre = this.livreService.getLivre(idLivre);

            if(tupleLivre == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }

            if(tupleLivre.getIdMembre() != 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.getIdMembre());
            }

            if(this.reservationService.getReservationLivre(idLivre) != null) {
                throw new DAOException("Livre "
                    + idLivre
                    + " r�serv� ");
            }

            /* Suppression du livre. */
            final int nb = this.livreService.vendre(idLivre);
            if(nb == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(ServiceException e) {
            this.cx.rollback();
            throw new DAOException(e);
        }
    }

}
