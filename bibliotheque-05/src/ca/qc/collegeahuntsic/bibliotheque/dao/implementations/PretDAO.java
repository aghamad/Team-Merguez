// Fichier PretDAO.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Team-Merguez
 */

public class PretDAO extends DAO implements IPretDAO {
    /**
     * Crée le DAO de la table <code>pret</code>.
     *
     * @param pretDTOClass La classe de pret DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de pret DTO est <code>null</code>
     */
    PretDAO(Class<PretDTO> pretDTOClass) throws InvalidDTOClassException {
        super(pretDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("La idMembre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer les prets ne peut être null");
        }

        return (List<PretDTO>) find(session,
            PretDTO.ID_MEMBRE_COLUMN_NAME,
            idMembre,
            sortByPropertyName);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(idLivre == null) {
            throw new InvalidCriterionException("La idLivre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer les prets ne peut être null");
        }

        return (List<PretDTO>) find(session,
            PretDTO.ID_LIVRE_COLUMN_NAME,
            idLivre,
            sortByPropertyName);
    }

    /**
     * {@inheritDoc}
     * @throws
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(datePret == null) {
            throw new InvalidCriterionException("La datePret ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer les prets ne peut être null");
        }

        return (List<PretDTO>) find(session,
            PretDTO.DATE_PRET_COLUMN_NAME,
            datePret,
            sortByPropertyName);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(dateRetour == null) {
            throw new InvalidCriterionException("La dateRetour ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer les prets ne peut être null");
        }

        return (List<PretDTO>) find(session,
            PretDTO.DATE_RETOUR_COLUMN_NAME,
            dateRetour,
            sortByPropertyName);
    }

}
