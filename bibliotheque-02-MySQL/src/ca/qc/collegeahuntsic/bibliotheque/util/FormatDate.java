// Fichier FormatDate.java
// Auteur : Team-Merguez
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilitaire de création d'un {@link java.sql.Timestamp} dans un format défini.
 *
 * @author Gilles Benichou
 */
public final class FormatDate {
    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(FormatDate.FORMAT_DATE);

    static {
        FormatDate.SIMPLE_DATE_FORMAT.setLenient(false);
    }

    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private FormatDate() {
        super();
    }

    /**
     * Convertit une chaîne de caractères en {@link java.sql.Timestamp} selon le format <code>yyyy-MM-dd</code>.
     *
     * @param date La chaîne de caractères
     * @return Le {@link java.sql.Timestamp} issu de la conversion
     * @throws ParseException Si la chaîne de caractères n'est pas formatée correctement
     */
    public static Timestamp timestampValue(String date) throws ParseException {
        final Date dateFormatee = FormatDate.SIMPLE_DATE_FORMAT.parse(date);
        final Timestamp timestamp = new Timestamp(dateFormatee.getTime());
        return timestamp;
    }

    /**
     * Convertit un {@link java.sql.Timestamp} en une chaîne de caractères selon le format <code>yyyy-MM-dd</code>.
     *
     * @param timestamp Le {@link java.sql.Timestamp}
     * @return La chaîne de caractères issue de la conversion
     * @throws ParseException Si le {@link java.sql.Timestamp} n'est pas formaté correctement
     */
    public static String stringValue(Timestamp timestamp) {
        final Date date = new Date(timestamp.getTime());
        final String dateFormatee = FormatDate.SIMPLE_DATE_FORMAT.format(date);
        return dateFormatee;
    }
}
