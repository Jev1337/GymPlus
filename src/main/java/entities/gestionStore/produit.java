package entities.gestionStore;

public class produit {

    private int idProduit;
    private String name;
    private float prix;
    private int stock;
    private String description;
    private String categorie;
    private String photo;
    private int seuil;
    private float promo;

    //getters and setters
    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSeuil() {
        return seuil;
    }

    public void setSeuil(int seuil) {
        this.seuil = seuil;
    }

    public float getPromo() {
        return promo;
    }

    public void setPromo(float promo) {
        this.promo = promo;
    }
    //const param et non


    public produit(int idProduit, String name, float prix, int stock, String description, String categorie, String photo, int seuil, float promo) {
        this.idProduit = idProduit;
        this.name = name;
        this.prix = prix;
        this.stock = stock;
        this.description = description;
        this.categorie = categorie;
        this.photo = photo;
        this.seuil = seuil;
        this.promo = promo;
    }

    public produit() {
    }

    //tous les att sauf idProduit car auto increment

    public produit(String name, float prix, int stock, String description, String categorie, String photo, int seuil, float promo) {
        this.name = name;
        this.prix = prix;
        this.stock = stock;
        this.description = description;
        this.categorie = categorie;
        this.photo = photo;
        this.seuil = seuil;
        this.promo = promo;
    }


    //toString


    @Override
    public String toString() {
        return "produit{" +
                "idProduit=" + idProduit +
                ", name='" + name + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", photo='" + photo + '\'' +
                ", seuil=" + seuil +
                ", promo=" + promo +
                '}';
    }
}
