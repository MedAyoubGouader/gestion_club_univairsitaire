package application.club.basededonnees;

import application.club.donnees.Evenement;
import application.club.outils.ConnexionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequetesEvenement {

    // Ajouter un événement
    public boolean ajouterEvenement(Evenement evenement) {
        String sql = "INSERT INTO Evenement (titre, dateEvenement, lieu, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, evenement.getTitre());
            pstmt.setDate(2, evenement.getDateEvenement());
            pstmt.setString(3, evenement.getLieu());
            pstmt.setString(4, evenement.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Modifier un événement
    public boolean modifierEvenement(Evenement evenement) {
        String sql = "UPDATE Evenement SET titre = ?, dateEvenement = ?, lieu = ?, description = ? WHERE idEvenement = ?";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, evenement.getTitre());
            pstmt.setDate(2, evenement.getDateEvenement());
            pstmt.setString(3, evenement.getLieu());
            pstmt.setString(4, evenement.getDescription());
            pstmt.setInt(5, evenement.getIdEvenement());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un événement
    public boolean supprimerEvenement(int idEvenement) {
        String sql = "DELETE FROM Evenement WHERE idEvenement = ?";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEvenement);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Consulter les événements enregistrés
    public List<Evenement> obtenirTousLesEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM Evenement";
        
        try (Connection conn = ConnexionOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Evenement ev = new Evenement(
                    rs.getInt("idEvenement"),
                    rs.getString("titre"),
                    rs.getDate("dateEvenement"),
                    rs.getString("lieu"),
                    rs.getString("description")
                );
                evenements.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }
}

