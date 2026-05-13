package application.club.interfaces;

import application.club.basededonnees.RequetesStatistiques;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class FenetreTableauDeBord {
    private BorderPane view;
    private RequetesStatistiques requetesStatistiques;

    public FenetreTableauDeBord() {
        requetesStatistiques = new RequetesStatistiques();
        view = new BorderPane();
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #f4f6f9;");
        loadData();
    }

    public void loadData() {
        Map<String, Object> stats = requetesStatistiques.consulterStatistiques();

        int nbMembres = (Integer) stats.getOrDefault("totalMembres", 0);
        int nbReunions = (Integer) stats.getOrDefault("totalReunions", 0);
        double totalBudget = (Double) stats.getOrDefault("soldeFinancier", 0.0);
        double totalDepenses = (Double) stats.getOrDefault("totalDepenses", 0.0);
        double totalCotisations = (Double) stats.getOrDefault("totalCotisations", 0.0);

        Label lblTitle = new Label("Tableau de Bord 📊");
        lblTitle.setFont(Font.font("Arial", 32));
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox statsBox = new HBox(25);
        statsBox.setAlignment(Pos.CENTER);
        
        statsBox.getChildren().addAll(
            createStatCard("Total Membres", String.valueOf(nbMembres), "#3498db"),
            createStatCard("Total Réunions", String.valueOf(nbReunions), "#2ecc71"),
            createStatCard("Solde Financier", String.format("%.2f DT", totalBudget), "#f39c12")
        );

        // Graphiques
        HBox chartsBox = new HBox(40);
        chartsBox.setAlignment(Pos.CENTER);
        chartsBox.setPadding(new Insets(30, 0, 0, 0));

        // PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Cotisations (" + String.format("%.2f DT", totalCotisations) + ")", totalCotisations),
            new PieChart.Data("Dépenses (" + String.format("%.2f DT", totalDepenses) + ")", totalDepenses)
        );
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Répartition Financière");
        pieChart.setPrefSize(400, 300);

        // BarChart pour Membres
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Vue Globale");
        barChart.setPrefSize(400, 300);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statistiques");
        series.getData().add(new XYChart.Data<>("Membres", nbMembres));
        series.getData().add(new XYChart.Data<>("Réunions", nbReunions));
        barChart.getData().add(series);

        chartsBox.getChildren().addAll(pieChart, barChart);

        VBox center = new VBox(20);
        center.setAlignment(Pos.TOP_CENTER);
        center.setPadding(new Insets(20, 0, 0, 0));
        center.getChildren().addAll(lblTitle, statsBox, chartsBox);

        view.setCenter(center);
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", derive(" + color + ", -20%)); -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 8);");
        card.setPrefSize(220, 130);

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("Segoe UI", 36));
        lblValue.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", 16));
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-weight: 600;");

        card.getChildren().addAll(lblValue, lblTitle);
        return card;
    }

    public BorderPane getView() {
        return view;
    }
}
