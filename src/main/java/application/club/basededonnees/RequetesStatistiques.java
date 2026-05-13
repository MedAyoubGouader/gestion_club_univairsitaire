package application.club.basededonnees;

import java.lang.RuntimeException;
import application.club.outils.ConnexionOracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class RequetesStatistiques {

    public Map<String, Object> consulterStatistiques() {
        Map<String, Object> stats = new HashMap<>();

        try (Connection conn = ConnexionOracle.getConnection();
             Statement stmt = conn.createStatement()) {

            // Nombre total des réunions
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Reunion")) {
                if (rs.next()) {
                    stats.put("totalReunions", rs.getInt(1));
                }
            }

            // Total des cotisations
            try (ResultSet rs = stmt.executeQuery("SELECT SUM(montant) FROM Cotisation")) {
                if (rs.next()) {
                    stats.put("totalCotisations", rs.getDouble(1));
                }
            }

            // Total des dépenses
            try (ResultSet rs = stmt.executeQuery("SELECT SUM(montant) FROM Depense")) {
                if (rs.next()) {
                    stats.put("totalDepenses", rs.getDouble(1));
                }
            }
            
            // Solde financier
            double totalCotisations = (Double) stats.getOrDefault("totalCotisations", 0.0);
            double totalDepenses = (Double) stats.getOrDefault("totalDepenses", 0.0);
            stats.put("soldeFinancier", totalCotisations - totalDepenses);

            // Total des membres
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Membre")) {
                if (rs.next()) {
                    stats.put("totalMembres", rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des statistiques", e);
        }

        return stats;
    }
}
