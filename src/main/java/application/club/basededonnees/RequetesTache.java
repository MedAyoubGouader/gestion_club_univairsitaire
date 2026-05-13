package application.club.basededonnees;

import application.club.outils.ConnexionOracle;
import application.club.donnees.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RequetesTache {
    public List<Tache> findAll() throws SQLException {
        String sql = "SELECT idTache, description, etat, dateLimite, idMembre FROM Tache ORDER BY idTache";
        List<Tache> taches = new ArrayList<>();

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Date sqlDate = resultSet.getDate("dateLimite");
                int idMembreVal = resultSet.getInt("idMembre");
                Integer idMembre = resultSet.wasNull() ? null : idMembreVal;
                
                taches.add(new Tache(
                        resultSet.getInt("idTache"),
                        resultSet.getString("description"),
                        resultSet.getString("etat"),
                        sqlDate != null ? sqlDate.toLocalDate() : null,
                        idMembre
                ));
            }
        }

        return taches;
    }

    public void create(Tache tache) throws SQLException {
        String sql = "INSERT INTO Tache (description, etat, dateLimite, idMembre) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tache.getDescription());
            statement.setString(2, tache.getEtat());
            setDate(statement, 3, tache.getDateLimite());
            setNullableInt(statement, 4, tache.getIdMembre());
            statement.executeUpdate();
        }
    }

    public void update(Tache tache) throws SQLException {
        String sql = "UPDATE Tache SET description = ?, etat = ?, dateLimite = ?, idMembre = ? WHERE idTache = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tache.getDescription());
            statement.setString(2, tache.getEtat());
            setDate(statement, 3, tache.getDateLimite());
            setNullableInt(statement, 4, tache.getIdMembre());
            statement.setInt(5, tache.getIdTache());
            statement.executeUpdate();
        }
    }

    public void deleteById(int idTache) throws SQLException {
        String sql = "DELETE FROM Tache WHERE idTache = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idTache);
            statement.executeUpdate();
        }
    }

    private void setDate(PreparedStatement statement, int index, java.time.LocalDate localDate) throws SQLException {
        if (localDate == null) {
            statement.setNull(index, Types.DATE);
            return;
        }
        statement.setDate(index, Date.valueOf(localDate));
    }

    private void setNullableInt(PreparedStatement statement, int index, Integer value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
            return;
        }
        statement.setInt(index, value);
    }
}
