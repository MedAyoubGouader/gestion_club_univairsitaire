package application.club.interfaces;

import application.club.basededonnees.RequetesCotisation;
import application.club.basededonnees.RequetesDepense;
import application.club.basededonnees.RequetesMembre;
import application.club.donnees.Cotisation;
import application.club.donnees.Depense;
import application.club.donnees.Membre;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FenetreFinance extends BorderPane {
    private TableView<Cotisation> tableCotisation;
    private TableView<Depense> tableDepense;
    
    private RequetesCotisation requetesCotisation;
    private RequetesDepense requetesDepense;
    
    private ObservableList<Cotisation> dataCotisation;
    private ObservableList<Depense> dataDepense;

    public FenetreFinance() {
        requetesCotisation = new RequetesCotisation();
        requetesDepense = new RequetesDepense();
        
        dataCotisation = FXCollections.observableArrayList();
        dataDepense = FXCollections.observableArrayList();

        setPadding(new Insets(20));

        // --- Table Cotisation ---
        tableCotisation = new TableView<>();
        TableColumn<Cotisation, Integer> colCId = new TableColumn<>("ID");
        colCId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCotisation()).asObject());
        TableColumn<Cotisation, Double> colCMontant = new TableColumn<>("Montant");
        colCMontant.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getMontant().doubleValue()).asObject());
        TableColumn<Cotisation, LocalDate> colCDate = new TableColumn<>("Date");
        colCDate.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDatePaiement()));
        TableColumn<Cotisation, Integer> colCMembre = new TableColumn<>("ID Membre");
        colCMembre.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdMembre()).asObject());

        tableCotisation.getColumns().addAll(colCId, colCMontant, colCDate, colCMembre);
        tableCotisation.setItems(dataCotisation);
        tableCotisation.setPrefHeight(200);

        // --- Table Depense ---
        tableDepense = new TableView<>();
        TableColumn<Depense, Integer> colDId = new TableColumn<>("ID");
        colDId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdDepense()).asObject());
        TableColumn<Depense, String> colDDesc = new TableColumn<>("Description");
        colDDesc.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMotif()));
        TableColumn<Depense, Double> colDMontant = new TableColumn<>("Montant");
        colDMontant.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getMontant().doubleValue()).asObject());
        TableColumn<Depense, LocalDate> colDDate = new TableColumn<>("Date");
        colDDate.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getDateDepense()));

        tableDepense.getColumns().addAll(colDId, colDDesc, colDMontant, colDDate);
        tableDepense.setItems(dataDepense);
        tableDepense.setPrefHeight(200);

        // --- Formulaire Cotisation ---
        TextField txtMontantCotisation = new TextField(); txtMontantCotisation.setPromptText("Montant");
        DatePicker dpDateCotisation = new DatePicker(); dpDateCotisation.setPromptText("Date");
        ComboBox<Membre> comboMembre = new ComboBox<>();
        comboMembre.setPromptText("Membre");
        comboMembre.getItems().addAll(new RequetesMembre().obtenirTousLesMembres());

        Button btnAjouterCotisation = new Button("Ajouter Cotisation");
        btnAjouterCotisation.setOnAction(e -> {
            try {
                Cotisation c = new Cotisation();
                c.setMontant(new BigDecimal(txtMontantCotisation.getText()));
                c.setDatePaiement(dpDateCotisation.getValue());
                if (comboMembre.getValue() != null) c.setIdMembre(comboMembre.getValue().getIdMembre());
                requetesCotisation.add(c);
                loadData();
                txtMontantCotisation.clear(); dpDateCotisation.setValue(null); comboMembre.getSelectionModel().clearSelection();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });
        HBox formCotisation = new HBox(10, txtMontantCotisation, dpDateCotisation, comboMembre, btnAjouterCotisation);
        formCotisation.setPadding(new Insets(10, 0, 20, 0));

        // --- Formulaire Depense ---
        TextField txtMontantDepense = new TextField(); txtMontantDepense.setPromptText("Montant");
        TextField txtMotifDepense = new TextField(); txtMotifDepense.setPromptText("Motif");
        DatePicker dpDateDepense = new DatePicker(); dpDateDepense.setPromptText("Date");

        Button btnAjouterDepense = new Button("Ajouter Dépense");
        btnAjouterDepense.setOnAction(e -> {
            try {
                Depense d = new Depense();
                d.setMontant(new BigDecimal(txtMontantDepense.getText()));
                d.setMotif(txtMotifDepense.getText());
                d.setDateDepense(dpDateDepense.getValue());
                requetesDepense.add(d);
                loadData();
                txtMontantDepense.clear(); txtMotifDepense.clear(); dpDateDepense.setValue(null);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });
        HBox formDepense = new HBox(10, txtMontantDepense, txtMotifDepense, dpDateDepense, btnAjouterDepense);
        formDepense.setPadding(new Insets(10, 0, 0, 0));

        Label title = new Label("Gestion des Finances");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle1 = new Label("Cotisations");
        subtitle1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label subtitle2 = new Label("Dépenses");
        subtitle2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox center = new VBox(10, title, subtitle1, tableCotisation, formCotisation, subtitle2, tableDepense, formDepense);
        setCenter(center);

        loadData();
    }

    public void loadData() {
        try {
            dataCotisation.clear();
            dataCotisation.addAll(requetesCotisation.findAll());
            
            dataDepense.clear();
            dataDepense.addAll(requetesDepense.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
