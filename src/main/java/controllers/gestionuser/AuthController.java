package controllers.gestionuser;

import animatefx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class AuthController {

    private FadeInRight fadeInRightAnimation = new FadeInRight();
    private FadeInLeft fadeInLeftAnimation = new FadeInLeft();
    private FadeOutLeft fadeOutLeftAnimation = new FadeOutLeft();
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();


    @FXML
    private Button signin_btn;

    @FXML
    private Button signup_btn;

    @FXML
    private Pane signin_pane;

    @FXML
    private Button signin_switch_btn;

    @FXML
    private Pane signin_switch_pane;

    @FXML
    private Pane signup_pane;

    @FXML
    private Button signup_switch_btn;

    @FXML
    private Pane signup_switch_pane;


    @FXML
    void signin_btn_act(ActionEvent event) {

    }
    @FXML
    void signup_btn_act(ActionEvent event) {

    }
    @FXML
    void signup_switch_btn_act(ActionEvent event) {
        fadeOutLeftAnimation.setNode(signin_pane);
        fadeOutLeftAnimation.setOnFinished(e -> {
            signin_switch_pane.setOpacity(0);
            signin_switch_pane.setVisible(true);
            signin_pane.setVisible(false);
            fadeInLeftAnimation.setNode(signin_switch_pane);
            fadeInLeftAnimation.play();

        });
        fadeOutLeftAnimation.play();
        fadeOutRightAnimation.setNode(signup_switch_pane);
        fadeOutRightAnimation.setOnFinished(e -> {
            signup_pane.setOpacity(0);
            signup_pane.setVisible(true);
            signup_switch_pane.setVisible(false);
            fadeInRightAnimation.setNode(signup_pane);
            fadeInRightAnimation.play();
        });
        fadeOutRightAnimation.play();
    }
    @FXML
    void signin_switch_btn_act(ActionEvent event) {
        fadeOutRightAnimation.setNode(signup_pane);
        fadeOutRightAnimation.setOnFinished(e -> {
            signup_switch_pane.setOpacity(0);
            signup_switch_pane.setVisible(true);
            signup_pane.setVisible(false);
            fadeInRightAnimation.setNode(signup_switch_pane);
            fadeInRightAnimation.play();
        });
        fadeOutRightAnimation.play();
        fadeOutLeftAnimation.setNode(signin_switch_pane);
        fadeOutLeftAnimation.setOnFinished(e -> {
            signin_pane.setOpacity(0);
            signin_pane.setVisible(true);
            signin_switch_pane.setVisible(false);
            fadeInLeftAnimation.setNode(signin_pane);
            fadeInLeftAnimation.play();
        });
        fadeOutLeftAnimation.play();
    }
    public void initialize() {
        fadeInLeftAnimation.setNode(signin_pane);
        fadeInLeftAnimation.play();
        fadeInRightAnimation.setNode(signup_switch_pane);
        fadeInRightAnimation.play();
    }




}
