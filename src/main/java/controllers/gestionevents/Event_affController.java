package controllers.gestionevents;

import entities.gestionevents.Event_details;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import services.gestionevents.Event_participantsService;

import java.util.List;


public class Event_affController {

    @FXML
    private Label date;

    @FXML
    private Label duration;

    @FXML
    private Label id_event;

    @FXML
    private Label name;

    @FXML
    private Label spots;

    @FXML
    private Label type;
    Event_details eventt=new Event_details();

    public void get_event(Event_details e)
    {   eventt=e;
        id_event.setText(String.valueOf(e.getId()));
        name.setText(e.getName());
        type.setText(e.getType());
        date.setText(e.getEvent_date());
        duration.setText(e.getDuree());
        spots.setText(String.valueOf(e.getNb_places()));
    }



}