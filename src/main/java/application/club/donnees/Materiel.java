package application.club.donnees;

public class Materiel {
    private int idMateriel;
    private String nom;
    private int quantite;
    private String etat;

    public Materiel() {
    }

    public Materiel(int idMateriel, String nom, int quantite, String etat) {
        this.idMateriel = idMateriel;
        this.nom = nom;
        this.quantite = quantite;
        this.etat = etat;
    }

    public Materiel(String nom, int quantite, String etat) {
        this.nom = nom;
        this.quantite = quantite;
        this.etat = etat;
    }

    public int getIdMateriel() {
        return idMateriel;
    }

    public void setIdMateriel(int idMateriel) {
        this.idMateriel = idMateriel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
