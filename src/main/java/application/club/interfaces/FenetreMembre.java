package application.club.interfaces;

import application.club.basededonnees.RequetesMembre;
import application.club.donnees.Membre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

// L'interface de tous les membres. J'ajoute les validations regex email
public class FenetreMembre {
    private BorderPane view;
    private TableView<Membre> table;
    private RequetesMembre RequetesMembre;
    private ObservableList<Membre> membreList;

    private TextField txtNom, txtPrenom, txtEmail, txtTelephone;
    private ComboBox<String> cbPoste;

    public FenetreMembre() {
        RequetesMembre = new RequetesMembre();
        view = new BorderPane();
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("Gestion des Membres 👥");
        lblTitle.setFont(Font.font("Arial", 26));
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        BorderPane.setMargin(lblTitle, new Insets(0, 0, 20, 0));
        view.setTop(lblTitle);

        setupTable();
        view.setCenter(table);

        view.setBottom(setupForm());
        loadData();
    }

    private void setupTable() {
        table = new TableView<>();
        table.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-border-color: #bdc3c7;");

        TableColumn<Membre, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idMembre"));

        TableColumn<Membre, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Membre, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Membre, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(180);

        TableColumn<Membre, String> colPoste = new TableColumn<>("Poste (Rôle)");
        colPoste.setCellValueFactory(new PropertyValueFactory<>("poste"));
        colPoste.setPrefWidth(120);

        TableColumn<Membre, Integer> colEvents = new TableColumn<>("Nb Événements");
        colEvents.setCellValueFactory(new PropertyValueFactory<>("nbEvenements"));
        
        TableColumn<Membre, String> colStatut = new TableColumn<>("Statut BDD");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutActif"));
        colStatut.setPrefWidth(120);

        table.getColumns().addAll(colId, colNom, colPrenom, colEmail, colPoste, colEvents, colStatut);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNom.setText(newSel.getNom());
                txtPrenom.setText(newSel.getPrenom());
                txtEmail.setText(newSel.getEmail());
                txtTelephone.setText(newSel.getTelephone());
                cbPoste.setValue(newSel.getPoste());
            }
        });
    }

    private VBox setupForm() {
        VBox formConteneur = new VBox(20);
        formConteneur.setPadding(new Insets(20, 0, 0, 0));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        txtNom = new TextField(); txtNom.setPromptText("Ex: Dupont");
        txtPrenom = new TextField(); txtPrenom.setPromptText("Ex: Jean");
        txtEmail = new TextField(); txtEmail.setPromptText("contact@exemple.com");
        txtTelephone = new TextField(); txtTelephone.setPromptText("0600000000");
        
        cbPoste = new ComboBox<>(FXCollections.observableArrayList(
            "Membre simple", "Membre du bureau", "Président", "Trésorier", "Secrétaire"
        ));
        cbPoste.setPromptText("Sélectionner un poste");

        grid.add(new Label("Nom:"), 0, 0); grid.add(txtNom, 1, 0);
        grid.add(new Label("Prénom:"), 2, 0); grid.add(txtPrenom, 3, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(txtEmail, 1, 1);
        grid.add(new Label("Tél:"), 2, 1); grid.add(txtTelephone, 3, 1);
        grid.add(new Label("Poste:"), 0, 2); grid.add(cbPoste, 1, 2);

        HBox btnBox = new HBox(15);
        Button btnAjouter = creerBouton("➕ Ajouter", "#27ae60");
        Button btnModifier = creerBouton("✏️ Modifier", "#f39c12");
        Button btnSupprimer = creerBouton("🗑️ Supprimer", "#c0392b");
        Button btnVider = creerBouton("🔄 Vider", "#7f8c8d");

        btnAjouter.setOnAction(e -> ajouterMembre());
        btnModifier.setOnAction(e -> modifierMembre());
        btnSupprimer.setOnAction(e -> supprimerMembre());
        btnVider.setOnAction(e -> viderChamps());

        btnBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnVider);
        formConteneur.getChildren().addAll(grid, btnBox);
        return formConteneur;
    }

    private Button creerBouton(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                     "-fx-font-size: 14px; -fx-padding: 8 15 8 15; -fx-background-radius: 5;");
        return b;
    }

    public void loadData() {
        membreList = FXCollections.observableArrayList(RequetesMembre.obtenirTousLesMembres());
        table.setItems(membreList);
    }

    private void ajouterMembre() {
        if (champsInvalides()) return;
        
        // REGEX DU CAHIER DES CHARGES POUR UN VRAI EMAIL ! 
        if (!validateEmail(txtEmail.getText())) {
            return;
        }

        if (RequetesMembre.existeMembre(txtNom.getText(), txtPrenom.getText(), -1)) {
            showAlert("Blocage", "Une personne nommée " + txtNom.getText() + " " + txtPrenom.getText() + " existe déjà !");
            return;
        }

        Membre m = new Membre(txtNom.getText(), txtPrenom.getText(), txtEmail.getText(), txtTelephone.getText(), cbPoste.getValue());
        RequetesMembre.ajouterMembre(m);
        loadData(); viderChamps();
    }

    private void modifierMembre() {
        Membre sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        if (champsInvalides()) return;
        
        if (!validateEmail(txtEmail.getText())) {
            return;
        }

        if (RequetesMembre.existeMembre(txtNom.getText(), txtPrenom.getText(), sel.getIdMembre())) {
            showAlert("Blocage", "Vous ne pouvez pas le renommer ainsi. La combinaison existe déjà.");
            return;
        }

        sel.setNom(txtNom.getText());
        sel.setPrenom(txtPrenom.getText());
        sel.setEmail(txtEmail.getText());
        sel.setTelephone(txtTelephone.getText());
        sel.setPoste(cbPoste.getValue());
        
        RequetesMembre.modifierMembre(sel);
        loadData();
    }

    private void supprimerMembre() {
        Membre sel = table.getSelectionModel().getSelectedItem();
        if (sel != null) {
            RequetesMembre.supprimerMembre(sel.getIdMembre());
            loadData(); viderChamps();
        }
    }

    private boolean champsInvalides() {
        if (txtNom.getText().trim().isEmpty() || txtPrenom.getText().trim().isEmpty() || cbPoste.getValue() == null) {
            showAlert("Incomplet", "Veuillez remplir au moins le nom, le prénom et le poste.");
            return true;
        }
        return false;
    }

    private boolean validateEmail(String emailStr) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+\\..+)$"; // Doit contenir arobase et point pour un vrai domaine
        if (!emailStr.matches(regex)) {
            showAlert("Email Invalide", "Veuillez saisir un VRAI email. Exemple: etudiant@gmail.com ou prenom@tn.com");
            return false;
        }
        return true;
    }

    private void viderChamps() {
        txtNom.clear(); txtPrenom.clear(); txtEmail.clear(); txtTelephone.clear(); cbPoste.setValue(null);
    }

    private void showAlert(String title, String c) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(c); a.showAndWait();
    }

    public BorderPane getView() { return view; }
}

