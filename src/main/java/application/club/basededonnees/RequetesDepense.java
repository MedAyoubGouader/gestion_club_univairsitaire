package application.club.basededonnees;

import java.lang.RuntimeException;
import application.club.outils.ConnexionOracle;
import application.club.donnees.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RequetesDepense {

    public void add(Depense depense) {
        String sql = "INSERT INTO Depense (montant, dateDepense, motif, idAdmin) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, depense.getMontant());
            statement.setDate(2, Date.valueOf(depense.getDateDepense()));
            statement.setString(3, depense.getMotif());

            if (depense.getIdAdmin() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, depense.getIdAdmin());
            }

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to add depense", exception);
        }
    }

    public List<Depense> findAll() {
        String sql = "SELECT idDepense, montant, dateDepense, motif, idAdmin FROM Depense ORDER BY dateDepense DESC";
        List<Depense> depenses = new ArrayList<>();

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Depense depense = new Depense();
                depense.setIdDepense(resultSet.getInt("idDepense"));
                depense.setMontant(resultSet.getBigDecimal("montant"));

                Date sqlDate = resultSet.getDate("dateDepense");
                depense.setDateDepense(sqlDate == null ? null : sqlDate.toLocalDate());

                depense.setMotif(resultSet.getString("motif"));

                int idAdmin = resultSet.getInt("idAdmin");
                depense.setIdAdmin(resultSet.wasNull() ? null : idAdmin);

                depenses.add(depense);
            }
            return depenses;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to fetch depenses", exception);
        }
    }

    public BigDecimal sumDepenses() {
        String sql = "SELECT COALESCE(SUM(montant), 0) AS total FROM Depense";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getBigDecimal("total");
            }
            return BigDecimal.ZERO;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to compute total depenses", exception);
        }
    }
}
