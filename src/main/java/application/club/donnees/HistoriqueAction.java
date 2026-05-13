package application.club.donnees;

import java.sql.Timestamp;

public class HistoriqueAction {
    private int idAction;
    private String action;
    private Timestamp dateHeure;
    private Integer idAdmin;

    public HistoriqueAction() {
    }

    public HistoriqueAction(int idAction, String action, Timestamp dateHeure, Integer idAdmin) {
        this.idAction = idAction;
        this.action = action;
        this.dateHeure = dateHeure;
        this.idAdmin = idAdmin;
    }

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Timestamp dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }

    @Override
    public String toString() {
        return "HistoriqueAction{" +
                "idAction=" + idAction +
                ", action='" + action + '\'' +
                ", dateHeure=" + dateHeure +
                ", idAdmin=" + idAdmin +
                '}';
    }
}
