// Fichier IPretService.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import org.hibernate.Session;

/**
 * Interface de service pour manipuler les prets dans la base de données.
 *
 * @author Gilles Benichou
 */
public interface IPretService extends IService {

    /**
     *
     * Trouve les prêts à partir d'une date de prêt. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param datePret La date de prêt à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la session Hibernate est null
     * @throws InvalidCriterionException  Si la propriété à utiliser est null
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
    *
    * Trouve les prêts à partir d'une date de retour. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
    *
    * @param session La session Hibernate à utiliser
    * @param dateRetour La date de retour à trouver
    * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
    * @return La liste des prêts correspondants; une liste vide sinon
    * @throws InvalidHibernateSessionException Si la session Hibernate est null
    * @throws InvalidCriterionException  Si la propriété à utiliser est null
    * @throws InvalidCriterionValueException Si la valeur à trouver est null
    * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
    * @throws ServiceException S'il y a une erreur avec la base de données
    */
    List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     * Trouve les pret à partir d'un membre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun membre
     * n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session utilisé
     * @param idMembre Le membre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prets correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si le titre est <code>null</code>
     * @throws InvalidCriterionValueException Si la value est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Commence un prêt.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à commencer
     * @throws InvalidHibernateSessionException Si la session Hibernate est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws ExistingLoanException Si le livre a été prêté
     * @throws InvalidLoanLimitException Si le livre a été prêté
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException;

    /**
    *
    * Renouvelle le prêt d'un livre.
    *
    * @param session La session Hibernate à utiliser
    * @param pretDTO Le prêt à renouveler
    * @throws InvalidHibernateSessionException Si la session Hibernate est null
    * @throws InvalidDTOException Si le prêt est null
    * @throws MissingLoanException Si le livre n'a pas encore été prêté
    * @throws ExistingReservationException Si le livre a été réservé
    * @throws ServiceException S'il y a une erreur avec la base de données
    */
    void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        ServiceException;

    /**
     *
     * Termine un prêt.
     *
    * @param session La session Hibernate à utiliser
    * @param pretDTO Le prêt à terminer
    * @throws InvalidHibernateSessionException Si la session Hibernate est null
    * @throws InvalidDTOException Si le prêt est null
    * @throws MissingLoanException Si le livre n'a pas encore été prêté
    * @throws ServiceException S'il y a une erreur avec la base de données
    */
    void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException;
}
