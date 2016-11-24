// Fichier LivreService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.ILivreService;
import org.hibernate.Session;

/**
 * Service de la table <code>livre</code>.
 *
 * @author Gilles Bénichou
 */
public class LivreService extends Service implements ILivreService {

    private ILivreDAO livreDAO;

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public LivreService(ILivreDAO livreDAO) throws InvalidDAOException {
        // TODO: Change the constructor visibility to package when switching to Spring
        super(livreDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.LivreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    // End Getters and Setters
    @Override
    public List<LivreDTO> findByTitre(Session session,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getLivreDAO().findByTitre(session,
                titre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquerir(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        add(session,
            livreDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void vendre(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {

        try {
            final LivreDTO unLivreDTO = (LivreDTO) get(session,
                livreDTO.getIdLivre());
            if(!unLivreDTO.getIdLivre().isEmpty()) {
                throw new MissingDTOException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }

            final List<PretDTO> prets = new ArrayList<>(unLivreDTO.getPrets());
            if(!prets.isEmpty()) {
                for(PretDTO pretDTO : prets) {
                    if(pretDTO.getDateRetour().before(unLivreDTO.getDateAcquisition())) {
                        throw new ExistingLoanException("Le livre "
                            + unLivreDTO.getTitre()
                            + " (ID de livre : "
                            + unLivreDTO.getIdLivre()
                            + ") a été prêté à "
                            + pretDTO.getMembreDTO().getNom()
                            + " (ID de membre : "
                            + pretDTO.getMembreDTO().getIdMembre()
                            + ")");
                    }
                }
            }

            final List<ReservationDTO> reservations = new ArrayList<>(unLivreDTO.getReservations());
            if(!reservations.isEmpty()) {
                for(ReservationDTO reservationDTO : reservations) {
                    if(reservationDTO.getDateReservation().before(unLivreDTO.getDateAcquisition())) {

                        throw new ExistingReservationException("Le livre "
                            + unLivreDTO.getTitre()
                            + " (ID de livre : "
                            + unLivreDTO.getIdLivre()
                            + ") est réservé pour "
                            + reservationDTO.getMembreDTO().getNom()
                            + " (ID de membre : "
                            + reservationDTO.getMembreDTO().getIdMembre()
                            + ")");
                    }
                }

            }
            delete(session,
                unLivreDTO);
        } catch(
            ExistingReservationException
            | ExistingLoanException
            | ServiceException
            | InvalidPrimaryKeyException
            | MissingDTOException daoException) {
            throw new ServiceException(daoException);
        }
    }

}
