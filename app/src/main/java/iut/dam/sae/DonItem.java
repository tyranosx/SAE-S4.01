package iut.dam.sae;

import java.util.Date;

public class DonItem {
    private int montant;
    private Date date;
    private String prenom;

    // Constructeur vide requis pour Firebase
    public DonItem() { }

    // Constructeur avec paramètres
    public DonItem(int montant, Date date, String prenom) {
        this.montant = montant;
        this.date = date;
        this.prenom = prenom;
    }

    // Getters
    public int getMontant() {
        return montant;
    }

    public Date getDate() {
        return date;
    }

    public String getPrenom() {
        return prenom;
    }

    // Setters (si jamais tu en as besoin)
    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
