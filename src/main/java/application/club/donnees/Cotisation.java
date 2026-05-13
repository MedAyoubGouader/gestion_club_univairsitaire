package application.club.donnees;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cotisation {

    private Integer idCotisation;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private Integer idMembre;

    public Cotisation() {
    }

    public Cotisation(Integer idCotisation, BigDecimal montant, LocalDate datePaiement, Integer idMembre) {
        this.idCotisation = idCotisation;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.idMembre = idMembre;
    }

    public Integer getIdCotisation() {
        return idCotisation;
    }

    public void setIdCotisation(Integer idCotisation) {
        this.idCotisation = idCotisation;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Integer getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(Integer idMembre) {
        this.idMembre = idMembre;
    }
}
