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

public class RequetesCotisation {

    public boolean memberExists(int idMembre) {
        String sql = "SELECT 1 FROM Membre WHERE idMembre = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idMembre);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to verify member", exception);
        }
    }

    public void add(Cotisation cotisation) {
        String sql = "INSERT INTO Cotisation (montant, datePaiement, idMembre) VALUES (?, ?, ?)";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, cotisation.getMontant());
            statement.setDate(2, Date.valueOf(cotisation.getDatePaiement()));

            if (cotisation.getIdMembre() == null) {
                statement.setNull(3, Types.INTEGER);
            } else {
                statement.setInt(3, cotisation.getIdMembre());
            }

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to add cotisation", exception);
        }
    }

    public List<Cotisation> findAll() {
        String sql = "SELECT idCotisation, montant, datePaiement, idMembre FROM Cotisation ORDER BY datePaiement DESC";
        List<Cotisation> cotisations = new ArrayList<>();

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Cotisation cotisation = new Cotisation();
                cotisation.setIdCotisation(resultSet.getInt("idCotisation"));
                cotisation.setMontant(resultSet.getBigDecimal("montant"));

                Date sqlDate = resultSet.getDate("datePaiement");
                cotisation.setDatePaiement(sqlDate == null ? null : sqlDate.toLocalDate());

                int idMembre = resultSet.getInt("idMembre");
                cotisation.setIdMembre(resultSet.wasNull() ? null : idMembre);

                cotisations.add(cotisation);
            }
            return cotisations;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to fetch cotisations", exception);
        }
    }

    public BigDecimal sumCotisations() {
        String sql = "SELECT COALESCE(SUM(montant), 0) AS total FROM Cotisation";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getBigDecimal("total");
            }
            return BigDecimal.ZERO;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to compute total cotisations", exception);
        }
    }
}
