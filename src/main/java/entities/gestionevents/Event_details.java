package entities.gestionevents;

public class Event_details {
    private int id;
    private String name;
    private String type;
    private String event_date;
    private String duree;
    private int nb_places;
    private int nb_total;

    public Event_details(String name, String type, String event_date, String duree,int nb_places,int nb_total) {
        this.name = name;
        this.type = type;
        this.event_date = event_date;
        this.duree = duree;
        this.nb_places=nb_places;
        this.nb_total=nb_total;
    }
    public Event_details()
    {}
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
    public void setNb_places(int nb_places) {
        this.nb_places = nb_places;
    }
    public int getNb_places() {
        return nb_places;
    }
    public void setNb_total(int nb_total) {
        this.nb_total = nb_total;
    }
    public int getNb_total() {
            return nb_total;
        }

    @Override
    public String toString() {
        return "Event_details [id=" + id + ", name=" + name + ", type=" + type + ", event_date=" + event_date + ", duree="
                + duree + ", nb_places=" + nb_places + ", nb_total=" + nb_total +"]";
    }

}
