package application.club.donnees;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Depense {

    private Integer idDepense;
    private BigDecimal montant;
    private LocalDate dateDepense;
    private String motif;
    private Integer idAdmin;

    public Depense() {
    }

    public Depense(Integer idDepense, BigDecimal montant, LocalDate dateDepense, String motif, Integer idAdmin) {
        this.idDepense = idDepense;
        this.montant = montant;
        this.dateDepense = dateDepense;
        this.motif = motif;
        this.idAdmin = idAdmin;
    }

    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDate dateDepense) {
        this.dateDepense = dateDepense;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }
}
