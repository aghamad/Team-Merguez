// Fichier IReservationService.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import org.hibernate.Session;

/**
 * Interface de service pour manipuler les réservations dans la base de données.
 *
 * @author team-merguez
 */
public interface IReservationService extends IService {

    /**
    *
    * TODO Ajoute une nouvelle réservation dans la base de données.
    *
    * @param session - La session Hibernate à utiliser.
    * @param reservationDTO - La réservation à ajouter.
    * @throws InvalidHibernateSessionException - Si la connexion est null.
    * @throws InvalidDTOException - Si la réservation est null.
    * @throws MissingLoanException - Si le livre n'a pas encore été prêté.
    * @throws ExistingLoanException - Si le livre est déjà prêté au membre.
    * @throws ExistingReservationException - Si le membre a déjà réservé ce livre.
    * @throws ServiceException -  S'il y a une erreur avec la base de données.
    */

    void placer(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException;

    /**
     *
     * Utilise une réservation.
     *
     * @param session - La session Hibernate à utiliser.
     * @param reservationDTO - La réservation à utiliser.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws ExistingReservationException - Si la réservation n'est pas la première de la liste.
     * @throws ExistingLoanException - Si le livre est déjà prêté au membre.
     * @throws InvalidLoanLimitException - Si le membre a atteint sa limite de prêt.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */

    void utiliser(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ServiceException;

    /**
     *
     *
     * Annule une réservation.
     *
     * @param session - La session hibernate à utiliser.
     * @param reservationDTO - Le reservation à annuler.
     * @throws InvalidHibernateSessionException - Si la connexion est null.
     * @throws InvalidDTOException - Si la réservation est null.
     * @throws ServiceException - S'il y a une erreur avec la base de données.
     */
    void annuler(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException;

}
