// Fichier ServiceException.java
// Auteur : Ary-Carson Bernier
// Date de création : 2016-09-12

package ca.qc.collegeahuntsic.bibliotheque.exception;

public class ServiceException extends Exception {
    /**
     * TODO Auto-generated field javadoc
     */
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }
}
