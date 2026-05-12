package application.club.basededonnees;

import application.club.donnees.Membre;
import application.club.outils.ConnexionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequetesParticipation {

    // Ajouter un membre à un événement
    // ET mettre à jour le statut du membre vers "Actif" automatiquement.
    public boolean ajouterParticipation(int idMembre, int idEvenement, String role) {
        String insertSql = "INSERT INTO ParticipationEvenement (idMembre, idEvenement, dateParticipation, statutPresence, role) VALUES (?, ?, CURRENT_DATE, 'Présent', ?)";
        String updateMembreSql = "UPDATE Membre SET statut = 'Actif' WHERE idMembre = ?";
        
        try (Connection conn = ConnexionOracle.getConnection()) {
            // Désactiver l'auto-commit pour sécuriser (Transaction)
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertSql);
                 PreparedStatement pstmt2 = conn.prepareStatement(updateMembreSql)) {
                
                // 1. Ajouter la participation
                pstmt1.setInt(1, idMembre);
                pstmt1.setInt(2, idEvenement);
                pstmt1.setString(3, role);
                pstmt1.executeUpdate();
                
                // 2. Mettre à jour le membre pour qu'il devienne "Actif" en "dur" dans la table
                pstmt2.setInt(1, idMembre);
                pstmt2.executeUpdate();
                
                // Valider !
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupérer les membres qui assistent à l'événement spécifique   
    public List<Membre> obtenirParticipants(int idEvenement) {
        List<Membre> participants = new ArrayList<>();
        String sql = "SELECT m.* FROM Membre m JOIN ParticipationEvenement p ON m.idMembre = p.idMembre WHERE p.idEvenement = ?";
        
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, idEvenement);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Membre m = new Membre(rs.getInt("idMembre"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("poste"));
                participants.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }
}

