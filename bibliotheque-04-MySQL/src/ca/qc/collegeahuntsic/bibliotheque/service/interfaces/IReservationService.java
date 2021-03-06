// Fichier IReservationService.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;

/**
 * Interface de service pour manipuler les réservations dans la base de données.
 *
 * @author Gilles Benichou
 */
public interface IReservationService extends IService {
    /**
     *
     * TODO Ajoute une nouvelle réservation dans la base de données.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - La réservation à ajouter.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void add(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * Lit une réservation à partir de la base de données. Si aucune réservation n'est trouvée, null est retourné.
     *
     * @param connexion - La connexion à utiliser.
     * @param idReservation - L'ID de la réservation à lire.
     * @return La réservation lue ; null sinon.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    ReservationDTO get(Connexion connexion,
        String idReservation) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException;

    /**
     *
     * TODO Met à jour une réservation dans la base de données.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - La réservation à mettre à jour.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void update(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * Supprime une réservation de la base de données.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - La réservation à supprimer.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void delete(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * TODO Trouve toutes les réservations de la base de données. La liste est classée par ordre croissant sur sortByPropertyName. Si aucune réservation n'est trouvée, une List vide est retournée.
     *
     * @param connexion - La connexion à utiliser.
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer.
     * @return La liste de toutes les réservations ; une liste vide sinon.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    List<ReservationDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * TODO Trouve les réservations à partir d'un membre. La liste est classée par ordre croissant sur sortByPropertyName. Si aucune réservation n'est trouvée, une List vide est retournée.
     *
     * @param connexion - La connexion à utiliser.
     * @param idMembre - L'ID du membre à trouver.
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer.
     * @return La liste des réservations correspondantes ; une liste vide sinon.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidCriterionException - Si l'ID du membre est null.
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Trouve les réservations à partir d'un livre. La liste est classée par ordre croissant sur sortByPropertyName. Si aucune réservation n'est trouvée, une List vide est retournée..
     *
     * @param connexion - La connexion à utiliser.
     * @param idLivre - L'ID du livre à trouver.
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer.
     * @return La liste des réservations correspondantes ; une liste vide sinon.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidCriterionException - Si l'ID du livre est null.
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Place une réservation.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - La réservation à placer.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidPrimaryKeyException - Si la clef primaire du membre est null ou si la clef primaire du livre est null.
     * @throws MissingDTOException - Si le membre n'existe pas ou si le livre n'existe pas.
     * @throws InvalidCriterionException - Si l'ID du livre est null.
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null.
     * @throws MissingLoanException - Si le livre n'a pas encore été prêté.
     * @throws ExistingLoanException - Si le livre est déjà prêté au membre.
     * @throws ExistingReservationException - Si le membre a déjà réservé ce livre.
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void placer(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * Utilise une réservation.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - La réservation à utiliser.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null, si la clef primaire du membre est null ou si la clef primaire du livre est null.
     * @throws MissingDTOException - Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas.
     * @throws InvalidCriterionException - Si l'ID du livre est null.
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null.
     * @throws ExistingReservationException - Si la réservation n'est pas la première de la liste.
     * @throws ExistingLoanException - Si le livre est déjà prêté au membre.
     * @throws InvalidLoanLimitException - Si le membre a atteint sa limite de prêt.
     * @throws InvalidDTOClassException - Si la classe du membre n'est pas celle que prend en charge le DAO ou si la classe du n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void utiliser(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     *
     * Annule une réservation.
     *
     * @param connexion - La connexion à utiliser.
     * @param reservationDTO - Le reservation à annuler.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null.
     * @throws MissingDTOException - Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas.
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */
    void annuler(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        ServiceException;

}
