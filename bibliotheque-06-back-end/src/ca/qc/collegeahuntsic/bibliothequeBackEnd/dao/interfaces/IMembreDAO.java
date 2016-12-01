// Fichier IMembreDAO.java
// Auteur : Team-Merguez
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import org.hibernate.Session;

/**
 *
 * Interface DAO pour manipuler les membres dans la base de données.
 *
 * @author Team-Merguez
 */
public interface IMembreDAO extends IDAO {
    /**
     * Trouve les membres à partir d'un nom. La liste est classée par ordre croissant sur sortByPropertyName.
     * Si aucun membre n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param nom Le nom à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des livres correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si le titre est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<MembreDTO> findByNom(Session session,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;
}
