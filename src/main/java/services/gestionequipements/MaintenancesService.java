package services.gestionequipements;

import entities.gestionequipements.Maintenances;
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
        String sql="insert into maintenances values (?,?,?,?)";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setInt(1,maintenances.getIdm());
        ps.setInt(2, maintenances.getIde());
        ps.setString(3,maintenances.getDate_maintenance());
        ps.setString(4,maintenances.getStatus());
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
        String sql="delete from maintenances where id =?";
        PreparedStatement ps =connection.prepareStatement(sql);
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
}
