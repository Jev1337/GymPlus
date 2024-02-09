package entities.gestionSuivi;

public class Planning {
    private int id_Planning;
    private int idObjectif;
    private  int id;
    private int idCoach;
    private String trainingProg ;

    private String foodProg ;

    public Planning(){

    }

    public Planning(int id_Objectif,int id_Coach,String trainningProg,String foodProg){
        this.setIdObjectif(id_Objectif);
        this.setIdCoach(id_Coach);
        this.setTrainingProg(trainningProg);
        this.setFoodProg(foodProg);

    }

    public Planning(int id_Planning,int id_Objectif,int id_Coach,String trainningProg,String foodProg){
        this.setId_Planning(id_Planning);
        this.setIdObjectif(id_Objectif);
        this.setIdCoach(id_Coach);
        this.setTrainingProg(trainningProg);
        this.setFoodProg(foodProg);

    }



    public int getId_Planning() {
        return id_Planning;
    }

    public void setId_Planning(int id_Planning) {
        this.id_Planning = id_Planning;
    }

    public int getIdObjectif() {
        return idObjectif;
    }

    public void setIdObjectif(int idObjectif) {
        this.idObjectif = idObjectif;
    }

    public int getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }

    public String getTrainingProg() {
        return trainingProg;
    }

    public void setTrainingProg(String trainingProg) {
        this.trainingProg = trainingProg;
    }

    public String getFoodProg() {
        return foodProg;
    }

    public void setFoodProg(String foodProg) {
        this.foodProg = foodProg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
