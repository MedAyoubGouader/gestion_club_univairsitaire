package application.club.donnees;

import java.time.LocalDate;

public class Tache {
    private int idTache;
    private String description;
    private String etat;
    private LocalDate dateLimite;
    private Integer idMembre;

    public Tache() {
    }

    public Tache(int idTache, String description, String etat, LocalDate dateLimite, Integer idMembre) {
        this.idTache = idTache;
        this.description = description;
        this.etat = etat;
        this.dateLimite = dateLimite;
        this.idMembre = idMembre;
    }

    public Tache(String description, String etat, LocalDate dateLimite, Integer idMembre) {
        this.description = description;
        this.etat = etat;
        this.dateLimite = dateLimite;
        this.idMembre = idMembre;
    }

    public int getIdTache() {
        return idTache;
    }

    public void setIdTache(int idTache) {
        this.idTache = idTache;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public LocalDate getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(LocalDate dateLimite) {
        this.dateLimite = dateLimite;
    }

    public Integer getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(Integer idMembre) {
        this.idMembre = idMembre;
    }
}
