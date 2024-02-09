package entities.gestionSuivi;

import java.sql.Date;

public class Objectif {
    private int id_objectif;
    private int id_user;
    private float poids_Obj;
    private Date dateD;
    private Date dateF;
    private float poids_Act;
    private float taille ;
    private String alergie;
    private String typeObj;
    private String coachName;
    private String firstName;
    private int coachId ;

    public  Objectif (){

    }
    public Objectif(float poids_Obj,Date dateF,float poids_Act,float taille,String alergie,String typeObj,int CoachId){
        this.setPoids_Obj(poids_Obj);
        this.setDateF(dateF);
        this.setPoids_Act(poids_Act);
        this.setTaille(taille);
        this.setAlergie(alergie);
        this.setTypeObj(typeObj);
        this.setCoachId(CoachId);

    }

    public Objectif(int id_objectif,float poids_Obj,Date dateF,float poids_Act,float taille,String alergie,String typeObj,int CoachId){
        this.setId_objectif(id_objectif);
        this.setPoids_Obj(poids_Obj);
        this.setDateF(dateF);
        this.setPoids_Act(poids_Act);
        this.setTaille(taille);
        this.setAlergie(alergie);
        this.setTypeObj(typeObj);
        this.setCoachId(CoachId);

    }


    public int getId_objectif() {
        return id_objectif;
    }

    public void setId_objectif(int id_objectif) {
        this.id_objectif = id_objectif;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public float getPoids_Obj() {
        return poids_Obj;
    }

    public void setPoids_Obj(float poids_Obj) {
        this.poids_Obj = poids_Obj;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

    public Date getDateF() {
        return dateF;
    }

    public void setDateF(Date dateF) {
        this.dateF = dateF;
    }

    public float getPoids_Act() {
        return poids_Act;
    }

    public void setPoids_Act(float poids_Act) {
        this.poids_Act = poids_Act;
    }

    public float getTaille() {
        return taille;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public String getAlergie() {
        return alergie;
    }

    public void setAlergie(String alergie) {
        this.alergie = alergie;
    }

    public String getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(String typeObj) {
        this.typeObj = typeObj;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }
}
