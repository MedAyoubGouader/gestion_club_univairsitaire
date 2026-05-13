package application.club.basededonnees;

import application.club.outils.ConnexionOracle;
import application.club.donnees.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequetesMateriel {
    public List<Materiel> findAll() throws SQLException {
        String sql = "SELECT idMateriel, nom, quantite, etat FROM Materiel ORDER BY idMateriel";
        List<Materiel> materiels = new ArrayList<>();

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                materiels.add(new Materiel(
                        resultSet.getInt("idMateriel"),
                        resultSet.getString("nom"),
                        resultSet.getInt("quantite"),
                        resultSet.getString("etat")
                ));
            }
        }

        return materiels;
    }

    public void create(Materiel materiel) throws SQLException {
        String sql = "INSERT INTO Materiel (nom, quantite, etat) VALUES (?, ?, ?)";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, materiel.getNom());
            statement.setInt(2, materiel.getQuantite());
            statement.setString(3, materiel.getEtat());
            statement.executeUpdate();
            new RequetesHistoriqueAction().enregistrerAction("Ajout d'un materiel : " + materiel.getNom(), 1);
        }
    }

    public void update(Materiel materiel) throws SQLException {
        String sql = "UPDATE Materiel SET nom = ?, quantite = ?, etat = ? WHERE idMateriel = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, materiel.getNom());
            statement.setInt(2, materiel.getQuantite());
            statement.setString(3, materiel.getEtat());
            statement.setInt(4, materiel.getIdMateriel());
            statement.executeUpdate();
            new RequetesHistoriqueAction().enregistrerAction("Modification d'un materiel ID: " + materiel.getIdMateriel(), 1);
        }
    }

    public void deleteById(int idMateriel) throws SQLException {
        String sql = "DELETE FROM Materiel WHERE idMateriel = ?";

        try (Connection connection = ConnexionOracle.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idMateriel);
            new RequetesHistoriqueAction().enregistrerAction("Suppression d'un materiel ID: " + idMateriel, 1);
            statement.executeUpdate();
        }
    }
}
