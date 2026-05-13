package application.club.interfaces;

import application.club.basededonnees.RequetesHistoriqueAction;
import application.club.donnees.HistoriqueAction;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.sql.Timestamp;
import java.util.List;

public class FenetreHistorique extends BorderPane {
    private TableView<HistoriqueAction> table;
    private RequetesHistoriqueAction requetesHistorique;
    private ObservableList<HistoriqueAction> masterData;

    public FenetreHistorique() {
        requetesHistorique = new RequetesHistoriqueAction();
        masterData = FXCollections.observableArrayList();

        setPadding(new Insets(20));

        // Table
        table = new TableView<>();
        
        TableColumn<HistoriqueAction, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdAction()).asObject());
        
        TableColumn<HistoriqueAction, String> colAction = new TableColumn<>("Action effectuée");
        colAction.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction()));
        colAction.setPrefWidth(400);

        TableColumn<HistoriqueAction, Timestamp> colDate = new TableColumn<>("Date et Heure");
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateHeure()));
        colDate.setCellFactory(column -> new TableCell<HistoriqueAction, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    setText(sdf.format(item));
                }
            }
        });
        colDate.setPrefWidth(200);

        TableColumn<HistoriqueAction, Integer> colAdmin = new TableColumn<>("ID Admin");
        colAdmin.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getIdAdmin();
            return new SimpleObjectProperty<>(id);
        });

        table.getColumns().addAll(colId, colAction, colDate, colAdmin);
        table.setItems(masterData);

        Label title = new Label("Historique des Actions \u23f2");
        title.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnRefresh = new Button("Rafra\u00eechir");
        btnRefresh.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5;");
        btnRefresh.setOnMouseEntered(e -> btnRefresh.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5;"));
        btnRefresh.setOnMouseExited(e -> btnRefresh.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5;"));
        btnRefresh.setOnAction(e -> loadData());

        table.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox center = new VBox(15, title, btnRefresh, table);
        center.setPadding(new Insets(10, 0, 0, 0));
        setCenter(center);

        loadData();
    }

    public void loadData() {
        try {
            masterData.clear();
            List<HistoriqueAction> liste = requetesHistorique.consulterHistorique();
            masterData.addAll(liste);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
