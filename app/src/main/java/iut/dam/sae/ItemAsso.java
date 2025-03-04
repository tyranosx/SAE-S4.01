package iut.dam.sae;

public class ItemAsso {
    private String nom;
    private String description;
    private int imageResId;
    private String url;

    public ItemAsso(String nom, String description, int imageResId, String url) {
        this.nom = nom;
        this.description = description;
        this.imageResId = imageResId;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
