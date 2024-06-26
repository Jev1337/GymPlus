package controllers.gestionevents;

import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import controllers.gestionuser.GlobalVar;
import entities.gestionevents.Event_details;
import entities.gestionevents.Event_participants;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.controlsfx.control.Rating;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import services.gestionevents.Event_detailsService;
import services.gestionevents.Event_participantsService;
import utils.MyDatabase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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
    @FXML
    private Button rewards;
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
    @FXML
    private Button my_events;
    @FXML
    private Button all_events;
    @FXML
    private Pane whey_pane;
    @FXML
    private Pane belt_pane;
    @FXML
    private Pane bag_pane;

    @FXML
    private ListView<Event_details> list_events;

    @FXML
    private CheckBox check_table;
    @FXML
    private CheckBox myevents;
    @FXML
    private ImageView image_cover_event;
    @FXML
    private Button history_btn;
    @FXML
    private Pane history;
    @FXML
    private ListView<Event_details> list_events1;
    @FXML
    private Button rate;
    @FXML
    private Button back_btn;
    @FXML
    private Button backtoeventpane1;
    @FXML
    private Pane title_events;


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
        join_id.setText("Join");
        join_id.setGraphic(new FontIcon(Feather.LOG_IN));
        join_id.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.SUCCESS);
        join_id.setMnemonicParsing(true);

        history_btn.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT

        );
        history_btn.setMnemonicParsing(true);
        history_btn.setGraphic(new FontIcon(Feather.CLOCK));
        rewards.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT

        );
        rewards.setMnemonicParsing(true);
        rewards.setGraphic(new FontIcon(Feather.GIFT));
        String style = "-fx-color-cell-bg-selected: -color-accent-emphasis;" +
                "-fx-color-cell-fg-selected: -color-fg-emphasis;" +
                "-fx-color-cell-bg-selected-focused: -color-accent-emphasis;" +
                "-fx-color-cell-fg-selected-focused: -color-fg-emphasis;";

        list_events.setStyle(style);
        list_events.getSelectionModel().selectFirst();
        list_events1.setStyle(style);
        list_events1.getSelectionModel().selectFirst();

        events.setText("Back");
        events.setGraphic(new FontIcon(Feather.ARROW_LEFT));
        events.getStyleClass().add(Styles.ACCENT);
        events.setMnemonicParsing(true);
        backtoeventpane1.setText("Back");
        backtoeventpane1.setGraphic(new FontIcon(Feather.ARROW_LEFT));
        backtoeventpane1.getStyleClass().add(Styles.ACCENT);
        backtoeventpane1.setMnemonicParsing(true);

        rate.setText("Rate");
        rate.setGraphic(new FontIcon(Feather.STAR));
        rate.getStyleClass().add(Styles.ACCENT);
        rate.setMnemonicParsing(true);


        Button[] buttons = {bag_btn, whey_btn, belt_btn};

        for (Button button : buttons) {
            button.setGraphic(new FontIcon(Feather.GIFT));
            button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.SUCCESS);
            button.setMnemonicParsing(true);
        }
        try {
            System.out.println("Points: " + GlobalVar.getUser().getEvent_points());
            afficher();
            afficher1();
            afficher_history();
            points_label.setText("Points: " + GlobalVar.getUser().getEvent_points());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        startCountdown();
    }

    @FXML
    public void switchtomyevents(ActionEvent actionEvent) {
        if (myevents.isSelected()) {

            List<Event_details> events = eventDetailsService.getEventsByUserId_now(GlobalVar.getUser().getId());
            //change image_cover_event to anothe image
            image_cover_event.setImage(new javafx.scene.image.Image("file:src/main/resources/assets/images/myevents.png"));
            event_details.clear();
            event_details.addAll(events);
            event_detailsTableView.setItems(event_details);
            namec.setCellValueFactory(new PropertyValueFactory<>("name"));
            typec.setCellValueFactory(new PropertyValueFactory<>("type"));
            datec.setCellValueFactory(new PropertyValueFactory<>("event_date"));
            durationc.setCellValueFactory(new PropertyValueFactory<>("duree"));
            durationc.setCellFactory(column -> {
                return new TableCell<Event_details, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {

                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item + " minutes");
                        }
                    }
                };
            });
            spotsc.setCellValueFactory(new PropertyValueFactory<>("nb_places"));
            spotsc.setCellFactory(column -> {
                return new TableCell<Event_details, Integer>() {
                    private ProgressBar bar = new ProgressBar();

                    {
                        bar.setPrefWidth(100);
                        bar.setStyle("-fx-accent: -color-fg-default;");

                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            int totalSpots = getTableView().getItems().get(getIndex()).getNb_total();
                            int availableSpots = totalSpots - item;
                            bar.setProgress((double) availableSpots / totalSpots);

                            // Create a label to display the number of available spots over the total spots
                            Label spotsLabel = new Label(availableSpots + "/" + totalSpots);
                            spotsLabel.setTextFill(Color.BLACK);

                            // Create a StackPane to hold the progress bar and the label
                            StackPane stack = new StackPane();
                            stack.getChildren().addAll(bar, spotsLabel);

                            setGraphic(stack);
                        }
                    }
                };
            });







            //list too
            List<Event_details> events2 = eventDetailsService.getEventsByUserId_now(GlobalVar.getUser().getId());
            ObservableList<Event_details> event_details2 = FXCollections.observableArrayList(events2);
            list_events.setItems(event_details2);
            list_events.setCellFactory(param -> new ListCell<Event_details>() {
                @Override
                protected void updateItem(Event_details item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getName() == null) {
                        setText(null);
                    } else {
                        // Create a custom layout for each item
                        VBox vbox = new VBox();
                        Label nameLabel = new Label("Name: " + item.getName());
                        Label typeLabel = new Label("Type: " + item.getType());
                        Label dateLabel = new Label("Date: " + item.getEvent_date());
                        Label durationLabel = new Label("Duration: " + item.getDuree()+" minutes");
                        //add the progress bar to the list view
                        ProgressBar bar = new ProgressBar();
                        bar.setStyle("-fx-accent: -color-fg-default;");
                        bar.setPrefWidth(100);
                        int totalSpots = item.getNb_total();
                        int availableSpots = totalSpots - item.getNb_places();
                        bar.setProgress((double) availableSpots / totalSpots);
                        Label spotsLabel = new Label(availableSpots + "/" + totalSpots);
                        spotsLabel.setTextFill(Color.BLACK);
                        StackPane stack = new StackPane();
                        stack.getChildren().addAll(bar, spotsLabel);
                        vbox.getChildren().addAll(nameLabel, typeLabel, dateLabel, durationLabel, stack);
                        setGraphic(vbox);
                    }

                }

            });
        } else {
            try {
                image_cover_event.setImage(new javafx.scene.image.Image("file:src/main/resources/assets/images/eventsclient.png"));
                afficher();
                afficher1();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    void afficher() throws SQLException {
        List<Event_details> events = eventDetailsService.getAll_now();
        event_details.clear();
        event_details.addAll(events);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list_events);
        event_detailsTableView.setItems(event_details);
        namec.setCellValueFactory(new PropertyValueFactory<>("name"));
        typec.setCellValueFactory(new PropertyValueFactory<>("type"));
        datec.setCellValueFactory(new PropertyValueFactory<>("event_date"));
        durationc.setCellValueFactory(new PropertyValueFactory<>("duree"));
        durationc.setCellFactory(column -> {
            return new TableCell<Event_details, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item + " minutes");
                    }
                }
            };
        });
        spotsc.setCellValueFactory(new PropertyValueFactory<>("nb_places"));
        spotsc.setCellFactory(column -> {
            return new TableCell<Event_details, Integer>() {
                private ProgressBar bar = new ProgressBar();

                {
                    bar.setPrefWidth(100);
                    bar.setStyle("-fx-accent: -color-fg-default;");

                }

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        int totalSpots = getTableView().getItems().get(getIndex()).getNb_total();
                        int availableSpots = totalSpots - item;
                        bar.setProgress((double) availableSpots / totalSpots);

                        // Create a label to display the number of available spots over the total spots
                        Label spotsLabel = new Label(availableSpots + "/" + totalSpots);
                        spotsLabel.setTextFill(Color.BLACK);

                        // Create a StackPane to hold the progress bar and the label
                        StackPane stack = new StackPane();
                        stack.getChildren().addAll(bar, spotsLabel);

                        setGraphic(stack);
                    }
                }
            };
        });

    }

    public void afficher1() throws SQLException {
        //afficher les events dans une list view list_events
        List<Event_details> events = eventDetailsService.getAll_now();
        ObservableList<Event_details> event_details = FXCollections.observableArrayList(events);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list_events);
        list_events.setItems(event_details);
        list_events.setCellFactory(param -> new ListCell<Event_details>() {
            @Override
            protected void updateItem(Event_details item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    // Create a custom layout for each item
                    VBox vbox = new VBox();
                    Label nameLabel = new Label("Name: " + item.getName());
                    Label typeLabel = new Label("Type: " + item.getType());
                    Label dateLabel = new Label("Date: " + item.getEvent_date());
                    Label durationLabel = new Label("Duration: " + item.getDuree() + " minutes");
                    //add the progress bar to the list view
                    ProgressBar bar = new ProgressBar();
                    bar.setStyle("-fx-accent: -color-fg-default;");
                    bar.setPrefWidth(100);
                    int totalSpots = item.getNb_total();
                    int availableSpots = totalSpots - item.getNb_places();
                    bar.setProgress((double) availableSpots / totalSpots);
                    Label spotsLabel = new Label(availableSpots + "/" + totalSpots);
                    spotsLabel.setTextFill(Color.BLACK);
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(bar, spotsLabel);
                    vbox.getChildren().addAll(nameLabel, typeLabel, dateLabel, durationLabel, stack);
                    setGraphic(vbox);
                }

            }

        });

    }

    @FXML
    void switch_to_table(ActionEvent event) {
        if (check_table.isSelected()) {
            event_detailsTableView.setVisible(true);
            list_events.setVisible(false);
        } else {
            event_detailsTableView.setVisible(false);
            list_events.setVisible(true);
        }

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

    /*public void delete_passed_events(List<Event_details> events) {
        try {
            for (Event_details event : events) {
                LocalDateTime eventDateTime = LocalDateTime.parse(event.getEvent_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (eventDateTime.isBefore(LocalDateTime.now())) {
                    eventDetailsService.delete(event.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    public void join_event(ActionEvent actionEvent) throws SQLException {
        Event_details selectedEvent = event_detailsTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            selectedEvent = list_events.getSelectionModel().getSelectedItem();
        }

        int id = 0;
        Date nextEventDate = getNextEventDate(0);
        System.out.println("Next event date: " + nextEventDate);
        Connection connection = MyDatabase.getInstance().getConnection();
        if (selectedEvent != null) {
            //check the date must be greater than the current date +1 hour
            if (check_event_date_and_time_passed(selectedEvent.getId())) {
                //delete_passed_events(event_details);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Event date and time has passed");
                alert.setContentText("Sorry, you cannot join an event that has already passed");
                afficher1();
                afficher();
                alert.showAndWait();

                return;
            }
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
                alert.setHeaderText("You are a participant in this event");
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
                    afficher1();
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

            // After the user has joined the event, generate the iCalendar data
            String icsData = generateICalendarData(selectedEvent);

            // Generate the QR code from the iCalendar data
            ByteArrayOutputStream qrCode = QRCode.from(icsData).to(ImageType.PNG).stream();

            // Display the QR code in an alert
            displayQRCodeAlert(qrCode, selectedEvent);

            afficher();
            afficher1();

            Join_notf();
        } catch (SQLException e) {
            //alert select an event to join
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No event selected");
            alert.setContentText("Please select an event to join");
            alert.showAndWait();

        }
    }

    private String generateICalendarData(Event_details event) {
        ICalendar ical = new ICalendar();
        VEvent vEvent = new VEvent();
        vEvent.setSummary(event.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(event.getEvent_date(), formatter);
        vEvent.setDateStart(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        int durationInMinutes = Integer.parseInt(event.getDuree());
        LocalDateTime endDateTime = dateTime.plusMinutes(durationInMinutes);
        vEvent.setDateEnd(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));

        // Set the location of the event
        vEvent.setLocation("GymPlus");

        ical.addEvent(vEvent);
        return Biweekly.write(ical).go();
    }

    private void displayQRCodeAlert(ByteArrayOutputStream qrCode, Event_details event) {
        try {
            // Convert the ByteArrayOutputStream to a javafx.scene.image.Image
            ByteArrayInputStream bis = new ByteArrayInputStream(qrCode.toByteArray());
            Image image = new Image(bis);

            // Create an ImageView to display the image
            ImageView imageView = new ImageView(image);

            // Set the dimensions of the ImageView
            imageView.setFitWidth(300); // Set the width to 300
            imageView.setFitHeight(300); // Set the height to 300

            // Create an Alert to display the ImageView
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("QR Code");
            alert.setHeaderText("Scan this QR code to add the event to your calendar");
            alert.setGraphic(imageView);

            // Create a "Share on Twitter" button
            ButtonType shareOnTwitterButton = new ButtonType("Share on Twitter");

            // Add the "Share on Twitter" button to the alert
            alert.getButtonTypes().add(shareOnTwitterButton);

            // Add an event filter to handle the "Share on Twitter" button click
            alert.getDialogPane().lookupButton(shareOnTwitterButton).addEventFilter(ActionEvent.ACTION, eventFilter -> {
                // Create a Twitter post
                createTwitterPost(event);


                eventFilter.consume();
            });


            alert.show();
        } catch (Exception e) {
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
        if(GlobalVar.getUser() == null) {
            return;
        }
        Date nextEventDate = getNextEventDate(GlobalVar.getUser().getId());
        if (nextEventDate != null) {
            Date now = new Date();
            long diff = nextEventDate.getTime() - now.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String countdown = String.format("%d days and %02d:%02d:%02d", diffDays, diffHours, diffMinutes, diffSeconds);
            countdownLabel.setText(diff >= 0 ? "Time until Your next event: " + countdown : "Time since last event: " + countdown);
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
        //in the list too
        ObservableList<Event_details> event_details = FXCollections.observableArrayList(searchResults);
        list_events.setItems(event_details);
        list_events.setCellFactory(param -> new ListCell<Event_details>() {
            @Override
            protected void updateItem(Event_details item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    // Create a custom layout for each item
                    VBox vbox = new VBox();
                    Label nameLabel = new Label("Name: " + item.getName());
                    Label typeLabel = new Label("Type: " + item.getType());
                    Label dateLabel = new Label("Date: " + item.getEvent_date());
                    Label durationLabel = new Label("Duration: " + item.getDuree());
                    //add the progress bar to the list view
                    ProgressBar bar = new ProgressBar();
                    bar.setStyle("-fx-accent: -color-fg-default;");
                    bar.setPrefWidth(100);
                    int totalSpots = item.getNb_total();
                    int availableSpots = totalSpots - item.getNb_places();
                    bar.setProgress((double) availableSpots / totalSpots);
                    Label spotsLabel = new Label(availableSpots + "/" + totalSpots);
                    spotsLabel.setTextFill(Color.BLACK);
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(bar, spotsLabel);
                    vbox.getChildren().addAll(nameLabel, typeLabel, dateLabel, durationLabel, stack);
                    setGraphic(vbox);
                }

            }

        });
    }

    @FXML
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
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points() - 2500));
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
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

                // Convert the QR code image to a DataSource
                DataSource qrCodeDataSource = new ByteArrayDataSource(bout.toByteArray(), "image/png");

                // Send the QR code via email
                sendEmailWithQRCode(GlobalVar.getUser().getEmail(), "Congratulations", "You have successfully claimed GymPlus Whey Protein get it by scanning it in the gym reception", qrCodeDataSource);
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
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points() - 1500));
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
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

                // Convert the QR code image to a DataSource
                DataSource qrCodeDataSource = new ByteArrayDataSource(bout.toByteArray(), "image/png");

                // Send the QR code via email
                sendEmailWithQRCode(GlobalVar.getUser().getEmail(), "Congratulations", "You have successfully claimed GymPlus Weightlifting Belt get it by scanning it in the gym reception ", qrCodeDataSource);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not enough points");
            alert.setContentText("You need at least 1500 points to claim this reward");
            alert.showAndWait();
        }
    }

    public void sendEmailWithQRCode(String to, String subject, String body, DataSource qrCodeDataSource) {
        new Thread(() -> {
            try {
                String from = "gymplus-noreply@grandelation.com";
                String password = "yzDvS_UoSL7b";
                String host = "mail.grandelation.com";

                // Setup mail server
                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.debug", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.port", "465");

                // Get the Session object.
                Session session = Session.getInstance(props, null);

                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject(subject);

                // Create a multipart message
                Multipart multipart = new MimeMultipart();

                // Create the message part
                MimeBodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText(body);

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(qrCodeDataSource));
                attachment.setFileName("qr-code.png");
                multipart.addBodyPart(attachment);

                // Send the complete message parts
                message.setContent(multipart);

                // Send message
                Transport.send(message, from, password);

                System.out.println("Sent message successfully....");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @FXML
    public void claim_bag(ActionEvent actionEvent) {
        if (GlobalVar.getUser().getEvent_points() >= 2000) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to claim GymPlus Gym Bag?");
            confirmationAlert.setContentText("Remaining Points: " + (GlobalVar.getUser().getEvent_points() - 2000));

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
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

                // Convert the QR code image to a DataSource
                DataSource qrCodeDataSource = new ByteArrayDataSource(bout.toByteArray(), "image/png");

                // Send the QR code via email
                sendEmailWithQRCode(GlobalVar.getUser().getEmail(), "Congratulations", "You have successfully claimed GymPlus Bag get it by scanning it in the gym reception ", qrCodeDataSource);

                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("Congratulations");
                qrCodeAlert.setHeaderText("You have successfully claimed GymPlus Gym Bag");

                ImageView qrImageView = new ImageView(qrImage);
                qrCodeAlert.setGraphic(qrImageView);
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

    @FXML
    void go_to_history(ActionEvent event) {

            FadeOutRight f = new FadeOutRight(event_pane);
            f.setOnFinished((e) -> {
                event_pane.setVisible(false);
                FadeInRight f2 = new FadeInRight(history);
                history.setOpacity(0);
                history.setVisible(true);
                f2.play();
            });
            f.play();

    }

    public void afficher_history() throws SQLException {
        //show the events that the user participated in the past using getPastEventsByUserId
        List<Event_details> events = eventDetailsService.getPastEventsByUserId(GlobalVar.getUser().getId());
        ObservableList<Event_details> event_details = FXCollections.observableArrayList(events);
        list_events1.setItems(event_details);
        list_events1.setCellFactory(param -> new ListCell<Event_details>() {
            @Override
            protected void updateItem(Event_details item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    // Create a custom layout for each item
                    VBox vbox = new VBox();
                    Label nameLabel = new Label("Name: " + item.getName());
                    Label typeLabel = new Label("Type: " + item.getType());
                    Label dateLabel = new Label("Date: " + item.getEvent_date());
                    Label durationLabel = new Label("Duration: " + item.getDuree());
                    vbox.getChildren().addAll(nameLabel, typeLabel, dateLabel, durationLabel);
                    setGraphic(vbox);
                }

            }

        });
    }
    @FXML
    public void rate_event(ActionEvent actionEvent) {
        Event_details selectedEvent = list_events1.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Rate Event");
            dialog.setHeaderText("Rate " + selectedEvent.getName());

            Rating ratingControl = new Rating();
            ratingControl.setMax(5);
            dialog.getDialogPane().setContent(ratingControl);

            // Create a custom ButtonType for confirmation
            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return (int) ratingControl.getRating();
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()) {
                int rating = result.get();
                Event_participantsService eventParticipantsService = new Event_participantsService();
                Event_participants eventParticipants = eventParticipantsService.getByEventIdAndUserId(selectedEvent.getId(), GlobalVar.getUser().getId());
                eventParticipants.setRate(rating);
                try {
                    eventParticipantsService.update(eventParticipants);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Rating submitted");
                alert.setContentText("Thank you for rating " + selectedEvent.getName());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No event selected");
            alert.setContentText("Please select an event to rate");
            alert.showAndWait();
        }
    }
    public void createTwitterPost(Event_details event) {
        String message = "I just joined the event " + event.getName() + " on " + event.getEvent_date() + "! Join me there!";
        try {
            String urlEncodedMessage = URLEncoder.encode(message, "UTF-8");
            String url = "https://twitter.com/intent/tweet?text=" + urlEncodedMessage;

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back_toeventsss(ActionEvent event){
        FadeOutRight f = new FadeOutRight(history);
        f.setOnFinished((e) -> {
            history.setVisible(false);
            FadeInRight f2 = new FadeInRight(event_pane); // Apply the animation to event_pane
            event_pane.setOpacity(0);
            event_pane.setVisible(true);
            f2.play();
        });
        f.play();
    }


}













