package application.club.basededonnees;

import java.lang.RuntimeException;
import application.club.outils.ConnexionOracle;
import application.club.donnees.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequetesHistoriqueAction {

    public void enregistrerAction(String action, Integer idAdmin) {
        String sql = "INSERT INTO HistoriqueAction (action, idAdmin) VALUES (?, ?)";

        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, action);
            if (idAdmin != null) {
                pstmt.setInt(2, idAdmin);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'action dans l'historique", e);
        }
    }

    public List<HistoriqueAction> consulterHistorique() {
        List<HistoriqueAction> historique = new ArrayList<>();
        String sql = "SELECT idAction, action, dateHeure, idAdmin FROM HistoriqueAction ORDER BY dateHeure DESC";

        try (Connection conn = ConnexionOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistoriqueAction h = new HistoriqueAction();
                h.setIdAction(rs.getInt("idAction"));
                h.setAction(rs.getString("action"));
                h.setDateHeure(rs.getTimestamp("dateHeure"));
                int idAdmin = rs.getInt("idAdmin");
                if (!rs.wasNull()) {
                    h.setIdAdmin(idAdmin);
                }
                historique.add(h);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recupération de l'historique", e);
        }

        return historique;
    }
}
