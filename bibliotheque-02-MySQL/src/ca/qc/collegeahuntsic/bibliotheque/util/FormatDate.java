// Fichier FormatDate.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilitaire de création d'un Timestamp dans un format défini.
 *
 * @author Team-Merguez
 */

public final class FormatDate {

    /**
     * variable private static.
     *
     */
    private static SimpleDateFormat formatAMJ;
    static {
        formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
        formatAMJ.setLenient(false);
    }

    /**
     *
     * Constructeur privé pour empêcher toute instanciation.
     *
     */
    private FormatDate() {
        super();
    }

    /**
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     * @param dateString nom de la String passer en parametre
     * @throws ParseException est l'exception retourner
     * @return formatAMJ retourne la date qui est parse.
     */
    public static Date convertirDate(String dateString) throws ParseException {
        return formatAMJ.parse(dateString);
    }

    /**
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     * @param date est la date passe en paramettre.
     * @return formatAMJ retourne la date qui est parse.
     */
    public static String toString(Date date) {
        return formatAMJ.format(date);
    }
}
