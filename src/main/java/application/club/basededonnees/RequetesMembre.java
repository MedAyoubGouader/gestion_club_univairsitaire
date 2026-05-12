package application.club.basededonnees;

import application.club.donnees.Membre;
import application.club.outils.ConnexionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequetesMembre {

    public boolean existeMembre(String nom, String prenom, int idAExclure) {
        String sql = "SELECT COUNT(*) FROM Membre WHERE LOWER(nom) = LOWER(?) AND LOWER(prenom) = LOWER(?) AND idMembre != ?";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setInt(3, idAExclure);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean ajouterMembre(Membre membre) {
        String sql = "INSERT INTO Membre (nom, prenom, email, telephone, poste, statut) VALUES (?, ?, ?, ?, ?, 'Inactif')";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setString(4, membre.getTelephone());
            pstmt.setString(5, membre.getPoste());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierMembre(Membre membre) {
        String sql = "UPDATE Membre SET nom = ?, prenom = ?, email = ?, telephone = ?, poste = ? WHERE idMembre = ?";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setString(4, membre.getTelephone());
            pstmt.setString(5, membre.getPoste());
            pstmt.setInt(6, membre.getIdMembre());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerMembre(int idMembre) {
        String sql = "DELETE FROM Membre WHERE idMembre = ?";
        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idMembre);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Membre> obtenirTousLesMembres() {
        List<Membre> membres = new ArrayList<>();
        // On récupère directement le statut physique qui est MAINTENANT dans la table
        String sql = "SELECT m.idMembre, m.nom, m.prenom, m.email, m.telephone, m.poste, m.statut, " +
                     "(SELECT COUNT(*) FROM ParticipationEvenement pe WHERE pe.idMembre = m.idMembre) as nbEvents " +
                     "FROM Membre m ORDER BY m.idMembre DESC";
        
        try (Connection conn = ConnexionOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membre membre = new Membre(
                    rs.getInt("idMembre"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("poste")
                );
                membre.setNbEvenements(rs.getInt("nbEvents"));
                membre.setStatutActif(rs.getString("statut"));
                membres.add(membre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membres;
    }

    /**
     * Méthode très pratique pour retrouver un membre soit par son ID (int), soit par "Nom Prenom"
     */
    public Membre rechercherMembreIdOuNom(String recherche) {
        recherche = recherche.trim();
        String sql = "SELECT * FROM Membre WHERE ";
        boolean estNumero = false;
        try {
            Integer.parseInt(recherche);
            estNumero = true;
        } catch (NumberFormatException ignored) {}

        if (estNumero) {
            sql += "idMembre = ?";
        } else {
            sql += "UPPER(nom) || ' ' || UPPER(prenom) LIKE UPPER(?) OR UPPER(prenom) || ' ' || UPPER(nom) LIKE UPPER(?)";
        }

        try (Connection conn = ConnexionOracle.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (estNumero) {
                pstmt.setInt(1, Integer.parseInt(recherche));
            } else {
                pstmt.setString(1, "%" + recherche + "%");
                pstmt.setString(2, "%" + recherche + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Membre(rs.getInt("idMembre"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("poste"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Membre introuvable
    }
}

