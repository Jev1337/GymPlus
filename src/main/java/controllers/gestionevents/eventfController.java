package controllers.gestionevents;

import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import controllers.gestionuser.GlobalVar;
import entities.gestionevents.Event_details;
import entities.gestionevents.Event_participants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import services.gestionevents.Event_detailsService;
import services.gestionevents.Event_participantsService;
import utils.MyDatabase;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class eventfController {
    private final ObservableList<Event_details> event_details = FXCollections.observableArrayList();
    private final Event_detailsService eventDetailsService = new Event_detailsService();
    private PreparedStatement hasUserJoinedEventStatement;
    private PreparedStatement decrementSpotsStatement;
    private PreparedStatement incrementSpotsStatement;
    private PreparedStatement getNextEventDateStatement;
    private PreparedStatement update_user_ptsStatement;
    @FXML
    private TableColumn<Event_details, String> datec;

    @FXML
    private TableColumn<Event_details, String> durationc;

    @FXML
    private TextField search_id;
    @FXML
    private Button join_id;

    @FXML
    private TableColumn<Event_details, String> namec;

    @FXML
    private TableColumn<Event_details, String> typec;
    @FXML
    private TableColumn<Event_details, Integer> spotsc;

    @FXML
    private TableView<Event_details> event_detailsTableView;

    @FXML
    private Label countdownLabel;

    private Timeline timeline;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label points_label;
    @FXML
    private Pane event_points;
    @FXML
    private Pane event_pane;
    @FXML
    private Button go_to_reward;
    @FXML
    private Button events;
    @FXML
    private Button whey_btn;
    @FXML
    private Button belt_btn;
    @FXML
    private Button bag_btn;
    public eventfController() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            hasUserJoinedEventStatement = connection.prepareStatement("select * from event_participants where event_details_id = ? and user_id = ?");
            decrementSpotsStatement = connection.prepareStatement("UPDATE event_details  SET nb_places = nb_places - 1 WHERE id = ?");
            incrementSpotsStatement = connection.prepareStatement("UPDATE event_details  SET nb_places = nb_places + 1 WHERE id = ?");
            getNextEventDateStatement = connection.prepareStatement(
                    "SELECT event_date FROM event_details " +
                            "JOIN event_participants ON event_details.id = event_participants.event_details_id " +
                            "WHERE event_participants.user_id = ? AND event_date > NOW() " +
                            "ORDER BY event_date ASC LIMIT 1"
            );
            update_user_ptsStatement = connection.prepareStatement("UPDATE user SET event_points = ? WHERE id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {
        try {
            System.out.println("Points: " + GlobalVar.getUser().getEvent_points());
            afficher();
            points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        startCountdown();
    }

    void afficher() throws SQLException {
        List<Event_details> events = eventDetailsService.getAll();
        event_details.clear();
        event_details.addAll(events);
        event_detailsTableView.setItems(event_details);
        namec.setCellValueFactory(new PropertyValueFactory<>("name"));
        typec.setCellValueFactory(new PropertyValueFactory<>("type"));
        datec.setCellValueFactory(new PropertyValueFactory<>("event_date"));
        durationc.setCellValueFactory(new PropertyValueFactory<>("duree"));
        spotsc.setCellValueFactory(new PropertyValueFactory<>("nb_places"));
    }

    public boolean hasUserJoinedEvent(int event_id, int user_id) throws SQLException {
        hasUserJoinedEventStatement.setInt(1, event_id);
        hasUserJoinedEventStatement.setInt(2, user_id);
        ResultSet rs = hasUserJoinedEventStatement.executeQuery();
        return rs.next();
    }

    public void decrementSpots(int eventId) throws SQLException {
        decrementSpotsStatement.setInt(1, eventId);
        decrementSpotsStatement.executeUpdate();
    }

    public void incrementSpots(int eventId) throws SQLException {
        incrementSpotsStatement.setInt(1, eventId);
        incrementSpotsStatement.executeUpdate();
    }



    @FXML
    public void join_event(ActionEvent actionEvent) throws SQLException {
        Event_details selectedEvent = event_detailsTableView.getSelectionModel().getSelectedItem();

        int id = 0;
        Date nextEventDate = getNextEventDate(0);
        System.out.println("Next event date: " + nextEventDate);
        Connection connection = MyDatabase.getInstance().getConnection();
        if (selectedEvent != null) {
            if (selectedEvent.getNb_places() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No spots available");
                alert.setContentText("Sorry, there are no spots available for this event");
                alert.showAndWait();
                return;
            }
            String name = selectedEvent.getName();
            String type = selectedEvent.getType();
            String date = selectedEvent.getEvent_date();
            String duration = selectedEvent.getDuree();


            if (hasUserJoinedEvent(selectedEvent.getId(), GlobalVar.getUser().getId())) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText("You are already a participant in this event");
                alert.setContentText(" Are you sure you want to leave?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Event_participants ev = new Event_participants(selectedEvent.getId(), 0);
                    Event_participantsService eventParticipantsService = new Event_participantsService();
                    eventParticipantsService.delete(ev.getEvent_id());
                    incrementSpots(selectedEvent.getId());
                    GlobalVar.getUser().setEvent_points(GlobalVar.getUser().getEvent_points() - 100);
                    update_user_pts(GlobalVar.getUser().getId(), GlobalVar.getUser().getEvent_points());
                    points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());
                    afficher();
                    return;
                } else {
                    return;
                }
            }
            System.out.println("Join event: " + name + ", " + type + ", " + date + ", " + duration);
            try {
                PreparedStatement stmt = connection.prepareStatement("SELECT id FROM event_details WHERE name = ? AND type = ? AND event_date = ? AND duree = ? AND nb_places = ?");
                stmt.setString(1, selectedEvent.getName());
                stmt.setString(2, selectedEvent.getType());
                stmt.setString(3, selectedEvent.getEvent_date());
                stmt.setString(4, selectedEvent.getDuree());
                stmt.setInt(5, selectedEvent.getNb_places());
                GlobalVar.getUser().setEvent_points(GlobalVar.getUser().getEvent_points() + 100);
                update_user_pts(GlobalVar.getUser().getId(), GlobalVar.getUser().getEvent_points());
                points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                    System.out.println(id);
                } else {
                    System.out.println("No event found with the given details");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("No event selected");
        }

        Event_participants ev = new Event_participants(id, GlobalVar.getUser().getId());
        try {
            Event_participantsService eventParticipantsService = new Event_participantsService();
            eventParticipantsService.add(ev);
            decrementSpots(id);

            afficher();

            Join_notf();
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }
    boolean check_event_date_and_time_passed(int event_id) {
        Connection connection = MyDatabase.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT event_date FROM event_details WHERE id = ?");
            stmt.setInt(1, event_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date eventDate = rs.getTimestamp("event_date");
                Date now = new Date();
                return eventDate.before(now);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();



        }
        return false;
    }

    private void Join_notf() {
        final var msg = new Notification("Successfully Joined The Event");

        msg.getStyleClass().addAll(
                Styles.ACCENT, Styles.ELEVATED_1
        );
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        msg.setLayoutX(745);
        msg.setLayoutY(80);

        msg.setOnClose(e -> {
            var out = Animations.slideOutRight(msg, Duration.millis(250));
            out.setOnFinished(f -> mainPane.getChildren().remove(msg));
            out.playFromStart();
        });
        var in = Animations.slideInRight(msg, Duration.millis(250));
        if (!mainPane.getChildren().contains(msg)) {
            mainPane.getChildren().add(msg);
        }
        in.playFromStart();
    }

    public Date getNextEventDate(int userId) {
        LocalDateTime nextEventDate = null;
        try {
            getNextEventDateStatement.setInt(1, userId);
            ResultSet rs = getNextEventDateStatement.executeQuery();
            if (rs.next()) {
                nextEventDate = rs.getTimestamp("event_date").toLocalDateTime();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nextEventDate != null ? Date.from(nextEventDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    private void startCountdown() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateCountdown()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateCountdown() {
        Date nextEventDate = getNextEventDate(GlobalVar.getUser().getId());
        if (nextEventDate != null) {
            Date now = new Date();
            long diff = nextEventDate.getTime() - now.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String countdown = String.format("%d days and %02d:%02d:%02d", diffDays, diffHours, diffMinutes, diffSeconds);
            countdownLabel.setText(diff >= 0 ? "Time until next event: " + countdown : "Time since last event: " + countdown);
        } else {
            countdownLabel.setText("No upcoming events");
        }
    }

    @FXML
    public void search() {
        String query = search_id.getText();
        List<Event_details> searchResults = eventDetailsService.search(query);
        event_details.clear();
        event_details.addAll(searchResults);
    }



    public void go_to_rewards(ActionEvent actionEvent) {

        FadeOutRight f = new FadeOutRight(event_pane);
        f.setOnFinished((e) -> {
            event_pane.setVisible(false);
            FadeInRight f2 = new FadeInRight(event_points);
            event_points.setOpacity(0);
            event_points.setVisible(true);
            f2.play();
        });
        f.play();
    }

    @FXML
    public void back_to_events(ActionEvent actionEvent) {

        if (!event_pane.isVisible()) {

            FadeOutRight f = new FadeOutRight(event_points);
            f.setOnFinished((e) -> {

                event_points.setVisible(false);


                event_pane.setVisible(true);
                event_pane.setOpacity(0);


                FadeInRight f2 = new FadeInRight(event_pane);
                f2.play();
            });
            f.play();
        }
    }
    void update_user_pts(int id, int points) {
        try {
            update_user_ptsStatement.setInt(1, points);
            update_user_ptsStatement.setInt(2, id);
            update_user_ptsStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void claim_whey(ActionEvent actionEvent) {
        if (GlobalVar.getUser().getEvent_points() >= 2500) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to claim GymPlus Whey Protein?");
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points()-2500));
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GlobalVar.getUser().setEvent_points(GlobalVar.getUser().getEvent_points() - 2500);
                update_user_pts(GlobalVar.getUser().getId(), GlobalVar.getUser().getEvent_points());
                points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());


                String details = "User ID: " + GlobalVar.getUser().getId() +
                        "\nFirst Name: " + GlobalVar.getUser().getFirstname() +
                        "\nLast Name: " + GlobalVar.getUser().getLastname() +
                        "\nReward: GymPlus Whey Protein";
                ByteArrayOutputStream bout =
                        QRCode.from(details)
                                .withSize(200, 200)
                                .to(ImageType.PNG)
                                .stream();


                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                javafx.scene.image.Image qrImage = null;
                try {
                    qrImage = SwingFXUtils.toFXImage(ImageIO.read(bin), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("Congratulations");
                qrCodeAlert.setHeaderText("You have successfully claimed GymPlus Whey Protein");

                ImageView qrImageView = new ImageView(qrImage);
                qrCodeAlert.setGraphic(qrImageView);
                ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                qrCodeAlert.getButtonTypes().setAll(saveButtonType, ButtonType.CANCEL);
                Optional<ButtonType> qrResult = qrCodeAlert.showAndWait();
                if (qrResult.get() == saveButtonType){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(qrImage, null), "png", file);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                qrCodeAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not enough points");
            alert.setContentText("You need at least 2500 points to claim this reward");
            alert.showAndWait();
        }
    }

    @FXML
    public void claim_belt(ActionEvent actionEvent) {
        if (GlobalVar.getUser().getEvent_points() >= 1500) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to claim GymPlus Weightlifting Belt?");
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points()-1500));
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GlobalVar.getUser().setEvent_points(GlobalVar.getUser().getEvent_points() - 1500);
                update_user_pts(GlobalVar.getUser().getId(), GlobalVar.getUser().getEvent_points());
                points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());


                String details = "User ID: " + GlobalVar.getUser().getId() +
                        "\nFirst Name: " + GlobalVar.getUser().getFirstname() +
                        "\nLast Name: " + GlobalVar.getUser().getLastname() +
                        "\nReward: GymPlus Weightlifting Belt";
                ByteArrayOutputStream bout =
                        QRCode.from(details)
                                .withSize(200, 200)
                                .to(ImageType.PNG)
                                .stream();


                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                javafx.scene.image.Image qrImage = null;
                try {
                    qrImage = SwingFXUtils.toFXImage(ImageIO.read(bin), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Display QR code in an alert
                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("Congratulations");
                qrCodeAlert.setHeaderText("You have successfully claimed GymPlus Weightlifting Belt");

                ImageView qrImageView = new ImageView(qrImage);
                qrCodeAlert.setGraphic(qrImageView);
                ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                qrCodeAlert.getButtonTypes().setAll(saveButtonType, ButtonType.CANCEL);
                Optional<ButtonType> qrResult = qrCodeAlert.showAndWait();
                if (qrResult.get() == saveButtonType){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(qrImage, null), "png", file);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                qrCodeAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not enough points");
            alert.setContentText("You need at least 1500 points to claim this reward");
            alert.showAndWait();
        }
    }

    @FXML
    public void claim_bag(ActionEvent actionEvent) {
        if (GlobalVar.getUser().getEvent_points() >= 2000) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to claim GymPlus Gym Bag?");
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points()-2000));

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GlobalVar.getUser().setEvent_points(GlobalVar.getUser().getEvent_points() - 2000);
                update_user_pts(GlobalVar.getUser().getId(), GlobalVar.getUser().getEvent_points());
                points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());


                String details = "User ID: " + GlobalVar.getUser().getId() +
                        "\nFirst Name: " + GlobalVar.getUser().getFirstname() +
                        "\nLast Name: " + GlobalVar.getUser().getLastname() +
                        "\nReward: GymPlus Gym Bag";
                ByteArrayOutputStream bout =
                        QRCode.from(details)
                                .withSize(200, 200)
                                .to(ImageType.PNG)
                                .stream();

                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                javafx.scene.image.Image qrImage = null;
                try {
                    qrImage = SwingFXUtils.toFXImage(ImageIO.read(bin), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("Congratulations");
                qrCodeAlert.setHeaderText("You have successfully claimed GymPlus Gym Bag");

                ImageView qrImageView = new ImageView(qrImage);
                qrCodeAlert.setGraphic(qrImageView);
                ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                qrCodeAlert.getButtonTypes().setAll(saveButtonType, ButtonType.CANCEL);
                Optional<ButtonType> qrResult = qrCodeAlert.showAndWait();
                if (qrResult.get() == saveButtonType){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(qrImage, null), "png", file);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                qrCodeAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not enough points");
            alert.setContentText("You need at least 2000 points to claim this reward");
            alert.showAndWait();
        }
    }

}








