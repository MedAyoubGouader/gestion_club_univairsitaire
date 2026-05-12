package application.club.outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionOracle {

    // Paramètres de connexion selon ce que nous avons configuré dans Oracle
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "gestionclub";
    private static final String PASSWORD = "gestion123";
    
    private static Connection connection;

    /**
     * Méthode pour obtenir la connexion à la base de données (Singleton)
     */
    public static void mettreAjourBaseDeDonnees() {
        // Tente d'ajouter la colonne statut physique dans la base de données existante.
        String sqlAlign = "ALTER TABLE Membre ADD statut VARCHAR2(50) DEFAULT 'Inactif'";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlAlign);
            System.out.println("Colonne 'statut' ajoutée à la base de données avec succès !");
        } catch (SQLException e) {
            // L'erreur ORA-01430 signifie que la colonne existe déjà, ce qui est très bien. On ignore l'erreur.
            if (!e.getMessage().contains("ORA-01430")) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        try {
            // Si la connexion n'existe pas ou qu'elle a été fermée par une requête précédente, on la recrée
            if (connection == null || connection.isClosed()) {
                // Charger le driver Oracle
                Class.forName("oracle.jdbc.driver.OracleDriver");
                
                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC introuvable. Vérifiez vos dépendances Maven.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Méthode pour fermer la connexion proprement
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

