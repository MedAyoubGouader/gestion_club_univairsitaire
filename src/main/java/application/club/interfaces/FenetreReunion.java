package application.club.interfaces;

import application.club.basededonnees.RequetesReunion;
import application.club.donnees.Reunion;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.List;

public class FenetreReunion extends BorderPane {
    private TableView<Reunion> table;
    private RequetesReunion requetesReunion;
    private ObservableList<Reunion> masterData;

    private DatePicker dpDate;
    private TextField txtHeure, txtSujet, txtDuree;

    public FenetreReunion() {
        requetesReunion = new RequetesReunion();
        masterData = FXCollections.observableArrayList();

        setPadding(new Insets(20));

        // Table
        table = new TableView<>();
        
        TableColumn<Reunion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdReunion()).asObject());
        
        TableColumn<Reunion, String> colSujet = new TableColumn<>("Sujet");
        colSujet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSujet()));
        
        TableColumn<Reunion, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateReunion()));

        TableColumn<Reunion, String> colHeure = new TableColumn<>("Heure");
        colHeure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureReunion()));

        TableColumn<Reunion, Integer> colDuree = new TableColumn<>("Durée (min)");
        colDuree.setCellValueFactory(cellData -> {
            Integer d = cellData.getValue().getDuree();
            return new SimpleObjectProperty<>(d != null ? d : 0);
        });

        table.getColumns().addAll(colId, colSujet, colDate, colHeure, colDuree);
        table.setItems(masterData);

        // Form
        txtSujet = new TextField(); txtSujet.setPromptText("Sujet");
        dpDate = new DatePicker(); dpDate.setPromptText("Date");
        txtHeure = new TextField(); txtHeure.setPromptText("Heure (ex: 14:30)");
        txtDuree = new TextField(); txtDuree.setPromptText("Durée en minutes");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setOnAction(e -> ajouterReunion());

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerReunion());

        HBox form = new HBox(10, txtSujet, dpDate, txtHeure, txtDuree, btnAjouter, btnSupprimer);
        form.setPadding(new Insets(10, 0, 0, 0));

        Label title = new Label("Gestion des Réunions");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox center = new VBox(10, title, table, form);
        setCenter(center);

        loadData();
    }

    public void loadData() {
        try {
            masterData.clear();
            // We need to fetch data. Assuming RequetesReunion has a method to fetch all.
            // Using reflection/workaround if findAll doesn't exist, we will try.
            List<Reunion> reunions = requetesReunion.findAll(); 
            masterData.addAll(reunions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ajouterReunion() {
        try {
            Integer duree = null;
            if (!txtDuree.getText().isEmpty()) {
                duree = Integer.parseInt(txtDuree.getText());
            }
            Reunion r = new Reunion(null, dpDate.getValue(), txtHeure.getText(), txtSujet.getText(), duree);
            requetesReunion.add(r);
            loadData();
            txtSujet.clear(); dpDate.setValue(null); txtHeure.clear(); txtDuree.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerReunion() {
        Reunion r = table.getSelectionModel().getSelectedItem();
        if (r != null) {
            try {
                requetesReunion.deleteById(r.getIdReunion());
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
