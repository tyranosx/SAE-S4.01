package iut.dam.sae;

import java.util.Date;

public class MonActiviteDonItem {
    private String association;
    private int montant;
    private Date date;

    // Constructeur
    public MonActiviteDonItem(String association, int montant, Date date) {
        this.association = association;
        this.montant = montant;
        this.date = date;
    }

    // Getters
    public String getAssociation() {
        return association;
    }

    public int getMontant() {
        return montant;
    }

    public Date getDate() {
        return date;
    }

    // Setters (au cas où tu en aurais besoin plus tard)
    public void setAssociation(String association) {
        this.association = association;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}