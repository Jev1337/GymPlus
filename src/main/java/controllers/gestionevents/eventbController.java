package controllers.gestionevents;

import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import entities.gestionevents.Event_details;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.gestionevents.Event_detailsService;
import services.gestionevents.Event_participantsService;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class eventbController {
    private final Event_detailsService eventDetailsService = new Event_detailsService();
    private PreparedStatement getUsernameStatement;
    private PreparedStatement getPointsStatement;
    @FXML
    private Pane affichage_events_adstaff;
    @FXML
    private Pane editevent_id;

    @FXML
    private Button ajouter_id;

    @FXML
    private Button delete_id;

    @FXML
    private Button edit_id;

    @FXML
    private TableColumn<Event_details, String> event_datecol;

    @FXML
    private TableColumn<Event_details, String> event_durationcol;

    @FXML
    private TableColumn<Event_details, String> event_idcol;

    @FXML
    private TableColumn<Event_details, String> event_namecol;

    @FXML
    private TableColumn<Event_details, String> event_typecol;
    @FXML private TableColumn<ObservableList<String>, String> username_id;
    @FXML private TableColumn<ObservableList<String>, String> firstname_id;
    @FXML private TableColumn<ObservableList<String>, String> lastname_id;

    @FXML
    private TextField eventdate_id1;

    @FXML
    private TextField eventduree_id;

    @FXML
    private TextField eventname_id;

    @FXML
    private TextField eventtype_id;
    @FXML
    private TextField eventspots_id;
    @FXML
    private TableView<Event_details> tableevents_id;
    @FXML
    private TextField editeventname_id;
    @FXML
    private TextField editeventtype_id;
    @FXML
    private DatePicker editeventdate_id;
    @FXML
    private TextField editeventduration_id;
    @FXML
    TableView<ObservableList<String>> ListParticipants_id;
    @FXML
    TableColumn<Event_details, String> event_spotscol;
    @FXML
    TableView<ObservableList<String>> list_points;
    @FXML
    TableColumn<ObservableList<String>, String> username_id1;
    @FXML
    TableColumn<ObservableList<String>, String> points_id1;

    @FXML
    private DatePicker eventdate_id;
    @FXML
    private TextField editeventdate_id1;
    @FXML
    private ListView<Event_details> eventList;
    @FXML
    private CheckBox table_view;
    @FXML
    private Button kick_id;
    @FXML
    private Button editeventconfirm_id1;




    public eventbController() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            getUsernameStatement = connection.prepareStatement("SELECT username FROM user where id=?");
            getPointsStatement = connection.prepareStatement("SELECT event_points FROM user where id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void add_event(ActionEvent event) {
        eventdate_id.getEditor().setDisable(true);
        try {

            // Input validation
            if (eventname_id.getText().isEmpty() || eventtype_id.getText().isEmpty() || eventdate_id.getValue() == null || eventdate_id1.getText().isEmpty() || eventduree_id.getText().isEmpty() || eventspots_id.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }

            // Check if DatePicker is empty
            if (eventdate_id.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Please select a date.");
                return;
            }

            // Check if date is valid
            try {
                LocalDateTime eventDateTime = LocalDateTime.of(eventdate_id.getValue(),
                        LocalTime.parse(eventdate_id1.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                if (eventDateTime.isBefore(LocalDateTime.now().plusHours(1))) {
                    showAlert(Alert.AlertType.ERROR, "Event date and time should be at least one hour in the future.");
                    return;
                }
            } catch (DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid date or time format.");
                return;
            }

            if (!eventtype_id.getText().matches("[a-zA-Z]+")) {
                showAlert(Alert.AlertType.ERROR, "Type should be alphabets only.");
                return;
            }

            if (!eventdate_id1.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                showAlert(Alert.AlertType.ERROR, "Hour should be in HH:mm format.");
                return;
            }

            int nbPlaces;
            try {
                nbPlaces = Integer.parseInt(eventspots_id.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Number of places should be a valid number.");
                return;
            }
            if (nbPlaces <= 0) {
                showAlert(Alert.AlertType.ERROR, "Number of places should be > 0.");
                return;
            }

            int duration;
            try {
                duration = Integer.parseInt(eventduree_id.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Duration should be a valid number.");
                return;
            }
            if (duration < 10 || duration > 150) {
                showAlert(Alert.AlertType.ERROR, "Duration should be between 10 and 150.");
                return;
            }

            // If all validations pass, proceed with creating the event
            Event_detailsService eventDetailsService = new Event_detailsService();
            Event_details eventDetails = new Event_details();
            eventDetails.setName(eventname_id.getText());
            eventDetails.setType(eventtype_id.getText());
            eventDetails.setEvent_date(eventdate_id.getValue().toString() + " " + eventdate_id1.getText() + ":00");
            eventDetails.setDuree(eventduree_id.getText());
            eventDetails.setNb_places(nbPlaces);
            eventDetailsService.add(eventDetails);

            // Clear fields
            eventname_id.clear();
            eventtype_id.clear();
            eventdate_id.getEditor().clear();
            eventdate_id1.clear();
            eventduree_id.clear();
            eventspots_id.clear();
            delete_passed_events(eventDetailsService.getAll());
            afficher();
            afficher1();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void delete_event(ActionEvent event) {
        try {
            Event_detailsService eventDetailsService = new Event_detailsService();
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if(selectedEvent==null)
            {
                selectedEvent=eventList.getSelectionModel().getSelectedItem();
            }
            if (selectedEvent != null) {
                eventDetailsService.delete(selectedEvent.getId());
                delete_passed_events(eventDetailsService.getAll());
                afficher();
                afficher1();
                fillParticipants();
                fillParticipants1();
            } else {
                System.out.println("No event selected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void edit_event_cancel(ActionEvent event) {
        FadeOutRight f = new FadeOutRight(editevent_id);
        f.setOnFinished((e) -> {
            editevent_id.setVisible(false);
            FadeInRight f2 = new FadeInRight(affichage_events_adstaff);
            affichage_events_adstaff.setOpacity(0);
            affichage_events_adstaff.setVisible(true);
            f2.play();
        });
        f.play();
    }
    @FXML
    void edit_event(ActionEvent event) {
        Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
        if(selectedEvent==null)
        {
            selectedEvent=eventList.getSelectionModel().getSelectedItem();
        }
        if (selectedEvent != null) {
            editeventname_id.setText(selectedEvent.getName());
            editeventtype_id.setText(selectedEvent.getType());
            //date only without hour  in date picker editevent_date_id and hour in textfield editevent_date_id1
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(selectedEvent.getEvent_date(), formatter);

            editeventdate_id.setValue(dateTime.toLocalDate());
            editeventdate_id1.setText(dateTime.toLocalTime().toString());


            editeventduration_id.setText(selectedEvent.getDuree());
            FadeOutRight f = new FadeOutRight(affichage_events_adstaff);
            f.setOnFinished((e) -> {
                affichage_events_adstaff.setVisible(false);
                FadeInRight f2 = new FadeInRight(editevent_id);
                editevent_id.setOpacity(0);
                editevent_id.setVisible(true);
                f2.play();
            });
            f.play();
        } else {
            System.out.println("No event selected");
        }
    }
    @FXML
    void show_table(ActionEvent event) {
        if (table_view.isSelected()) {
            eventList.setVisible(false);
            tableevents_id.setVisible(true);
        } else {
            eventList.setVisible(true);
            tableevents_id.setVisible(false);
        }

    }

    public void afficher1() {



        try {
            delete_passed_events(eventDetailsService.getAll());
            List<Event_details> events = eventDetailsService.getAll();
            ObservableList<Event_details> data = FXCollections.observableArrayList(events);

            // Set a custom cell factory for your ListView
            eventList.setCellFactory(param -> new ListCell<Event_details>() {
                @Override
                protected void updateItem(Event_details item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Create a custom layout for each item
                        VBox vbox = new VBox();
                        Label idLabel = new Label("ID: " + item.getId());
                        Label nameLabel = new Label("Name: " + item.getName());
                        Label typeLabel = new Label("Type: " + item.getType());
                        Label dateLabel = new Label("Date: " + item.getEvent_date());
                        Label durationLabel = new Label("Duration: " + item.getDuree());
                        Label spotsLabel = new Label("Spots: " + item.getNb_places() + "/" + item.getNb_total());

                        vbox.getChildren().addAll(nameLabel, typeLabel, dateLabel, durationLabel, spotsLabel);

                        // Set the custom layout as the graphic of the cell
                        setGraphic(vbox);
                    }
                }
            });

            eventList.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void afficher() {

        try {

                delete_passed_events(eventDetailsService.getAll());

            List<Event_details> events = eventDetailsService.getAll();
            ObservableList<Event_details> data = FXCollections.observableArrayList(events);
            event_idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            event_namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
            event_typecol.setCellValueFactory(new PropertyValueFactory<>("type"));
            event_datecol.setCellValueFactory(new PropertyValueFactory<>("event_date"));
            event_durationcol.setCellValueFactory(new PropertyValueFactory<>("duree"));

            event_spotscol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Event_details, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Event_details, String> param) {
                    Event_details event = param.getValue();
                    String ratio = String.valueOf(event.getNb_places()) + "/" + String.valueOf(event.getNb_total());
                    return new SimpleStringProperty(ratio);
                }
            });
            tableevents_id.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();


        }
    }
    // a function to delete the events that the time has passed
    public void delete_passed_events(List<Event_details> events) {
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
    }
    @FXML
    void edit_event_confirm(ActionEvent event) {
        try {
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if(selectedEvent == null) {
                selectedEvent = eventList.getSelectionModel().getSelectedItem();
            }
            if (selectedEvent != null) {
                // Input validation
                if (editeventname_id.getText().isEmpty() || editeventtype_id.getText().isEmpty() || editeventdate_id.getValue() == null || editeventdate_id1.getText().isEmpty() || editeventduration_id.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                    return;
                }

                if (!editeventtype_id.getText().matches("[a-zA-Z]+")) {
                    showAlert(Alert.AlertType.ERROR, "Type should be alphabets only.");
                    return;
                }

                if (!editeventdate_id1.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                    showAlert(Alert.AlertType.ERROR, "Hour should be in HH:mm format.");
                    return;
                }
                if(!editeventdate_id.getValue().isAfter(LocalDate.now()))
                {
                    showAlert(Alert.AlertType.ERROR, "Date should be in the future.");
                    return;
                }

                int duration;
                try {
                    duration = Integer.parseInt(editeventduration_id.getText());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Duration should be a valid number.");
                    return;
                }
                if (duration < 10 || duration > 150) {
                    showAlert(Alert.AlertType.ERROR, "Duration should be between 10 and 150.");
                    return;
                }

                // If all validations pass, proceed with updating the event
                Event_detailsService eventDetailsService = new Event_detailsService();
                Event_details eventDetails = new Event_details();
                eventDetails.setId(selectedEvent.getId());
                eventDetails.setName(editeventname_id.getText());
                eventDetails.setType(editeventtype_id.getText());
                eventDetails.setEvent_date(editeventdate_id.getValue().toString() + " " + editeventdate_id1.getText() + ":00");
                eventDetails.setDuree(editeventduration_id.getText());
                eventDetails.setNb_places(selectedEvent.getNb_places());
                eventDetailsService.update(eventDetails);
                afficher();
                afficher1();
                FadeOutRight f = new FadeOutRight(editevent_id);
                f.setOnFinished((e) -> {
                    editevent_id.setVisible(false);
                    FadeInRight f2 = new FadeInRight(affichage_events_adstaff);
                    affichage_events_adstaff.setOpacity(0);
                    affichage_events_adstaff.setVisible(true);
                    f2.play();
                });
                f.play();
            } else {
                System.out.println("No event selected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



        void fillParticipants () {
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();

            Event_participantsService event_Participants = new Event_participantsService();
            if (selectedEvent != null) {
                try {
                    List<String> participants = event_Participants.getParticipants(selectedEvent.getId());
                    if (participants.isEmpty() || participants == null) {
                        ListParticipants_id.setVisible(false);
                        kick_id.setVisible(false);
                    } else {
                        kick_id.setVisible(true);
                        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                        for (int i = 0; i < participants.size(); i += 3) {
                            ObservableList<String> row = FXCollections.observableArrayList();
                            row.add(participants.get(i));
                            row.add(participants.get(i + 1));
                            row.add(participants.get(i + 2));
                            data.add(row);
                        }

                        username_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
                        firstname_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
                        lastname_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));

                        ListParticipants_id.setItems(data);
                        ListParticipants_id.setVisible(true);
                        kick_id.setVisible(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ListParticipants_id.setVisible(false);
                kick_id.setVisible(false);
            System.out.println("No event selected");
            }
        }
    void fillParticipants1 () {
        Event_details selectedEvent = eventList.getSelectionModel().getSelectedItem();

        Event_participantsService event_Participants = new Event_participantsService();
        if (selectedEvent != null) {
            try {
                List<String> participants = event_Participants.getParticipants(selectedEvent.getId());
                if (participants.isEmpty() || participants == null) {
                    ListParticipants_id.setVisible(false);
                    kick_id.setVisible(false);
                } else {
                    kick_id.setVisible(true);
                    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                    for (int i = 0; i < participants.size(); i += 3) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        row.add(participants.get(i));
                        row.add(participants.get(i + 1));
                        row.add(participants.get(i + 2));
                        data.add(row);
                    }

                    username_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
                    firstname_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
                    lastname_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));

                    ListParticipants_id.setItems(data);
                    ListParticipants_id.setVisible(true);
                    kick_id.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ListParticipants_id.setVisible(false);
            kick_id.setVisible(false);
            System.out.println("No event selected");
        }
    }
        @FXML
        void kick_user (ActionEvent event){
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if(selectedEvent==null)
            {
                selectedEvent=eventList.getSelectionModel().getSelectedItem();
            }
            if (selectedEvent != null) {
                Event_participantsService event_Participants = new Event_participantsService();
                ObservableList<String> selectedParticipant = ListParticipants_id.getSelectionModel().getSelectedItem();
                if (selectedParticipant != null) {
                    try {
                        event_Participants.delete(selectedEvent.getId(), selectedParticipant.get(0));
                        //spots +1
                        eventDetailsService.updatespots(selectedEvent.getId());
                        fillParticipants();
                        afficher();
                        afficher1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("No participant selected");
                }
            } else {
                System.out.println("No event selected");
            }

        }

        @FXML
        void initialize () {
            afficher();
            afficher1();
            fill_points();
            ListParticipants_id.setVisible(false);
            kick_id.setVisible(false);
            eventdate_id.getEditor().setDisable(true);
            editeventdate_id.getEditor().setDisable(true);

            tableevents_id.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    fillParticipants();


                }
            });
            eventList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    fillParticipants1();
                }
            });
        }

        public int get_points ( int user_id) throws SQLException {
            getPointsStatement.setInt(1, user_id);
            ResultSet rs = getPointsStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("event_points");
            }
            return 0;
        }
        public String get_username ( int user_id) throws SQLException {
            getUsernameStatement.setInt(1, user_id);
            ResultSet rs = getUsernameStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
            return "";
        }

        @FXML
        void fill_points () {
            try {

                Connection connection = MyDatabase.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT id FROM user WHERE role='client'");
                ResultSet rs = stmt.executeQuery();


                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();


                while (rs.next()) {

                    int userId = rs.getInt("id");


                    String username = get_username(userId);
                    int points = get_points(userId);


                    ObservableList<String> row = FXCollections.observableArrayList();


                    row.add(username);
                    row.add(String.valueOf(points));


                    data.add(row);
                }

                username_id1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
                points_id1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));

                list_points.setItems(data);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


}