package entities.gestionevents;

public class Event_participants {

    private int event_id;
    private int participant_id;
    private int rate;
    public Event_participants(int event_id, int participant_id) {
        this.event_id = event_id;
        this.participant_id = participant_id;
    }



    public int getEvent_id() {
        return event_id;
    }

    public int getParticipant_id() {
        return participant_id;
    }



    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public int getRate() {
        return rate;
    }
    @Override
    public String toString() {
        return "Event_participants [ event_id=" + event_id + ", participant_id=" + participant_id + "]";
    }

}
