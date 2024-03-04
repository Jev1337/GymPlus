package services.gestionevents;

import entities.gestionevents.Event_details;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Event_detailsService implements IService<Event_details> {
    private Connection connection;

    public Event_detailsService() {

        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Event_details eventDetails) throws SQLException {
        //check if event date and type are taken
        String query1 = "SELECT * From event_details where event_date=? and type=?";
        PreparedStatement ps1 = connection.prepareStatement(query1);
        ps1.setString(1, eventDetails.getEvent_date());
        ps1.setString(2, eventDetails.getType());
        ResultSet rs = ps1.executeQuery();
        if (rs.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot Add two Events with the same date and type");
            alert.showAndWait();
            return;
        }
        String query = "INSERT INTO event_details (name, type, event_date, duree,nb_places,nb_total) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, eventDetails.getName());
        ps.setString(2, eventDetails.getType());
        ps.setString(3, eventDetails.getEvent_date());
        ps.setString(4, eventDetails.getDuree());
        ps.setInt(5, eventDetails.getNb_places());
        ps.setInt(6, eventDetails.getNb_places());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM event_details WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public void update(Event_details eventDetails) throws SQLException {
        String query = "UPDATE event_details SET name=?, type=?, event_date=?, duree=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, eventDetails.getName());
        ps.setString(2, eventDetails.getType());
        ps.setString(3, eventDetails.getEvent_date());
        ps.setString(4, eventDetails.getDuree());
        ps.setInt(5, eventDetails.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Event_details> getAll() throws SQLException {
        List<Event_details> eventDetailsList = new ArrayList<>();
        String query = "SELECT * FROM event_details";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
            eventDetails.setId(rs.getInt("id"));
            eventDetailsList.add(eventDetails);


        }
        return eventDetailsList;
    }

    public List<Event_details> getAll_past() throws SQLException {
        List<Event_details> eventDetailsList = new ArrayList<>();
        String query = "SELECT * FROM event_details where event_date < CURDATE()";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
            eventDetails.setId(rs.getInt("id"));
            eventDetailsList.add(eventDetails);


        }
        return eventDetailsList;
    }

    public List<Event_details> getAll_now() throws SQLException {
        List<Event_details> eventDetailsList = new ArrayList<>();
        String query = "SELECT * FROM event_details where event_date > CURDATE()";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
            eventDetails.setId(rs.getInt("id"));
            eventDetailsList.add(eventDetails);


        }
        return eventDetailsList;
    }

    public List<Event_details> search(String s) {
        List<Event_details> eventDetailsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM event_details WHERE name LIKE ? or type LIKE ? or event_date LIKE ? or duree LIKE ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + s + "%");
            ps.setString(2, "%" + s + "%");
            ps.setString(3, "%" + s + "%");
            ps.setString(4, "%" + s + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
                eventDetails.setId(rs.getInt("id"));
                eventDetailsList.add(eventDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventDetailsList;
    }

    public List<Event_details> getEventsByUserId(int id) {
        List<Event_details> eventDetailsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM event_details WHERE id IN (SELECT event_details_id FROM event_participants WHERE user_id=?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
                eventDetails.setId(rs.getInt("id"));
                eventDetailsList.add(eventDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventDetailsList;
    }

    public List<Event_details> getEventsByUserId_now(int id) {
        List<Event_details> eventDetailsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM event_details WHERE id IN (SELECT event_details_id FROM event_participants WHERE user_id=?) and event_date > CURDATE()";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
                eventDetails.setId(rs.getInt("id"));
                eventDetailsList.add(eventDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventDetailsList;
    }

    public void updatespots(int id) throws SQLException {
        String query = "UPDATE event_details SET nb_places=nb_places+1 WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public int get_event_rate(int id) throws SQLException {
        String query = "SELECT AVG(rate) FROM event_participants WHERE event_details_id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    //show user past events
    public List<Event_details> getPastEventsByUserId(int id) {
        List<Event_details> eventDetailsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM event_details WHERE id IN (SELECT event_details_id FROM event_participants WHERE user_id=?) and event_date < CURDATE()";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event_details eventDetails = new Event_details(rs.getString("name"), rs.getString("type"), rs.getString("event_date"), rs.getString("duree"), rs.getInt("nb_places"), rs.getInt("nb_total"));
                eventDetails.setId(rs.getInt("id"));
                eventDetailsList.add(eventDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventDetailsList;
    }

    public int total_events() {
        String query = "SELECT COUNT(*) FROM event_details";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public XYChart.Series<String, Integer> getEventsByMonth() throws SQLException {
            String query = "Select count(id), monthname(event_date) FROM event_details GROUP BY MONTHNAME(event_date)";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("January", 0));
            series.getData().add(new XYChart.Data<>("February", 0));
            series.getData().add(new XYChart.Data<>("March", 0));
            series.getData().add(new XYChart.Data<>("April", 0));
            series.getData().add(new XYChart.Data<>("May", 0));
            series.getData().add(new XYChart.Data<>("June", 0));
            series.getData().add(new XYChart.Data<>("July", 0));
            series.getData().add(new XYChart.Data<>("August", 0));
            series.getData().add(new XYChart.Data<>("September", 0));
            series.getData().add(new XYChart.Data<>("October", 0));
            series.getData().add(new XYChart.Data<>("November", 0));
            series.getData().add(new XYChart.Data<>("December", 0));
            while (rs.next()) {
                switch (rs.getString(2)) {
                    case "January":
                        series.getData().get(0).setYValue(rs.getInt(1));
                        break;
                    case "February":
                        series.getData().get(1).setYValue(rs.getInt(1));
                        break;
                    case "March":
                        series.getData().get(2).setYValue(rs.getInt(1));
                        break;
                    case "April":
                        series.getData().get(3).setYValue(rs.getInt(1));
                        break;
                    case "May":
                        series.getData().get(4).setYValue(rs.getInt(1));
                        break;
                    case "June":
                        series.getData().get(5).setYValue(rs.getInt(1));
                        break;
                    case "July":
                        series.getData().get(6).setYValue(rs.getInt(1));
                        break;
                    case "August":
                        series.getData().get(7).setYValue(rs.getInt(1));
                        break;
                    case "September":
                        series.getData().get(8).setYValue(rs.getInt(1));
                        break;
                    case "October":
                        series.getData().get(9).setYValue(rs.getInt(1));
                        break;
                    case "November":
                        series.getData().get(10).setYValue(rs.getInt(1));
                        break;
                    case "December":
                        series.getData().get(11).setYValue(rs.getInt(1));
                        break;
                }
        }
        return series;
    }
}


