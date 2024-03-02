package services.gestionequipements;

import entities.gestionequipements.Maintenances;
import javafx.scene.chart.XYChart;
import utils.MyDatabase;
import services.IService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenancesService implements IService<Maintenances>{

    private Connection connection;

    public MaintenancesService() {
        connection= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Maintenances maintenances) throws SQLException {
        String sql="insert into maintenances (equipements_details_id, date_maintenance, status) values (?,?,?)";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setInt(1, maintenances.getIde());
        ps.setString(2,maintenances.getDate_maintenance());
        ps.setString(3,maintenances.getStatus());
        ps.executeUpdate();
        sql="update equipements_details set etat='Under Maintenance' where id=?";
        ps =connection.prepareStatement(sql);
        ps.setInt(1, maintenances.getIde());
        ps.executeUpdate();
    }

    @Override
    public void update(Maintenances maintenances) throws SQLException {
        String sql="update maintenances set equipements_details_id=?, date_maintenance=?, status=? where id=?";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setInt(1, maintenances.getIde());
        ps.setString(2,maintenances.getDate_maintenance());
        ps.setString(3,maintenances.getStatus());
        ps.setInt(4,maintenances.getIdm());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "update equipements_details set etat='Good' where id=(select equipements_details_id from maintenances where id=?)";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps =connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        sql="delete from maintenances where id =?";
        ps =connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Maintenances> getAll() throws SQLException {
        String sql = "select * from maintenances";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Maintenances> maint = new ArrayList<>();
        while (rs.next())
            maint.add(new Maintenances(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
        return maint;
    }

    public Boolean isUnderMaintenance(int id) throws SQLException {
        String sql = "select * from maintenances where equipements_details_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public XYChart.Series<String, Integer> getMaintenancesByMonth() throws SQLException {
        String sql = "select count(id), monthname(date_maintenance) from maintenances group by monthname(date_maintenance)";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
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

    public int getMaintenancesCount() throws SQLException {
        String sql = "select count(*) from maintenances";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}
