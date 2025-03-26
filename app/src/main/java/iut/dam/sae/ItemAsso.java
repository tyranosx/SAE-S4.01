package iut.dam.sae;

public class ItemAsso {
    private String nom;
    private String description;
    private int imageResId;
    private String siteWeb;
    private String category; // Ajout de la catégorie

    public ItemAsso(String nom, String description, int imageResId, String siteWeb, String category) {
        this.nom = nom;
        this.description = description;
        this.imageResId = imageResId;
        this.siteWeb = siteWeb;
        this.category = category;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public String getCategory() {
        return category;
    }

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

    public void setCategory(String category) {
        this.category = category;
    }
}
