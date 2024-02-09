package services.gestionevents;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import entities.gestionevents.Event_details;


public class Event_detailsService implements IService<Event_details> {
    private Connection connection;
    public Event_detailsService(){

    connection= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Event_details eventDetails) throws SQLException {
        String query="INSERT INTO event_details (name, type, event_date, duree) VALUES (?,?,?,?)";
        PreparedStatement ps= connection.prepareStatement(query);
        ps.setString(1, eventDetails.getName());
        ps.setString(2, eventDetails.getType());
        ps.setString(3, eventDetails.getEvent_date());
        ps.setInt(4, eventDetails.getDuree());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
    String query="DELETE FROM event_details WHERE id=?";
    PreparedStatement ps= connection.prepareStatement(query);
    ps.setInt(1, id);
    ps.executeUpdate();
    }

    @Override
    public void update(Event_details eventDetails) throws SQLException {
    String query="UPDATE event_details SET name=?, type=?, event_date=?, duree=? WHERE id=?";
    PreparedStatement ps= connection.prepareStatement(query);
    ps.setString(1, eventDetails.getName());
    ps.setString(2, eventDetails.getType());
    ps.setString(3, eventDetails.getEvent_date());
    ps.setInt(4, eventDetails.getDuree());
    ps.setInt(5, eventDetails.getId());
    ps.executeUpdate();
    }

    @Override
    public List<Event_details> getAll() throws SQLException {
        List<Event_details> eventDetailsList= new ArrayList<>();
        String query="SELECT * FROM event_details";
        Statement st= connection.createStatement();
        ResultSet rs= st.executeQuery(query);
        while(rs.next()){
            Event_details eventDetails= new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getInt("duree"));
            eventDetails.setId(rs.getInt("id"));
            eventDetailsList.add(eventDetails);


        }
    return eventDetailsList;}
}
