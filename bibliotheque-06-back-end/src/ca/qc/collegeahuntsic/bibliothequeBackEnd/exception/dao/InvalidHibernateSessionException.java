// Fichier InvalidHibernateSessionException.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao;

/**
 * Cette exception est utilisée en cas de problème avec un objet {@link ca.qc.collegeahuntsic.bibliothequeBackEnd.db.Connexion}.
 *
 * @author Gilles Bénichou
 */
public class InvalidHibernateSessionException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with null as its detail message. The cause is not initialized, and may subsequently be initialized by a call
     * to {@link java.lang.Throwable#initCause(java.lang.Throwable) Throwable.initCause(Throwable)}.
     */
    public InvalidHibernateSessionException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be initialized by a call
     * to {@link java.lang.Throwable#initCause(java.lang.Throwable) Throwable.initCause(Throwable)}.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the
     *        {@link java.lang.Throwable#getMessage() Throwable.getMessage()} method
     */
    public InvalidHibernateSessionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (<code>cause == null ? null : cause.toString()</code>) (which
     * typically contains the class and detail message of cause). This constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example, {@link java.security.PrivilegedActionException PrivilegedActionException}).
     *
     * @param cause The cause (which is saved for later retrieval by the {@link java.lang.Throwable#getCause() Throwable.getCause()} method).
     *        A null value is permitted, and indicates that the cause is nonexistent or unknown
     */
    public InvalidHibernateSessionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause. Note that the detail message associated with cause is not
     * automatically incorporated in this exception's detail message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method
     * @param cause The cause (which is saved for later retrieval by the {@link java.lang.Throwable#getCause() Throwable.getCause()} method).
     *        A null value is permitted, and indicates that the cause is nonexistent or unknown
     */
    public InvalidHibernateSessionException(String message,
        Throwable cause) {
        super(message,
            cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or
     * disabled.
     *
     * @param message The detail message
     * @param cause The cause. A null value is permitted, and indicates that the cause is nonexistent or unknown
     * @param enableSuppression Whether or not suppression is enabled or disabled
     * @param writableStackTrace Whether or not the stack trace should be writable
     */
    public InvalidHibernateSessionException(String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message,
            cause,
            enableSuppression,
            writableStackTrace);
    }
}
