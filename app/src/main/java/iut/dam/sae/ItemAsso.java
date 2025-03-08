package iut.dam.sae;

public class ItemAsso {
    private String nom;
    private String description;
    private int imageResId;  // Nouvelle variable pour l'image
    private String siteWeb;  // Nouvelle variable pour l'URL du site web

    // Constructeur avec toutes les variables
    public ItemAsso(String nom, String description, int imageResId, String siteWeb) {
        this.nom = nom;
        this.description = description;
        this.imageResId = imageResId;
        this.siteWeb = siteWeb;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;   // Retourne l'ID de l'image
    }

    public String getSiteWeb() {
        return siteWeb;   // Retourne l'URL du site web
    }

    // Setters (au cas où tu en aurais besoin)
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }
}
