// Fichier LivreService.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.ILivreService;
import org.hibernate.Session;

/**
 * Service de la table <code>livre</code>.
 *
 * @author Gilles Bénichou
 */
public class LivreService extends Service implements ILivreService {

    /**
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    LivreService(ILivreDAO livreDAO) throws InvalidDAOException {
        super(livreDAO);
    }

    @Override
    public List<LivreDTO> findByTitre(Session session,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return ((ILivreDAO) getDao()).findByTitre(session,
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
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre ne peut être null");
        }

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
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre ne peut être null");
        }

        try {

            final List<PretDTO> prets = new ArrayList<>(livreDTO.getPrets());
            if(!prets.isEmpty()) {
                for(PretDTO pretDTO : prets) {
                    if(!prets.isEmpty()) {
                        final PretDTO pretDTOs = prets.get(0);
                        final MembreDTO emprunteur = pretDTOs.getMembreDTO();
                        throw new ExistingLoanException("Le livre "
                            + livreDTO.getTitre()
                            + " (ID de livre : "
                            + livreDTO.getIdLivre()
                            + ") a été prêté à "
                            + emprunteur.getNom()
                            + " (ID de membre : "
                            + emprunteur.getIdMembre()
                            + ")");
                    }
                }
            }

            final List<ReservationDTO> reservations = new ArrayList<>(livreDTO.getReservations());
            if(!reservations.isEmpty()) {
                for(ReservationDTO reservationDTO : reservations) {
                    if(!reservations.isEmpty()) {
                        final MembreDTO emprunteur = reservationDTO.getMembreDTO();
                        throw new ExistingReservationException("Le livre "
                            + livreDTO.getTitre()
                            + " (ID de livre : "
                            + livreDTO.getIdLivre()
                            + ") est réservé pour "
                            + emprunteur.getNom()
                            + " (ID de membre : "
                            + emprunteur.getIdMembre()
                            + ")");
                    }
                }

            }

            delete(session,
                livreDTO);
        } catch(
            ExistingReservationException
            | ExistingLoanException
            | ServiceException daoException) {
            throw new ServiceException(daoException);
        }
    }

}
