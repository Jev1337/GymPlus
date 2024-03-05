package controllers.gestionsuivi;

import controllers.gestionuser.GlobalVar;
import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import test.ObjectifTestingFX;
import utils.MyDatabase;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PlanningListController {

    @FXML
    private Label DateDebutLabel;

    @FXML
    private Label DateFinLabel;

    @FXML
    private ImageView IamgeAdherant;

    @FXML
    private Hyperlink LoadingFoodLabel;

    @FXML
    private Hyperlink LoadingTrainingLabel;

    @FXML
    private Label NomLabel;

    @FXML
    private Label ObjectifLabel;

    MyDatabase c = new MyDatabase();

    @FXML
    private Hyperlink ObjectifLabelHyperlink;

    private  static  PlanningListController planningListController ;
    public void setPlanningListController(PlanningListController planningListController) {
        PlanningListController.planningListController = planningListController;
    }

    @FXML
    private Text labelPlanningEtat;
    ObjectifTestingFX  objectifTestingFX = new ObjectifTestingFX()  ;

    public void showPdfTrainningPlan(String urlTrainingPlan){
        String pdfTrainingFile = urlTrainingPlan;
        File pdfFile = new File(pdfTrainingFile);
        HostServices hostServices = objectifTestingFX.getApplicationHostServices();
        if (pdfFile.exists()) {
            hostServices.showDocument(pdfFile.toURI().toString());
        } else {
            System.out.println("File does not exist: "+pdfTrainingFile);
        }
    }

    public void showPdfFoodPlan(String urlFoodPlan){
        String pdfTrainingFile = urlFoodPlan;
        File pdfFile = new File(pdfTrainingFile);
        HostServices hostServices = objectifTestingFX.getApplicationHostServices();
        if (pdfFile.exists()) {
            hostServices.showDocument(pdfFile.toURI().toString());
        } else {
            System.out.println("File does not exist: "+pdfTrainingFile);
        }
    }
    @FXML
    private Text StatusText;
    @FXML
    private Text etatProgress;

    @FXML
    private ImageView InProgressImage;
    @FXML
    private Separator Seperatorr;

    public void setData(Objectif objPlan) {
        String coachPhotoPath = "src/assets/profileuploads/" + objPlan.getCoachPhoto();
        ObservableList<Planning> objectifList = getPlanningItems(objPlan.getId_planning());


        if (objPlan.getId_planning() != 0) {
            for (Planning planning : objectifList) {
                etatProgress.setVisible(false);
                InProgressImage.setVisible(false);
                Seperatorr.setVisible(false);
                StatusText.setVisible(false);
                LoadingTrainingLabel.setVisible(true);
                LoadingFoodLabel.setVisible(true);
                labelPlanningEtat.setVisible(true);
                LoadingTrainingLabel.setOnAction(event -> showPdfTrainningPlan(planning.getTrainingProg()));
                LoadingFoodLabel.setOnAction(event -> showPdfFoodPlan(planning.getFoodProg()));

            }


        }else {
            LoadingTrainingLabel.setVisible(false);
            LoadingFoodLabel.setVisible(false);
            labelPlanningEtat.setVisible(false);

        }



        NomLabel.setText(objPlan.getFirstName());
        DateDebutLabel.setText(objPlan.getDateD().toString());
        DateFinLabel.setText(objPlan.getDateF().toString());
        if (coachPhotoPath != null && !coachPhotoPath.isEmpty()) {
            File file = new File(coachPhotoPath);
            if (file.exists()) {
                try {
                    String imageUrl = file.toURI().toURL().toString();
                    System.out.println(imageUrl);
                    IamgeAdherant.setImage(null);
                    Image coachImage = new Image(imageUrl, true);
                    IamgeAdherant.setImage(coachImage);

                    //*** rendering into cercle
                    Circle clip = new Circle(IamgeAdherant.getFitWidth() / 2, IamgeAdherant.getFitHeight() / 2, IamgeAdherant.getFitWidth() / 2
                    );
                    IamgeAdherant.setClip(clip);
                    IamgeAdherant.setPreserveRatio(false);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ObservableList<Objectif> getObjectifList2FromObjectifController  () {
        ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
        try {
            int userId = GlobalVar.getUser().getId();
            String query = "SELECT \n" +
                    "  (SELECT COUNT(*) FROM objectif o \n" +
                    "   JOIN user u ON o.userId = u.id AND u.role='client'\n" +
                    "   WHERE o.userId = ? AND NOT EXISTS (SELECT 1 FROM planning p WHERE p.idObjectif = o.idObjectif)) AS count,\n" +
                    "  o.idObjectif, o.poidsObj, o.dateD, o.dateF, o.PoidsAct, o.Taille, o.Alergie, o.TypeObj, u.firstname, u.photo, u.date_naiss\n" +
                    "FROM objectif o\n" +
                    "JOIN user u ON o.userId = u.id AND u.role='client'\n" +
                    "WHERE o.userId = ? AND NOT EXISTS (SELECT 1 FROM planning p WHERE p.idObjectif = o.idObjectif);";

            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                Objectif objectifs = new Objectif();
                objectifs.setNumberPlansInProgressive(rs.getInt("count"));
                objectifs.setId_objectif(rs.getInt("idObjectif"));
                objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                objectifs.setDateD(rs.getDate("dateD"));
                objectifs.setDateF(rs.getDate("dateF"));
                objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                objectifs.setTaille(rs.getFloat("Taille"));
                objectifs.setAlergie(rs.getString("Alergie"));
                objectifs.setTypeObj(rs.getString("TypeObj"));
                objectifs.setFirstName(rs.getString("firstname"));
                objectifs.setCoachPhoto(rs.getString("photo"));
                objectifs.setDateNaissance(rs.getDate("date_naiss"));
                objectifList.add(objectifs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectifList;
    }






    public ObservableList<Objectif> getObjectifList  () {
            ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
            try {
                int userId = GlobalVar.getUser().getId();
                String query = "SELECT \n" +
                        "  (SELECT COUNT(*) FROM objectif o \n" +
                        "   JOIN user u ON o.userId = u.id AND u.role='client'\n" +
                        "   WHERE o.CoachId = ? AND NOT EXISTS (SELECT 1 FROM planning p WHERE p.idObjectif = o.idObjectif)) AS count,\n" +
                        "  o.idObjectif, o.poidsObj, o.dateD, o.dateF, o.PoidsAct, o.Taille, o.Alergie, o.TypeObj, u.firstname, u.photo, u.date_naiss\n" +
                        "FROM objectif o\n" +
                        "JOIN user u ON o.userId = u.id AND u.role='client'\n" +
                        "WHERE o.CoachId = ? AND NOT EXISTS (SELECT 1 FROM planning p WHERE p.idObjectif = o.idObjectif);";

                PreparedStatement ps = c.getConnection().prepareStatement(query);
                ps.setInt(1, userId);
                ps.setInt(2, userId);

                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    Objectif objectifs = new Objectif();
                    objectifs.setNumberPlansInProgressive(rs.getInt("count"));
                    objectifs.setId_objectif(rs.getInt("idObjectif"));
                    objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                    objectifs.setDateD(rs.getDate("dateD"));
                    objectifs.setDateF(rs.getDate("dateF"));
                    objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                    objectifs.setTaille(rs.getFloat("Taille"));
                    objectifs.setAlergie(rs.getString("Alergie"));
                    objectifs.setTypeObj(rs.getString("TypeObj"));
                    objectifs.setFirstName(rs.getString("firstname"));
                    objectifs.setCoachPhoto(rs.getString("photo"));
                    objectifs.setDateNaissance(rs.getDate("date_naiss"));
                    objectifList.add(objectifs);
                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return objectifList;
        }




    public ObservableList<Objectif> getPlansList () {
        ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
        int userId = GlobalVar.getUser().getId();
        try {
          String query = "SELECT \n" +
                  "    (\n" +
                  "        SELECT COUNT(*)\n" +
                  "        FROM objectif o\n" +
                  "        JOIN planning p ON p.idObjectif = o.idObjectif\n" +
                  "        JOIN user u ON o.userId = u.id AND u.role = 'client'\n" +
                  "        WHERE o.CoachId = ?\n" +
                  "    ) AS count,\n" +
                  "    o.idObjectif,\n" +
                  "    o.poidsObj,\n" +
                  "    o.dateD,\n" +
                  "    o.dateF,\n" +
                  "    o.PoidsAct,\n" +
                  "    o.Taille,\n" +
                  "    o.Alergie,\n" +
                  "    o.TypeObj,\n" +
                  "    u.firstname,\n" +
                  "    u.photo,\n" +
                  "    u.date_naiss,\n" +
                  "    p.id_Planning\n" +
                  "FROM \n" +
                  "    objectif o\n" +
                  "JOIN \n" +
                  "    planning p ON p.idObjectif = o.idObjectif\n" +
                  "JOIN \n" +
                  "    user u ON o.userId = u.id AND u.role = 'client'\n" +
                  "WHERE \n" +
                  "    o.CoachId = ?;";



            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Objectif objectifs = new Objectif();

                    objectifs.setNumbersPlansDone(rs.getInt("count"));
                    objectifs.setId_objectif(rs.getInt("idObjectif"));
                    objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                    objectifs.setDateD(rs.getDate("dateD"));
                    objectifs.setDateF(rs.getDate("dateF"));
                    objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                    objectifs.setTaille(rs.getFloat("Taille"));
                    objectifs.setAlergie(rs.getString("Alergie"));
                    objectifs.setTypeObj(rs.getString("TypeObj"));
                    objectifs.setFirstName(rs.getString("firstname"));
                    objectifs.setCoachPhoto(rs.getString("photo"));
                    objectifs.setDateNaissance(rs.getDate("date_naiss"));
                    objectifs.setId_planning(rs.getInt("id_Planning"));

                    objectifList.add(objectifs);

                    //sending Planning Dataaaa



            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectifList;

    }





    public ObservableList<Planning> getPlanningItems  (int id_plan) {
        ObservableList<Planning> planningObservableList = FXCollections.observableArrayList();
        try {

            String query = "SELECT * " +
                    "FROM planning p " +
                    "WHERE p.id_Planning = ? ";

            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, id_plan);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                Planning planning = new Planning();
                planning.setId_Planning(rs.getInt("id_Planning"));
                planning.setIdObjectif(rs.getInt("idObjectif"));
                planning.setTrainingProg(rs.getString("TrainingProg"));
                planning.setFoodProg(rs.getString("FoodProg"));
                planningObservableList.add(planning);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planningObservableList;
    }


    public ObservableList<Objectif> getPlansList2 () {
        System.out.println("wawaswaa");
        ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
        int userId = GlobalVar.getUser().getId();
        try {
            String query = "SELECT \n" +
                    "    (\n" +
                    "        SELECT COUNT(*)\n" +
                    "        FROM objectif o\n" +
                    "        JOIN planning p ON p.idObjectif = o.idObjectif\n" +
                    "        JOIN user u ON o.userId = u.id AND u.role = 'client'\n" +
                    "        WHERE o.userId = ?\n" +
                    "    ) AS count,\n" +
                    "    o.idObjectif,\n" +
                    "    o.poidsObj,\n" +
                    "    o.dateD,\n" +
                    "    o.dateF,\n" +
                    "    o.PoidsAct,\n" +
                    "    o.Taille,\n" +
                    "    o.Alergie,\n" +
                    "    o.TypeObj,\n" +
                    "    u.firstname,\n" +
                    "    u.photo,\n" +
                    "    u.date_naiss,\n" +
                    "    p.id_Planning\n" +
                    "FROM \n" +
                    "    objectif o\n" +
                    "JOIN \n" +
                    "    planning p ON p.idObjectif = o.idObjectif\n" +
                    "JOIN \n" +
                    "    user u ON o.userId = u.id AND u.role = 'client'\n" +
                    "WHERE \n" +
                    "    o.userId = ?;";



            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Objectif objectifs = new Objectif();

                objectifs.setNumbersPlansDone(rs.getInt("count"));
                objectifs.setId_objectif(rs.getInt("idObjectif"));
                objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                objectifs.setDateD(rs.getDate("dateD"));
                objectifs.setDateF(rs.getDate("dateF"));
                objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                objectifs.setTaille(rs.getFloat("Taille"));
                objectifs.setAlergie(rs.getString("Alergie"));
                objectifs.setTypeObj(rs.getString("TypeObj"));
                objectifs.setFirstName(rs.getString("firstname"));
                objectifs.setCoachPhoto(rs.getString("photo"));
                objectifs.setDateNaissance(rs.getDate("date_naiss"));
                objectifs.setId_planning(rs.getInt("id_Planning"));

                objectifList.add(objectifs);

                //sending Planning Dataaaa



            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectifList;

    }



    }
