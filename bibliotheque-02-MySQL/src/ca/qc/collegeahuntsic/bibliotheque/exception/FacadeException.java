// Fichier FacadeException.java
// Auteur : Ary-Carson Bernier
// Date de création : 2016-09-12

package ca.qc.collegeahuntsic.bibliotheque.exception;

/**
 * Cette classe consiste a gérer les exceptions relié à la facade .
 *
 * @author Ary-Carson Bernier
 */
public class FacadeException extends Exception {
    /**.
     * TODO Auto-generated field javadoc
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cet méthode consiste à retourner le message d'erreur.
     *
     * @param message qui est le parametre dont on utilise afin de le retourner
     */
    public FacadeException(String message) {
        super(message);
    }
}
