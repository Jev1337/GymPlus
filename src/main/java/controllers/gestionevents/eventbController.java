package controllers.gestionevents;

import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import entities.gestionevents.Event_details;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import services.gestionevents.Event_detailsService;
import services.gestionevents.Event_participantsService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class eventbController {
    private final Event_detailsService eventDetailsService = new Event_detailsService();
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
    private TextField eventdate_id;

    @FXML
    private TextField eventduree_id;

    @FXML
    private TextField eventname_id;

    @FXML
    private TextField eventtype_id;

    @FXML
    private TableView<Event_details> tableevents_id;
    @FXML
    private TextField editeventname_id;
    @FXML
    private TextField editeventtype_id;
    @FXML
    private TextField editeventdate_id;
    @FXML
    private TextField editeventduration_id;
    @FXML
    TableView<ObservableList<String>> ListParticipants_id;

    @FXML
    void add_event(ActionEvent event) {
        try {
            Event_detailsService eventDetailsService = new Event_detailsService();
            Event_details eventDetails = new Event_details();
            eventDetails.setName(eventname_id.getText());
            eventDetails.setType(eventtype_id.getText());
            eventDetails.setEvent_date(eventdate_id.getText());
            eventDetails.setDuree(eventduree_id.getText());
            eventDetailsService.add(eventDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void delete_event(ActionEvent event) {
        try {
            Event_detailsService eventDetailsService = new Event_detailsService();
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                eventDetailsService.delete(selectedEvent.getId());
                afficher();
            } else {
                System.out.println("No event selected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void edit_event(ActionEvent event) {
        Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            editeventname_id.setText(selectedEvent.getName());
            editeventtype_id.setText(selectedEvent.getType());
            editeventdate_id.setText(selectedEvent.getEvent_date());
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

    void afficher() throws SQLException {
        try {
            List<Event_details> events = eventDetailsService.getAll();
            ObservableList<Event_details> event_details = FXCollections.observableArrayList(events);

            tableevents_id.setItems(event_details);

            event_idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            event_namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
            event_typecol.setCellValueFactory(new PropertyValueFactory<>("type"));
            event_datecol.setCellValueFactory(new PropertyValueFactory<>("event_date"));
            event_durationcol.setCellValueFactory(new PropertyValueFactory<>("duree"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void edit_event_confirm(ActionEvent event) {
        try {
            Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                Event_detailsService eventDetailsService = new Event_detailsService();
                Event_details eventDetails = new Event_details();
                eventDetails.setId(selectedEvent.getId());
                eventDetails.setName(editeventname_id.getText());
                eventDetails.setType(editeventtype_id.getText());
                eventDetails.setEvent_date(editeventdate_id.getText());
                eventDetails.setDuree(editeventduration_id.getText());
                eventDetailsService.update(eventDetails);
                afficher();
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


    void fillParticipants() {
        Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
        Event_participantsService event_Participants = new Event_participantsService();
        if (selectedEvent != null) {
            try {
                List<String> participants = event_Participants.getParticipants(selectedEvent.getId());
                if (participants.isEmpty()) {
                    ListParticipants_id.setVisible(false);
                } else {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ListParticipants_id.setVisible(false);
            System.out.println("No event selected");
        }
    }
    @FXML
    void kick_user(ActionEvent event) {
           Event_details selectedEvent = tableevents_id.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                Event_participantsService event_Participants = new Event_participantsService();
                ObservableList<String> selectedParticipant = ListParticipants_id.getSelectionModel().getSelectedItem();
                if (selectedParticipant != null) {
                    try {
                        event_Participants.delete(selectedEvent.getId(), selectedParticipant.get(0));
                        fillParticipants();
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
    void initialize() {
        try {
            afficher();
            ListParticipants_id.setVisible(false);

            tableevents_id.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    fillParticipants();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}