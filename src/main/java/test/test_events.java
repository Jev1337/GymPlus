package test;

import entities.gestionevents.Event_details;
import entities.gestionevents.Event_participants;
import services.gestionevents.Event_detailsService;
import services.gestionevents.Event_participantsService;


public class test_events {
    public static void main(String[] args) {
    Event_details event=new Event_details("event1", "type1", "2021-12-12", 2);
        Event_detailsService eventService= new Event_detailsService();
        System.out.println("ajout event1");
        try {
            eventService.add(event);
            System.out.println("event1 ajouté");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("liste des events");
        try {
            for (Event_details e : eventService.getAll()) {
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ajout eventparticipants");
        Event_participants eventParticipants= new Event_participants(1, 1);
        Event_participantsService eventParticipantsService= new Event_participantsService();




    }
}
