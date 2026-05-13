package application.club;

import application.club.interfaces.FenetreEvenement;
import application.club.interfaces.FenetreMembre;
import application.club.interfaces.FenetreTableauDeBord;
import application.club.interfaces.FenetreMateriel;
import application.club.interfaces.FenetreTache;
import application.club.interfaces.FenetreFinance;
import application.club.interfaces.FenetreHistorique;
import application.club.interfaces.FenetreReunion;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ApplicationPrincipale extends Application {

    private BorderPane mainLayout;
    private FenetreTableauDeBord fenetreTableauDeBord;
    private FenetreMembre fenetreMembre;
    private FenetreEvenement fenetreEvenement;
    private FenetreMateriel fenetreMateriel;
    private FenetreTache fenetreTache;
    private FenetreFinance fenetreFinance;
    private FenetreHistorique fenetreHistorique;
    private FenetreReunion fenetreReunion;

    @Override
    public void start(Stage primaryStage) {
        mainLayout = new BorderPane();
        
        // Initialiser les vues
        fenetreTableauDeBord = new FenetreTableauDeBord();
        fenetreMembre = new FenetreMembre();
        fenetreEvenement = new FenetreEvenement();
        fenetreMateriel = new FenetreMateriel();
        fenetreTache = new FenetreTache();
        fenetreFinance = new FenetreFinance();
        fenetreHistorique = new FenetreHistorique();
        fenetreReunion = new FenetreReunion();

        // Côté Gauche : Menu de navigation
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(30, 20, 20, 20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(250);

        Label logoTitle = new Label("G-CLUB 🎓");
        logoTitle.setFont(Font.font("Arial", 28));
        logoTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnDashboard = createMenuButton("📊 Tableau de Bord");
        Button btnMembres = createMenuButton("👥 Gestion Membres");
        Button btnEvenements = createMenuButton("📅 Gestion Événements");
        Button btnMateriels = createMenuButton("💻 Gestion Matériels");
        Button btnTaches = createMenuButton("✅ Gestion Tâches");
        Button btnFinance = createMenuButton("💰 Gestion Finances");
        Button btnHistorique = createMenuButton("🕰️ Historique");
        Button btnReunion = createMenuButton("👥 Réunions");

        btnDashboard.setOnAction(e -> {
            fenetreTableauDeBord.loadData();
            mainLayout.setCenter(fenetreTableauDeBord.getView());
        });
        btnMembres.setOnAction(e -> {
            fenetreMembre.loadData();
            mainLayout.setCenter(fenetreMembre.getView());
        });
        btnEvenements.setOnAction(e -> {
            fenetreEvenement.loadData();
            mainLayout.setCenter(fenetreEvenement.getView());
        });
        btnMateriels.setOnAction(e -> {
            fenetreMateriel.loadData();
            mainLayout.setCenter(fenetreMateriel);
        });
        btnTaches.setOnAction(e -> {
            fenetreTache.loadData();
            mainLayout.setCenter(fenetreTache);
        });
        btnFinance.setOnAction(e -> {
            fenetreFinance.loadData();
            mainLayout.setCenter(fenetreFinance);
        });
        btnReunion.setOnAction(e -> {
            fenetreReunion.loadData();
            mainLayout.setCenter(fenetreReunion);
        });
        btnHistorique.setOnAction(e -> {
            fenetreHistorique.loadData();
            mainLayout.setCenter(fenetreHistorique);
        });

        sidebar.getChildren().addAll(logoTitle, new Label(""), btnDashboard, btnMembres, btnEvenements, btnMateriels, btnTaches, btnFinance, btnHistorique, btnReunion);
        
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(fenetreTableauDeBord.getView());

        Scene scene = new Scene(mainLayout, 1100, 700);
        primaryStage.setTitle("Système de Gestion de Club Universitaire - Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                     "-fx-font-size: 16px; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 15 10 15;");
        
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-alignment: BASELINE_LEFT; -fx-cursor: hand; -fx-padding: 10 15 10 15;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 15 10 15;"));
        
        return btn;
    }
}
