package application.club.donnees;

import java.time.LocalDate;

public class Reunion {

    private Integer idReunion;
    private LocalDate dateReunion;
    private String heureReunion;
    private String sujet;
    private Integer duree;

    public Reunion() {
    }

    public Reunion(Integer idReunion, LocalDate dateReunion, String heureReunion, String sujet, Integer duree) {
        this.idReunion = idReunion;
        this.dateReunion = dateReunion;
        this.heureReunion = heureReunion;
        this.sujet = sujet;
        this.duree = duree;
    }

    public Integer getIdReunion() {
        return idReunion;
    }

    public void setIdReunion(Integer idReunion) {
        this.idReunion = idReunion;
    }

    public LocalDate getDateReunion() {
        return dateReunion;
    }

    public void setDateReunion(LocalDate dateReunion) {
        this.dateReunion = dateReunion;
    }

    public String getHeureReunion() {
        return heureReunion;
    }

    public void setHeureReunion(String heureReunion) {
        this.heureReunion = heureReunion;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Reunion{" +
                "idReunion=" + idReunion +
                ", dateReunion=" + dateReunion +
                ", heureReunion='" + heureReunion + '\'' +
                ", sujet='" + sujet + '\'' +
                ", duree=" + duree +
                '}';
    }
}
