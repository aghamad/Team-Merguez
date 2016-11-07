// Fichier MembreDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 *
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Team-Merguez
 */
public class MembreDAO extends DAO implements IMembreDAO {

    /**
     *
     * Crée le DAO de la table membre.
     *
     * @param membreDTOClass La classe de membre DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est null
     */
    public MembreDAO(Class<MembreDTO> membreDTOClass) throws InvalidDTOClassException {
        super(membreDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MembreDTO> findByNom(Session session,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        return (List<MembreDTO>) find(session,
            MembreDTO.NOM_COLUMN_NAME,
            nom,
            sortByPropertyName);
    }

}
