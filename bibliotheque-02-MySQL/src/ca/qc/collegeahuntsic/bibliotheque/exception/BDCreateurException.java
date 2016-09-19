// Fichier BDCreateurException.java
// Auteur : Ary-Carson Bernier
// Date de création : 2016-09-12

package ca.qc.collegeahuntsic.bibliotheque.exception;

/**
 * Cette classe consiste a gérer les exceptions lors de la création de la base de donnée.
 *
 * @author Ary-Carson Bernier
 */
public class BDCreateurException extends Exception {
    /**.
     * TODO Auto-generated field javadoc
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cet méthode consiste à retourner le message d'erreur.
     *
     * @param message qui est le parametre dont on utilise afin de le retourne
     */
    public BDCreateurException(String message) {
        super(message);
    }

}
