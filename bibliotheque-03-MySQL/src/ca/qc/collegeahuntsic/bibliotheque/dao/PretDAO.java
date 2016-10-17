// Fichier package-info.java
// Auteur : Team-Merguez
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Team-Merguez
 */
public class PretDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO pret (idPret, "
        + "                                                       idMembre, "
        + "                                                       idLivre, "
        + "                                                       datePret, "
        + "                                                       dateRetour) "
        + "                                    VALUES            (?, "
        + "                                                       ?, "
        + "                                                       ?, "
        + "                                                       ?, "
        + "                                                       ?)";

    private static final String READ_REQUEST = "SELECT idPret, "
        + "                                            idMembre, "
        + "                                            idLivre, "
        + "                                            datePret, "
        + "                                            dateRetour "
        + "                                     FROM   pret "
        + "                                     WHERE  idPret = ?";

    private static final String UPDATE_REQUEST = "UPDATE pret "
        + "                                       SET    idPret = ?, "
        + "                                              idMembre = ?, "
        + "                                              idLivre = ?, "
        + "                                              datePret = ?, "
        + "                                              dateRetour = ? "
        + "                                       WHERE  idPret = ?";

    private static final String DELETE_REQUEST = "DELETE FROM pret "
        + "                                       WHERE       idPret = ?";

    private static final String GET_ALL_REQUEST = "SELECT idPret, "
        + "                                               idMembre, "
        + "                                               idLivre, "
        + "                                               datePret, "
        + "                                               dateRetour "
        + "                                        FROM   pret";

    private static final String FIND_BY_LIVRE = "SELECT idPret, "
        + "                                             idMembre, "
        + "                                             idLivre, "
        + "                                             datePret, "
        + "                                             dateRetour "
        + "                                      FROM   pret "
        + "                                      WHERE  idLivre = ?";

    private static final String FIND_BY_MEMBRE = "SELECT idPret, "
        + "                                              idMembre, "
        + "                                              idLivre, "
        + "                                              datePret, "
        + "                                              dateRetour "
        + "                                       FROM   pret "
        + "                                       WHERE  idMembre = ?";

    private static final String FIND_BY_DATE_PRET = "SELECT idPret, "
        + "                                                 idMembre, "
        + "                                                 idLivre, "
        + "                                                 datePret, "
        + "                                                 dateRetour "
        + "                                         FROM    pret "
        + "                                         WHERE   datePret = ?";

    private static final String FIND_BY_DATE_RETOUR = "SELECT idPret, "
        + "                                                 idMembre, "
        + "                                                 idLivre, "
        + "                                                 datePret, "
        + "                                                 dateRetour "
        + "                                             FROM   pret "
        + "                                             WHERE  dateRetour = ?";

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion qui crée la connection.
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Methode Add qui ajoute un pret.
     *
     * @param pretDTO nom de la variable d'instance de la classe PretDTO
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                pretDTO.getIdPret());
            addPreparedStatement.setInt(2,
                pretDTO.getIdMembre());
            addPreparedStatement.setInt(3,
                pretDTO.getIdLivre());
            addPreparedStatement.setTimestamp(4,
                pretDTO.getDatePret());
            addPreparedStatement.setTimestamp(5,
                pretDTO.getDateRetour());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Methode read qui recoit un int en parametre.
     *
     * @param idePret int recu en parametre pour
     * @return la methode
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public PretDTO read(int idePret) throws DAOException {
        PretDTO pretDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(PretDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idePret);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    pretDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return pretDTO;
    }

    /**
     *
     * Methode delete qui delete un pret.
     *
     * @param pretDTO nom de la variable d'instance de la classe PretDTO
     * @throws DAOException S'il y a une erreur avec la base de donnéesa
     */
    public void delete(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(PretDAO.DELETE_REQUEST)) {

            deletePreparedStatement.setInt(1,
                pretDTO.getIdPret());

            deletePreparedStatement.executeUpdate();
        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Methode update qui update un pret.
     *
     * @param pretDTO nom de la variable d'instance de la classe PretDTO
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            addPreparedStatement.setInt(1,
                pretDTO.getIdPret());
            addPreparedStatement.setInt(2,
                pretDTO.getIdMembre());
            addPreparedStatement.setInt(3,
                pretDTO.getIdLivre());
            addPreparedStatement.setTimestamp(4,
                pretDTO.getDatePret());
            addPreparedStatement.setTimestamp(5,
                pretDTO.getDateRetour());
            addPreparedStatement.setInt(6,
                pretDTO.getIdPret());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Methode List<PretDTO> getAll() qui liste tout.
     *
     * @return retourne tout les resultats des requestes
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> getAll() throws DAOException {
        List<PretDTO> listeDTO = Collections.emptyList();
        PretDTO pretDTO = null;

        try(
            PreparedStatement getAllPreparedStatement = getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST);
            ResultSet resultSet = getAllPreparedStatement.executeQuery();) {

            if(resultSet.next()) {
                listeDTO = new ArrayList<>();
                do {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    pretDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                    listeDTO.add(pretDTO);
                } while(resultSet.next());
            }

        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return listeDTO;
    }

    /**
     *
     * Methode List<PretDTO> findByLivre qui liste les livres.
     *
     * @param idLivre Nom de la variable qui correspond a l'id d'un livre
     * @return la liste des livres
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByLivre(int idLivre) throws DAOException {
        List<PretDTO> listePrets = Collections.emptyList();
        PretDTO pretDTO = null;

        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE);) {
            findByLivrePreparedStatement.setInt(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery();) {
                if(resultSet.next()) {
                    listePrets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        listePrets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return listePrets;
    }

    /**
     *
     * Methode List<PretDTO> findByMembre qui liste les membres.
     *
     * @param idMembre Nom de la variable qui correspond a l'id d'un membre
     * @return la list de membre
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByMembre(int idMembre) throws DAOException {
        List<PretDTO> listePrets = Collections.emptyList();
        PretDTO pretDTO = null;

        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE);) {
            findByLivrePreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery();) {
                if(resultSet.next()) {
                    listePrets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        listePrets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return listePrets;
    }

    /**
     *
     * Methode List<PretDTO> findByDateRetour qui liste par la date de pret.
     *
     * @param dateRetour Nom de la variable de l'instance Timestamp
     * @return return la liste de pret par la date de retour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDateRetour(Timestamp dateRetour) throws DAOException {
        List<PretDTO> listePrets = Collections.emptyList();
        PretDTO pretDTO = null;

        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR);) {
            findByLivrePreparedStatement.setTimestamp(1,
                dateRetour);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery();) {
                if(resultSet.next()) {
                    listePrets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        listePrets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return listePrets;
    }

    /**
     *
     * Methode List<PretDTO> findByDatePret qui liste par la date de pret.
     *
     * @param datePret Nom de la variable de l'instance Timestamp
     * @return la liste de pret
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDatePret(Timestamp datePret) throws DAOException {
        List<PretDTO> listePrets = Collections.emptyList();
        PretDTO pretDTO = null;

        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET);) {
            findByLivrePreparedStatement.setTimestamp(1,
                datePret);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery();) {
                if(resultSet.next()) {
                    listePrets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        listePrets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(final SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return listePrets;
    }

}
