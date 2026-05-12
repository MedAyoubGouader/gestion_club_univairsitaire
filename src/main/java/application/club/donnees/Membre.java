package application.club.donnees;

import java.sql.Date;

public class Membre {
    private int idMembre;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;
    
    // Nouveaux champs demandés par ton cahier des charges
    private int nbEvenements;
    private String statutActif;

    public Membre() {
    }

    public Membre(int idMembre, String nom, String prenom, String email, String telephone, String poste) {
        this.idMembre = idMembre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.poste = poste;
    }

    public Membre(String nom, String prenom, String email, String telephone, String poste) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.poste = poste;
    }

    public int getIdMembre() { return idMembre; }
    public void setIdMembre(int idMembre) { this.idMembre = idMembre; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public int getNbEvenements() { return nbEvenements; }
    public void setNbEvenements(int nbEvenements) { this.nbEvenements = nbEvenements; }

    public String getStatutActif() { return statutActif; }
    public void setStatutActif(String statutActif) { this.statutActif = statutActif; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + poste + ") - " + statutActif;
    }
}

