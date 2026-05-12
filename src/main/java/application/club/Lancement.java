package application.club;

import javafx.application.Application;

public class Lancement {
    /* 
     * Cette classe de "lanceur" magique est nécessaire pour éviter les erreurs de modules 
     * dans IntelliJ lorsqu'on lance des interfaces JavaFX. 
     */
    public static void main(String[] args) {
        System.out.println("Mise à jour automatique de la BDD de gestion (Ajout du Statut physique)...");
        application.club.outils.ConnexionOracle.mettreAjourBaseDeDonnees();
        
        System.out.println("Lancement de l'interface graphique JavaFX...");
        // On demande à JavaFX de lancer notre classe "ApplicationPrincipale.java"
        Application.launch(ApplicationPrincipale.class, args);
    }
}

