package entities.gestionequipements;

public  class Equipements_details{

    private int id;
    private String name;
    private String description;
    private String duree_de_vie;
    private String etat;

    public Equipements_details() {
    }

    public Equipements_details(int id, String name, String description, String duree_de_vie, String etat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duree_de_vie = duree_de_vie;
        this.etat = etat;
    }

    public Equipements_details(String name, String description, String duree_de_vie, String etat) {
        this.name = name;
        this.description = description;
        this.duree_de_vie = duree_de_vie;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuree_de_vie() {
        return duree_de_vie;
    }

    public void setDuree_de_vie(String duree_de_vie) {
        this.duree_de_vie = duree_de_vie;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Equipements_details{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duree_de_vie='" + duree_de_vie + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }


}
