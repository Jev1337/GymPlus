package controllers.gestionevents;

import controllers.gestionuser.GlobalVar;
import entities.gestionevents.Event_details;
import entities.gestionevents.Event_participants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.gestionevents.Event_detailsService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Date;
import java.time.ZoneId;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import utils.MyDatabase;
import services.gestionevents.Event_participantsService;

public class eventfController {

    @FXML
    private TableColumn<Event_details, String> datec;

    @FXML
    private TableColumn<Event_details, String> durationc;


    @FXML
    private Button join_id;

    @FXML
    private TableColumn<Event_details,String> namec;

    @FXML
    private TableColumn<Event_details, String> typec;

    private final Event_detailsService eventDetailsService=new Event_detailsService();
    @FXML
    private TableView<Event_details> event_detailsTableView;

    @FXML
    private Label countdownLabel;

    private Timeline timeline;
    @FXML
    void initialize() {
        try {
            List<Event_details> events = eventDetailsService.getAll();
            ObservableList<Event_details> observableList = FXCollections.observableList(events);
            event_detailsTableView.setItems(observableList);

            namec.setCellValueFactory(new PropertyValueFactory<>("name"));
            typec.setCellValueFactory(new PropertyValueFactory<>("type"));
            datec.setCellValueFactory(new PropertyValueFactory<>("event_date"));
            durationc.setCellValueFactory(new PropertyValueFactory<>("duree"));
            startCountdown();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean hasUserJoinedEvent(int event_id,int user_id) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement("select * from event_participants where event_details_id = ? and user_id = ?");
        stmt.setInt(1, event_id);
        stmt.setInt(2, GlobalVar.getUser().getId());
        ResultSet rs = stmt.executeQuery();
        return rs.next();

    }
    @FXML
    public void join_event(ActionEvent actionEvent) throws SQLException {
        Event_details selectedEvent = event_detailsTableView.getSelectionModel().getSelectedItem();
        int id = 0;
        Date nextEventDate = getNextEventDate(0);
        System.out.println("Next event date: " + nextEventDate);
        Connection connection = MyDatabase.getInstance().getConnection();
        if (selectedEvent != null) {
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
                    return;
                }
                else {
                    return;
                }


            }




            System.out.println("Join event: " + name + ", " + type + ", " + date + ", " + duration);
            try {
                // Assuming you have a Connection object named "conn"

                PreparedStatement stmt = connection.prepareStatement("SELECT id FROM event_details WHERE name = ? AND type = ? AND event_date = ? AND duree = ?");
                stmt.setString(1, selectedEvent.getName());
                stmt.setString(2, selectedEvent.getType());
                stmt.setString(3, selectedEvent.getEvent_date());
                stmt.setString(4, selectedEvent.getDuree());

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
        //ajouter le participant a cette evenement
        Event_participants ev = new Event_participants(id,GlobalVar.getUser().getId() );
        try {
            Event_participantsService eventParticipantsService = new Event_participantsService();
            eventParticipantsService.add(ev);

        } catch (SQLException e) {
            e.printStackTrace();


        }


    }
    public Date getNextEventDate(int userId) {
        LocalDateTime nextEventDate = null;
        Connection connection = MyDatabase.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT event_date FROM event_details " +
                            "JOIN event_participants ON event_details.id = event_participants.event_details_id " +
                            "WHERE event_participants.user_id = ? AND event_date > NOW() " +
                            "ORDER BY event_date ASC LIMIT 1"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
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



}





