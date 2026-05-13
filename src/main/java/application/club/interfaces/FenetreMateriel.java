package application.club.interfaces;

import application.club.basededonnees.RequetesMateriel;
import application.club.donnees.Materiel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.util.List;

public class FenetreMateriel extends BorderPane {
    private TableView<Materiel> table;
    private RequetesMateriel requetesMateriel;
    private ObservableList<Materiel> masterData;

    private TextField txtNom, txtQuantite;
    private ComboBox<String> txtEtat;

    public FenetreMateriel() {
        requetesMateriel = new RequetesMateriel();
        masterData = FXCollections.observableArrayList();

        setPadding(new Insets(20));

        // Table
        table = new TableView<>();
        
        TableColumn<Materiel, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMateriel()).asObject());
        
        TableColumn<Materiel, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        
        TableColumn<Materiel, Integer> colQuantite = new TableColumn<>("Quantité");
        colQuantite.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        
        TableColumn<Materiel, String> colEtat = new TableColumn<>("État");
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));

        table.getColumns().addAll(colId, colNom, colQuantite, colEtat);
        table.setItems(masterData);

        // Form
        txtNom = new TextField(); txtNom.setPromptText("Nom du Matériel");
        txtQuantite = new TextField(); txtQuantite.setPromptText("Quantité");
        txtEtat = new ComboBox<>();
        txtEtat.getItems().addAll("Neuf", "Bon état", "Endommagé");
        txtEtat.setPromptText("État");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setOnAction(e -> ajouterMateriel());

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerMateriel());

        HBox form = new HBox(10, txtNom, txtQuantite, txtEtat, btnAjouter, btnSupprimer);
        form.setPadding(new Insets(10, 0, 0, 0));

        Label title = new Label("Gestion des Matériels");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox center = new VBox(10, title, table, form);
        setCenter(center);

        loadData();
    }

    public void loadData() {
        try {
            masterData.clear();
            List<Materiel> materiels = requetesMateriel.findAll();
            masterData.addAll(materiels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ajouterMateriel() {
        try {
            Materiel m = new Materiel(0, txtNom.getText(), Integer.parseInt(txtQuantite.getText()), txtEtat.getValue());
            requetesMateriel.create(m);
            loadData();
            txtNom.clear(); 
            txtQuantite.clear(); 
            txtEtat.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerMateriel() {
        Materiel m = table.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                requetesMateriel.deleteById(m.getIdMateriel());
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
