package application.club.interfaces;

import application.club.basededonnees.RequetesTache;
import application.club.basededonnees.RequetesMembre;
import application.club.donnees.Tache;
import application.club.donnees.Membre;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FenetreTache extends BorderPane {
    private TableView<Tache> table;
    private RequetesTache requetesTache;
    private ObservableList<Tache> masterData;

    private TextField txtDescription, txtEtat;
    private ComboBox<Membre> txtIdMembre;
    private DatePicker dpDateLimite;

    public FenetreTache() {
        requetesTache = new RequetesTache();
        masterData = FXCollections.observableArrayList();

        setPadding(new Insets(20));

        // Table
        table = new TableView<>();
        
        TableColumn<Tache, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdTache()).asObject());
        
        TableColumn<Tache, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Tache, String> colEtat = new TableColumn<>("État");
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));
        
        TableColumn<Tache, LocalDate> colDate = new TableColumn<>("Date Limite");
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateLimite()));
        
        TableColumn<Tache, Integer> colMembre = new TableColumn<>("ID Membre");
        colMembre.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getIdMembre();
            return new SimpleObjectProperty<>(id);
        });

        table.getColumns().addAll(colId, colDesc, colEtat, colDate, colMembre);
        table.setItems(masterData);

        // Form
        txtDescription = new TextField(); txtDescription.setPromptText("Description (ex: Achat)");
        txtEtat = new TextField(); txtEtat.setPromptText("État (ex: En cours)");
        dpDateLimite = new DatePicker(); dpDateLimite.setPromptText("Date Limite");
        
        txtIdMembre = new ComboBox<>();
        txtIdMembre.setPromptText("Membre");
        txtIdMembre.getItems().addAll(new RequetesMembre().obtenirTousLesMembres());

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setOnAction(e -> ajouterTache());

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerTache());

        HBox form = new HBox(10, txtDescription, txtEtat, dpDateLimite, txtIdMembre, btnAjouter, btnSupprimer);
        form.setPadding(new Insets(10, 0, 0, 0));

        Label title = new Label("Gestion des Tâches");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox center = new VBox(10, title, table, form);
        setCenter(center);

        loadData();
    }

    public void loadData() {
        try {
            masterData.clear();
            List<Tache> taches = requetesTache.findAll();
            masterData.addAll(taches);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ajouterTache() {
        try {
            Integer idMembre = null;
            if (txtIdMembre.getValue() != null) {
                idMembre = txtIdMembre.getValue().getIdMembre();
            }
            Tache t = new Tache(txtDescription.getText(), txtEtat.getText(), dpDateLimite.getValue(), idMembre);
            requetesTache.create(t);
            loadData();
            txtDescription.clear(); 
            txtEtat.clear(); 
            dpDateLimite.setValue(null); 
            txtIdMembre.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerTache() {
        Tache t = table.getSelectionModel().getSelectedItem();
        if (t != null) {
            try {
                requetesTache.deleteById(t.getIdTache());
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
