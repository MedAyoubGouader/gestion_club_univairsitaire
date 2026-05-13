package application.club.basededonnees;

import java.lang.RuntimeException;
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
import java.util.Optional;

public class RequetesReunion {

    public void add(Reunion reunion) {
        String sql = "INSERT INTO Reunion (dateReunion, heureReunion, sujet, duree) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(reunion.getDateReunion()));
            statement.setString(2, reunion.getHeureReunion());
            statement.setString(3, reunion.getSujet());

            if (reunion.getDuree() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, reunion.getDuree());
            }

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to add reunion", exception);
        }
    }

    public Optional<Reunion> findById(int idReunion) {
        String sql = "SELECT idReunion, dateReunion, heureReunion, sujet, duree FROM Reunion WHERE idReunion = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idReunion);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to fetch reunion by id", exception);
        }
    }

    public List<Reunion> findAll() {
        String sql = "SELECT idReunion, dateReunion, heureReunion, sujet, duree FROM Reunion ORDER BY dateReunion DESC, heureReunion DESC";
        List<Reunion> reunions = new ArrayList<>();

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reunions.add(mapRow(resultSet));
            }

            return reunions;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to fetch reunions", exception);
        }
    }

    public void update(Reunion reunion) {
        String sql = "UPDATE Reunion SET dateReunion = ?, heureReunion = ?, sujet = ?, duree = ? WHERE idReunion = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(reunion.getDateReunion()));
            statement.setString(2, reunion.getHeureReunion());
            statement.setString(3, reunion.getSujet());

            if (reunion.getDuree() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, reunion.getDuree());
            }

            statement.setInt(5, reunion.getIdReunion());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update reunion", exception);
        }
    }

    public void deleteById(int idReunion) {
        String sql = "DELETE FROM Reunion WHERE idReunion = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idReunion);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete reunion", exception);
        }
    }

    private Reunion mapRow(ResultSet resultSet) throws SQLException {
        Date sqlDate = resultSet.getDate("dateReunion");

        Reunion reunion = new Reunion();
        reunion.setIdReunion(resultSet.getInt("idReunion"));
        reunion.setDateReunion(sqlDate == null ? null : sqlDate.toLocalDate());
        reunion.setHeureReunion(resultSet.getString("heureReunion"));
        reunion.setSujet(resultSet.getString("sujet"));

        int duree = resultSet.getInt("duree");
        reunion.setDuree(resultSet.wasNull() ? null : duree);

        return reunion;
    }
}
