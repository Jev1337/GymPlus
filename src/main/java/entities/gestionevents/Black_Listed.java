package entities.gestionevents;

import java.sql.Date;

public class Black_Listed {
    private int id_user;
    private Date start_ban;
    private Date end_ban;

    public Black_Listed(int id_user, Date start_ban, Date end_ban) {
        this.id_user = id_user;
        this.start_ban = start_ban;
        this.end_ban = end_ban;
    }
    public Black_Listed()
    {}
    public int getId_user() {
        return id_user;
    }
    public Date getStart_ban() {
        return start_ban;
    }
    public Date getEnd_ban() {
        return end_ban;
    }
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    public void setStart_ban(Date start_ban) {
        this.start_ban = start_ban;
    }
    public void setEnd_ban(Date end_ban) {
        this.end_ban = end_ban;
    }



}
