package application.club;

import application.club.interfaces.FenetreEvenement;
import application.club.interfaces.FenetreMembre;
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
    private FenetreMembre fenetreMembre;
    private FenetreEvenement fenetreEvenement;

    @Override
    public void start(Stage primaryStage) {
        mainLayout = new BorderPane();
        
        // Initialiser les deux vues
        fenetreMembre = new FenetreMembre();
        fenetreEvenement = new FenetreEvenement();

        // 1. Côté Gauche : Menu de navigation
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(30, 20, 20, 20));
        sidebar.setStyle("-fx-background-color: #2c3e50;"); // Bleu sombre
        sidebar.setPrefWidth(250);

        Label logoTitle = new Label("G-CLUB 🎓");
        logoTitle.setFont(Font.font("Arial", 28));
        logoTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnMembres = createMenuButton("👥 Gestion Membres");
        Button btnEvenements = createMenuButton("📅 Gestion Événements");

        // Action des boutons : changer la vue au centre ET recharger les données depuis la base
        btnMembres.setOnAction(e -> {
            fenetreMembre.loadData();
            mainLayout.setCenter(fenetreMembre.getView());
        });
        
        btnEvenements.setOnAction(e -> {
            fenetreEvenement.loadData();
            mainLayout.setCenter(fenetreEvenement.getView());
        });

        sidebar.getChildren().addAll(logoTitle, new Label(""), btnMembres, btnEvenements);
        
        // Placer le menu à gauche
        mainLayout.setLeft(sidebar);

        // Par défaut, quand on ouvre l'appli, on affiche les membres
        mainLayout.setCenter(fenetreMembre.getView());

        // Afficher la fenêtre (un poil plus grande pour avoir de la place)
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
        
        // Effet de survol
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-alignment: BASELINE_LEFT; -fx-cursor: hand; -fx-padding: 10 15 10 15;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 15 10 15;"));
        
        return btn;
    }
}

