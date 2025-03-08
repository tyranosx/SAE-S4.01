package iut.dam.sae;

public class ItemAssociationActivity {
    private String nom;
    private String description;
    private int logoResId; // Référence vers une image drawable
    private String siteWeb; // URL du site web de l'association

    public void ItemAssociation(String nom, String description, int logoResId, String siteWeb) {
        this.nom = nom;
        this.description = description;
        this.logoResId = logoResId;
        this.siteWeb = siteWeb;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public String getSiteWeb() {
        return siteWeb;
    }
}