import java.sql.SQLException;

/**
 * Gestion des transactions de reli�es � la cr�ation et
 * suppresion de livres dans une biblioth�que.
 *
 * Ce programme permet de g�rer les transaction reli�es � la 
 * cr�ation et suppresion de livres.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * </pre>
 */
public class GestionLivre {

private Livre livre;
private Reservation reservation;
private Connexion cx;

/**
  * Creation d'une instance
  */
public GestionLivre(Livre livre, Reservation reservation)
{
this.cx = livre.getConnexion();
this.livre = livre;
this.reservation = reservation;
}

/**
  * Ajout d'un nouveau livre dans la base de donn�es.
  * S'il existe deja, une exception est lev�e.
  */
public void acquerir(int idLivre, String titre, String auteur, String dateAcquisition)
  throws SQLException, BibliothequeException, Exception
{
try {
    /* V�rifie si le livre existe d�ja */
    if (livre.existe(idLivre))
        throw new BibliothequeException("Livre existe deja: " + idLivre);

    /* Ajout du livre dans la table des livres */
    livre.acquerir(idLivre, titre, auteur, dateAcquisition);
    cx.commit();
    }
catch (Exception e)
    {
//        System.out.println(e);
    cx.rollback();
    throw e;
    }
}

/**
  * Vente d'un livre.
  */
public void vendre(int idLivre)
  throws SQLException, BibliothequeException, Exception
{
try {
    TupleLivre tupleLivre = livre.getLivre(idLivre);
    if (tupleLivre == null)
        throw new BibliothequeException("Livre inexistant: " + idLivre);
    if (tupleLivre.idMembre != 0)
        throw new BibliothequeException
            ("Livre " + idLivre + " prete a " + tupleLivre.idMembre);
    if (reservation.getReservationLivre(idLivre) !=null)
        throw new BibliothequeException
        ("Livre " + idLivre + " r�serv� ");
    
    /* Suppression du livre. */
    int nb = livre.vendre(idLivre);
    if (nb == 0)
        throw new BibliothequeException
        ("Livre " + idLivre + " inexistant");
    cx.commit();
    }
catch (Exception e)
    {
    cx.rollback();
    throw e;
    }
}
}
