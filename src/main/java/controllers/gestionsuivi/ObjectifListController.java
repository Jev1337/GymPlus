package controllers.gestionsuivi;

import entities.gestionSuivi.Objectif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import utils.MyDatabase;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ObjectifListController {

    @FXML
    private Label DateDebutField;

    @FXML
    private Label DateFinField;

    @FXML
    private Label NameField;

    @FXML
    private Label TypeObjField;
    @FXML
    private ImageView CoachPic;

    MyDatabase c = new MyDatabase();
    private Objectif objectif;


    public int getCoachIdByName(String name_coach_selected) {
        String query = "SELECT id  FROM user WHERE FirstName = ? AND Role='Coach'";
        int coachId = -1;

        try {
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, name_coach_selected);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                coachId = rs.getInt("id");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coachId;
    }


    public ObservableList<Objectif> getObjectifStatusList() {

        ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
        try {
            int userId = 2;

            String query = "SELECT o.idObjectif , o.poidsObj, o.DateD, o.DateF, o.PoidsAct, o.Taille, o.Alergie, o.TypeObj, u.photo, u.username AS username " +
                    "FROM objectif o " +
                    "JOIN user u ON o.CoachId = u.id " +
                    "WHERE o.userId = ?  AND u.role='Coach' ";


            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Objectif objectifs = new Objectif();
                objectifs.setId_objectif(rs.getInt("idObjectif"));
                objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                objectifs.setDateD(rs.getDate("DateD"));
                objectifs.setDateF(rs.getDate("DateF"));
                objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                objectifs.setTaille(rs.getFloat("Taille"));
                objectifs.setAlergie(rs.getString("Alergie"));
                objectifs.setTypeObj(rs.getString("TypeObj"));
                objectifs.setCoachPhoto(rs.getString("photo"));
                objectifs.setCoachName(rs.getString("username"));

                objectifList.add(objectifs);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objectifList;
    }

    public void setObjectif(Objectif objectif) throws MalformedURLException {
        NameField.setText(objectif.getCoachName());
        TypeObjField.setText(objectif.getTypeObj());
        java.sql.Date dateD = objectif.getDateD();
        java.sql.Date dateF = objectif.getDateF();

        String dateString = null;
        String dateString2 = null;

        if (dateD != null && dateF!=null) {
            dateString = dateD.toString();
            dateString2 = dateF.toString();

        }
        DateDebutField.setText(dateString);
        DateFinField.setText(dateString2);

        String coachPhotoPath = objectif.getCoachPhoto();
      //  System.out.println(coachPhotoPath);
        if (coachPhotoPath != null && !coachPhotoPath.isEmpty()) {
            File file = new File(coachPhotoPath);
            if (file.exists()) {
                try {
                    String imageUrl = file.toURI().toURL().toString();
                    System.out.println(imageUrl);
                    CoachPic.setImage(null);
                    Image coachImage = new Image(imageUrl, true);
                    CoachPic.setImage(coachImage);

                    //*** rendering into cercle
                    Circle clip = new Circle(CoachPic.getFitWidth()/2, CoachPic.getFitHeight()/2, CoachPic.getFitWidth()/2
                    );
                    CoachPic.setClip(clip);
                    CoachPic.setPreserveRatio(false);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else {
                String imageUrl = file.toURI().toURL().toString();
                file = new File(imageUrl);
                CoachPic.setImage(null);
                Image coachImage = new Image(imageUrl, true);
                CoachPic.setImage(coachImage);
                //*** rendering into cercle
                Circle clip = new Circle(CoachPic.getFitWidth()/2, CoachPic.getFitHeight()/2, CoachPic.getFitWidth()/2
                );
                CoachPic.setClip(clip);
                CoachPic.setPreserveRatio(false);


                System.out.println("File does not exist: " + file.getAbsolutePath());

            }
        } else {
            System.out.printf("tititiiti");
            CoachPic.setImage(null);
        }

        }
        }








