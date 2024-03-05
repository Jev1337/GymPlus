package controllers.gestionsuivi;

import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import services.gestionSuivi.PlanningService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ObjectifDisplayOnRightController implements Initializable {

    @FXML
    private Label DateDLabel;

    @FXML
    private Label DateFinLabel;

    @FXML
    private Label NameLabel;

    @FXML
    private Label PoidsActLabel;

    @FXML
    private Label PoidsObjLabel;

    @FXML
    private Label TailleLabel;

    @FXML
    private Label descriptionLbale;

    @FXML
    private ImageView ImageUser;
    @FXML
    private HBox HboxComeBack;



    private  static  PlanningController planningController ;
    public void setPallningControler(PlanningController planningController) {
        ObjectifDisplayOnRightController.planningController = planningController;
    }
    private  static  ObjectifController objectifController ;
    public void setObjectifController(ObjectifController objectifController) {
        ObjectifDisplayOnRightController.objectifController= objectifController;
    }



    @FXML
    private Button AddPlanningButton;
    @FXML
    private Button UpdatePlan;

    @FXML
    private Button DeletePlan;


    void makeModifButtonInvisble(){
        DeletePlan.setVisible(false);
        UpdatePlan.setVisible(false);
        AddPlanningButton.setVisible(true);

    }
    void makeModifButtonVisible(){
        UpdatePlan.setVisible(true);
        DeletePlan.setVisible(true);
        AddPlanningButton.setVisible(false);
    }

    void makeModifButtonVisible2(){
        UpdatePlan.setVisible(false);
        DeletePlan.setVisible(false);
        AddPlanningButton.setVisible(false);
    }

    void  dataInfos2(Objectif objPlan) throws IOException {
        String coachPhotoPath = "src/assets/profileuploads/" + objPlan.getCoachPhoto();
        if (coachPhotoPath != null && !coachPhotoPath.isEmpty()) {
            File file = new File(coachPhotoPath);
            if (file.exists()) {
                try {
                    String imageUrl = file.toURI().toURL().toString();
                    System.out.println(imageUrl);
                    ImageUser.setImage(null);
                    Image coachImage = new Image(imageUrl, true);
                    ImageUser.setImage(coachImage);

                    //*** rendering into cercle
                    Circle clip = new Circle(ImageUser.getFitWidth() / 2, ImageUser.getFitHeight() / 2, ImageUser.getFitWidth() / 2
                    );
                    ImageUser.setClip(clip);
                    ImageUser.setPreserveRatio(false);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        descriptionLbale.setText(objPlan.getAlergie());
        NameLabel.setText(objPlan.getFirstName());
        PoidsObjLabel.setText(Float.toString(objPlan.getPoids_Obj()));
        PoidsActLabel.setText(Float.toString(objPlan.getPoids_Act()));
        TailleLabel.setText(Float.toString(objPlan.getTaille()));
        DateDLabel.setText(objPlan.getDateD().toString());
        DateFinLabel.setText(objPlan.getDateF().toString());
    }

  void  dataInfos(Objectif objPlan) throws IOException {
      String coachPhotoPath = "src/assets/profileuploads/" + objPlan.getCoachPhoto();
      if (coachPhotoPath != null && !coachPhotoPath.isEmpty()) {
          File file = new File(coachPhotoPath);
          if (file.exists()) {
              try {
                  String imageUrl = file.toURI().toURL().toString();
                  System.out.println(imageUrl);
                  ImageUser.setImage(null);
                  Image coachImage = new Image(imageUrl, true);
                  ImageUser.setImage(coachImage);

                  //*** rendering into cercle
                  Circle clip = new Circle(ImageUser.getFitWidth() / 2, ImageUser.getFitHeight() / 2, ImageUser.getFitWidth() / 2
                  );
                  ImageUser.setClip(clip);
                  ImageUser.setPreserveRatio(false);


              } catch (MalformedURLException e) {
                  e.printStackTrace();
              }
          }
      }
      descriptionLbale.setText(objPlan.getAlergie());
      NameLabel.setText(objPlan.getFirstName());
      PoidsObjLabel.setText(Float.toString(objPlan.getPoids_Obj()));
      PoidsActLabel.setText(Float.toString(objPlan.getPoids_Act()));
      TailleLabel.setText(Float.toString(objPlan.getTaille()));
      DateDLabel.setText(objPlan.getDateD().toString());
      DateFinLabel.setText(objPlan.getDateF().toString());

      AddPlanningButton.getStyleClass().addAll(
              Styles.BUTTON_OUTLINED, Styles.ACCENT
              );
      AddPlanningButton.setMnemonicParsing(true);


      UpdatePlan.getStyleClass().addAll(
              Styles.BUTTON_OUTLINED, Styles.ACCENT
      );
      UpdatePlan.setMnemonicParsing(true);


      AddPlanningButton.setOnAction((E) -> {
          Animations.wobble(AddPlanningButton).playFromStart();
          try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/Planning.fxml"));
              if(planningController != null){
                  planningController.MakeItVisibleAddingPlanPan(objPlan);
                  //planningController.displayListePlanning();
                  //planningController.displayListeObjectif();
              }
              else {
                  System.out.println("problem");
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      });

      UpdatePlan.setOnAction((E) -> {
          Animations.wobble(UpdatePlan).playFromStart();
          try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/Planning.fxml"));
              if(planningController != null){
                  planningController.MakeItVisibleAddingPlanPan(objPlan);
                  //planningController.displayListePlanning();
                 // planningController.displayListeObjectif();
              }
              else {
                  System.out.println("problem");
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      });



      DeletePlan.getStyleClass().addAll(
              Styles.BUTTON_OUTLINED, Styles.ACCENT
      );
      DeletePlan.setMnemonicParsing(true);
      DeletePlan.setOnAction((E) -> {
          Animations.wobble(DeletePlan).playFromStart();
          try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/Planning.fxml"));
              if(planningController != null){
                  System.out.println("objPlan.getId_planning()");
                  System.out.println(objPlan.getId_planning());
                  PlanningService planningService = new PlanningService();
                  planningService.delete(objPlan.getId_planning());
                  setButtonInvisible();
                 planningController.displayListePlanning();
                  planningController.displayListeObjectif();
                  planningController.warning2();
              }
              else {
                  System.out.println("problem");
              }
          } catch (SQLException e) {
              throw new RuntimeException(e);
          }
      });
}

    void setButtonInvisible() {
        DeletePlan.setTranslateX(DeletePlan.getWidth());
        DeletePlan.setVisible(false);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), DeletePlan);
        transition.setToX(0);
        transition.play();

        UpdatePlan.setTranslateX(UpdatePlan.getWidth());
        UpdatePlan.setVisible(false);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), UpdatePlan);
        transition2.setToX(0);
        transition2.play();

        AddPlanningButton.setTranslateX(-AddPlanningButton.getWidth());
        AddPlanningButton.setVisible(true);
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), AddPlanningButton);
        transition3.setToX(0);
        transition3.play();


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var normalBtn = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.BACKWARD));
        normalBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        HboxComeBack.getChildren().add(normalBtn);

        normalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(planningController != null){
                    planningController.makePaneAddInvisible();
                }
                else {
                    System.out.println("problem");
                }
            }
        });

    }
}
