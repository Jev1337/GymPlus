package controllers.gestionevents;

import entities.gestionevents.Event_details;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.w3c.dom.events.Event;

import java.awt.event.MouseEvent;

public class  Event_affController {

    @FXML
    private Label date_id;

    @FXML
    private Label duration_id;

    @FXML
    private Label id_id;

    @FXML
    private Label name_id;

    @FXML
    private Label spots_id;

    @FXML
    private Label type_id;
    @FXML
    private Pane selected;
    private static eventbController eventbController;
    public void setEventController(eventbController eventbController) {
        Event_affController.eventbController = eventbController;
    }
    public void setdata(Event_details eventDetails) {
        id_id.setText(String.valueOf(eventDetails.getId()));
        name_id.setText(eventDetails.getName());
        type_id.setText(eventDetails.getType());
        date_id.setText(eventDetails.getEvent_date());
        duration_id.setText(eventDetails.getDuree());
        spots_id.setText(String.valueOf(eventDetails.getNb_places()+"/"+eventDetails.getNb_total()));
        setEventController(this.eventbController);

    }
    public Event_details getdata()
    {
        Event_details eventDetails = new Event_details();
        eventDetails.setId(id_id.getText().isEmpty() ? 0 : Integer.parseInt(id_id.getText()));
        eventDetails.setName(name_id.getText());
        eventDetails.setType(type_id.getText());
        eventDetails.setEvent_date(date_id.getText());
        eventDetails.setDuree(duration_id.getText());
        eventDetails.setNb_places(Integer.parseInt(spots_id.getText().split("/")[0]));
        eventDetails.setNb_total(Integer.parseInt(spots_id.getText().split("/")[1]));
        return eventDetails;

    }







}

