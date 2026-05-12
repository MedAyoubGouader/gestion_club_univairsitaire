package application.club.donnees;

import java.sql.Date;

public class Evenement {
    private int idEvenement;
    private String titre;
    private Date dateEvenement; // On utilise java.sql.Date pour la compatibilité avec Oracle
    private String lieu;
    private String description;

    // Constructeurs
    public Evenement() {}

    public Evenement(int idEvenement, String titre, Date dateEvenement, String lieu, String description) {
        this.idEvenement = idEvenement;
        this.titre = titre;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.description = description;
    }

    public Evenement(String titre, Date dateEvenement, String lieu, String description) {
        this.titre = titre;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.description = description;
    }

    // Getters et Setters
    public int getIdEvenement() { return idEvenement; }
    public void setIdEvenement(int idEvenement) { this.idEvenement = idEvenement; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public Date getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(Date dateEvenement) { this.dateEvenement = dateEvenement; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return titre + " - " + dateEvenement;
    }
}

