package iut.dam.sae;

public class DonItem {
    private String montant;
    private String date;

    public DonItem() { } // Constructeur vide requis pour Firebase

    public DonItem(String montant, String date) {
        this.montant = montant;
        this.date = date;
    }

    public String getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }
}
