package iut.dam.sae;

public class ItemAsso {
    private String nom;
    private String description;
    private int logo;
    private String lien;
    private String categorie;

    public ItemAsso(String nom, String description, int logo, String lien, String categorie) {
        this.nom = nom;
        this.description = description;
        this.logo = logo;
        this.lien = lien;
        this.categorie = categorie;
    }

    // Constructeur existant sans catégorie (pour compatibilité)
    public ItemAsso(String nom, String description, int logo, String lien) {
        this(nom, description, logo, lien, "Général");
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getLogo() {
        return logo;
    }

    public String getLien() {
        return lien;
    }

    public String getCategorie() {
        return categorie;
    }
}