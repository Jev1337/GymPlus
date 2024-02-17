package entities.gestionevents;

public class Event_details {
    private int id;
    private String name;
    private String type;
    private String event_date;
    private String duree;

    public Event_details(String name, String type, String event_date, String duree) {
        this.name = name;
        this.type = type;
        this.event_date = event_date;
        this.duree = duree;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getEvent_date() {
        return event_date;
    }
    public String getDuree() {
        return duree;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
    public void setDuree(String duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Event_details [id=" + id + ", name=" + name + ", type=" + type + ", event_date=" + event_date + ", duree="
                + duree + "]";
    }

}
