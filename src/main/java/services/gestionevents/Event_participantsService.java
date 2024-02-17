package services.gestionevents;

import entities.gestionevents.Event_participants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import services.IService;
import utils.MyDatabase;

public class Event_participantsService implements IService<Event_participants> {
    private Connection connection;
    public Event_participantsService(){

        connection= MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Event_participants eventParticipants) throws SQLException {
        String query="INSERT INTO event_participants (event_details_id, user_id) VALUES (?,?)";
        PreparedStatement ps= connection.prepareStatement(query);
        ps.setInt(1, eventParticipants.getEvent_id());
        ps.setInt(2, eventParticipants.getParticipant_id());

        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
    String query="DELETE FROM event_participants WHERE event_details_id=?";
    PreparedStatement ps= connection.prepareStatement(query);
    ps.setInt(1, id);
    ps.executeUpdate();

    }

    @Override
    public void update(Event_participants eventParticipants) throws SQLException {
    String query="UPDATE event_participants SET event_details_id=?, user_id=? WHERE id=?";
    PreparedStatement ps= connection.prepareStatement(query);
    ps.setInt(1, eventParticipants.getEvent_id());
    ps.setInt(2, eventParticipants.getParticipant_id());
    ps.setInt(3, eventParticipants.getId());
    ps.executeUpdate();

    }

    @Override
    public List<Event_participants> getAll() throws SQLException {
        List<Event_participants> eventParticipantsList= new ArrayList<>();
        String query="SELECT * FROM event_participants";
        PreparedStatement ps= connection.prepareStatement(query);
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
            Event_participants eventParticipants= new Event_participants(rs.getInt("event_details_id"), rs.getInt("user_id"));
            eventParticipants.setId(rs.getInt("id"));
            eventParticipantsList.add(eventParticipants);
        }

        return eventParticipantsList;

    }
    public List<String> getParticipants(int event_id) throws SQLException {
        List<String> participantsList= new ArrayList<>();
        String query="Select username , firstname, lastname from user u join event_participants ep on u.id=ep.user_id where ep.event_details_id=?";
        PreparedStatement ps= connection.prepareStatement(query);
        ps.setInt(1, event_id);
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
            participantsList.add(rs.getString("username"));
            participantsList.add(rs.getString("firstname"));
            participantsList.add(rs.getString("lastname"));
        }
        return participantsList;
    }
}
