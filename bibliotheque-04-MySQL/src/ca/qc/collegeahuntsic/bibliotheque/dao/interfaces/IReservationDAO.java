
package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;

public interface IReservationDAO extends IDAO {
    /**
     * Trouve les réservations d'un membre.
     * La liste est classée par ordre croissant sur sortByPropertyName.
     * Si aucune réservation n'est trouvée, une List vide est retournée.
     *
     * @param connexion La connexion à utiliser.
     * @param idMembre L'ID du membre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si le titre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les réservations d'un livre.
     * La liste est classée par ordre croissant sur sortByPropertyName.
     * Si aucune réservation n'est trouvée, une List vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si le titre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;
}
