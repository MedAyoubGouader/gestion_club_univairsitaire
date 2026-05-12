package application.club.interfaces;

import application.club.basededonnees.RequetesEvenement;
import application.club.basededonnees.RequetesMembre;
import application.club.basededonnees.RequetesParticipation;
import application.club.donnees.Evenement;
import application.club.donnees.Membre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FenetreEvenement {
    private BorderPane view;
    private TableView<Evenement> table;
    private RequetesEvenement RequetesEvenement;
    private ObservableList<Evenement> evenementList;

    // Champs pour le formulaire d'ajout d'événement
    private TextField txtTitre, txtLieu;
    private TextArea txtDescription;
    private DatePicker dpDate;

    public FenetreEvenement() {
        RequetesEvenement = new RequetesEvenement();
        view = new BorderPane();
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("Gestion des Événements 📅");
        lblTitle.setFont(Font.font("Arial", 26));
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #9b59b6;");
        BorderPane.setMargin(lblTitle, new Insets(0, 0, 20, 0));
        view.setTop(lblTitle);

        setupTable();
        view.setCenter(table);

        // Ajout du formulaire complet au bas de la page
        view.setBottom(setupForm());

        loadData();
    }

    private void setupTable() {
        table = new TableView<>();
        table.setStyle("-fx-font-size: 14px;");
        
        TableColumn<Evenement, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));

        TableColumn<Evenement, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colTitre.setPrefWidth(150);

        TableColumn<Evenement, Date> colDate = new TableColumn<>("Date Prévue");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));
        colDate.setPrefWidth(120);

        TableColumn<Evenement, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colLieu.setPrefWidth(120);

        table.getColumns().addAll(colId, colTitre, colDate, colLieu);

        // Événement au clic sur le tableau
        table.setRowFactory(tv -> {
            TableRow<Evenement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Evenement ev = row.getItem();
                    // Si on clique simplement, on remplit le formulaire pour l'éditer
                    txtTitre.setText(ev.getTitre());
                    dpDate.setValue(ev.getDateEvenement().toLocalDate());
                    txtLieu.setText(ev.getLieu() != null ? ev.getLieu() : "");
                    txtDescription.setText(ev.getDescription() != null ? ev.getDescription() : "");

                    // Si on DOUBLE-CLIQUE, on ouvre la sous-interface des présences !
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        ouvrirFenetrePresences(ev);
                    }
                }
            });
            return row;
        });
    }

    private VBox setupForm() {
        VBox formConteneur = new VBox(20);
        formConteneur.setPadding(new Insets(20, 0, 0, 0));

        // Grille pour les champs de création de l'événement
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        txtTitre = new TextField(); txtTitre.setPromptText("Ex: Réunion d'information");
        dpDate = new DatePicker(); dpDate.setPromptText("Choisir une date");
        txtLieu = new TextField(); txtLieu.setPromptText("Ex: Salle 104");
        txtDescription = new TextArea(); 
        txtDescription.setPromptText("Description de l'événement...");
        txtDescription.setPrefRowCount(2);
        txtDescription.setPrefWidth(300);

        grid.add(new Label("Titre de l'événement:"), 0, 0); grid.add(txtTitre, 1, 0);
        grid.add(new Label("Date:"), 2, 0); grid.add(dpDate, 3, 0);
        grid.add(new Label("Lieu:"), 0, 1); grid.add(txtLieu, 1, 1);
        grid.add(new Label("Description:"), 2, 1); grid.add(txtDescription, 3, 1);

        HBox btnBox = new HBox(15);
        
        Button btnAjouter = new Button("➕ Ajouter Événement");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAjouter.setOnAction(e -> ajouterEvenement());

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        btnModifier.setOnAction(e -> modifierEvenement());

        Button btnSupprimer = new Button("🗑️ Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSupprimer.setOnAction(e -> supprimerEvenement());

        Button btnPresences = new Button("🧑‍🤝‍🧑 Gérer les présences (Sous-interface)");
        btnPresences.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold;");
        btnPresences.setOnAction(e -> {
            Evenement ev = table.getSelectionModel().getSelectedItem();
            if (ev == null) {
                new Alert(Alert.AlertType.WARNING, "Veuillez d'abord sélectionner un événement dans le tableau !").show();
            } else {
                ouvrirFenetrePresences(ev);
            }
        });

        btnBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnPresences);
        
        Label infoTuto = new Label("💡 Astuce : Double-cliquez sur un événement dans le tableau pour gérer ses présences !");
        infoTuto.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");

        formConteneur.getChildren().addAll(grid, btnBox, infoTuto);
        return formConteneur;
    }

    private void ajouterEvenement() {
        if (txtTitre.getText().isEmpty() || dpDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Le titre et la date sont obligatoires !").show();
            return;
        }

        Evenement ev = new Evenement();
        ev.setTitre(txtTitre.getText());
        ev.setDateEvenement(Date.valueOf(dpDate.getValue()));
        ev.setLieu(txtLieu.getText());
        ev.setDescription(txtDescription.getText());

        RequetesEvenement.ajouterEvenement(ev);
        loadData();
        viderChamps();
    }

    private void modifierEvenement() {
        Evenement sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        if (txtTitre.getText().isEmpty() || dpDate.getValue() == null) return;

        sel.setTitre(txtTitre.getText());
        sel.setDateEvenement(Date.valueOf(dpDate.getValue()));
        sel.setLieu(txtLieu.getText());
        sel.setDescription(txtDescription.getText());

        RequetesEvenement.modifierEvenement(sel);
        loadData();
    }

    private void supprimerEvenement() {
        Evenement sel = table.getSelectionModel().getSelectedItem();
        if (sel != null) {
            RequetesEvenement.supprimerEvenement(sel.getIdEvenement());
            loadData();
            viderChamps();
        }
    }

    private void viderChamps() {
        txtTitre.clear();
        dpDate.setValue(null);
        txtLieu.clear();
        txtDescription.clear();
    }

    public void loadData() {
        evenementList = FXCollections.observableArrayList(RequetesEvenement.obtenirTousLesEvenements());
        table.setItems(evenementList);
    }

    // ==========================================
    // NOTRE FAMEUSE SOUS-INTERFACE DE PRESENCE
    // ==========================================
    private void ouvrirFenetrePresences(Evenement evenement) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Présences à l'événement : " + evenement.getTitre());
        
        VBox conteneur = new VBox(15);
        conteneur.setPadding(new Insets(20));

        Label titre = new Label("Ajouter une participation à : " + evenement.getTitre());
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Champs de recherche (ID ou Nom complet)
        HBox boxRecherche = new HBox(10);
        TextField txtRecherche = new TextField();
        txtRecherche.setPromptText("Saisir un ID ou (Nom Prénom)");
        txtRecherche.setPrefWidth(250);
        Button btnTrouver = new Button("Chercher & Ajouter");

        // Tableau des participants
        TableView<Membre> tableParticipants = new TableView<>();
        TableColumn<Membre, Integer> cid = new TableColumn<>("ID");
        cid.setCellValueFactory(new PropertyValueFactory<>("idMembre"));
        TableColumn<Membre, String> cNom = new TableColumn<>("Nom");
        cNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TableColumn<Membre, String> cPrenom = new TableColumn<>("Prénom");
        cPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tableParticipants.getColumns().addAll(cid, cNom, cPrenom);

        RequetesMembre RequetesMembre = new RequetesMembre();
        RequetesParticipation requetesParticipation = new RequetesParticipation();

        // Remplir avec les membres qui participent déjà
        Runnable rafraichirPresents = () -> {
            List<Membre> presentsDB = requetesParticipation.obtenirParticipants(evenement.getIdEvenement());
            tableParticipants.setItems(FXCollections.observableArrayList(presentsDB));
        };
        rafraichirPresents.run();

        btnTrouver.setOnAction(e -> {
            String saisie = txtRecherche.getText();
            if (saisie.isEmpty()) return;

            Membre cible = RequetesMembre.rechercherMembreIdOuNom(saisie);
            if (cible == null) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Impossible de trouver un membre avec cet ID ou ce Nom/Prénom !");
                a.show();
                return;
            }

            boolean success = requetesParticipation.ajouterParticipation(cible.getIdMembre(), evenement.getIdEvenement(), "Participant");
            
            if (success) {
                txtRecherche.clear();
                rafraichirPresents.run();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Membre (ID: "+cible.getIdMembre()+") a été ajouté ! Son statut global vient de passer à 'Actif'.");
                a.show();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Ce membre a peut-être déjà été validé à cet événement.");
                a.show();
            }
        });

        boxRecherche.getChildren().addAll(txtRecherche, btnTrouver);
        conteneur.getChildren().addAll(titre, new Label("Renseignez un membre par son identifiant ou par ses prénoms :"), boxRecherche, new Label("Actuellement inscrits :"), tableParticipants);

        Scene s = new Scene(conteneur, 600, 500);
        popup.setScene(s);
        popup.show();
    }

    public BorderPane getView() { return view; }
}

