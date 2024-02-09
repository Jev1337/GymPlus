package services.gestionSuivi;

import entities.gestionSuivi.Planning;
import entities.gestionuser.Staff;
import services.IService;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlanningService implements IService<Planning> {
    private Connection connection;
    public PlanningService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Planning plan) throws SQLException {
        try {
            java.sql.PreparedStatement ps=connection.prepareStatement("INSERT INTO PLANNING(id_Objectif,id_Coach,TrainningProg,FoodProg) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1,plan.getIdObjectif());
            ps.setInt(2,plan.getIdCoach());
            ps.setString(3,plan.getTrainingProg());
            ps.setString(4,plan.getFoodProg());
            ps.execute();
            ps.close();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            java.sql.PreparedStatement ps=  connection.prepareStatement("DELETE FROM PLANNING WHERE `idObjectif `=?");
            ps.setInt(1,id);
            ps.executeUpdate();
            ps.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Planning plan) throws SQLException {
        try {
            int coachId = 2;
            java.sql.PreparedStatement ps = connection.prepareStatement("UPDATE `PLANNING` \n "
                    +"SET  \n "
                    +"`TrainningProg`=?, \n"
                    +"`FoodProg`=?\n"
                    + "WHERE `id_Coach`=?");
            ps.setString(1, plan.getTrainingProg());
            ps.setString(2, plan.getFoodProg());
            ps.setInt(3, coachId);
            ps.executeUpdate();
            ps.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Planning> getAll() throws SQLException {
        List<Planning> plans =new ArrayList<>();
        String query ="SELECT * FROM PLANNING";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Planning plannings = new Planning();
            plannings.setId_Planning(rs.getInt("id_Planning"));
            plannings.setIdObjectif(rs.getInt("idObjectif"));
            plannings.setIdCoach(rs.getInt("id_Coach"));
            plannings.setTrainingProg(rs.getString("TrainningProg"));
            plannings.setFoodProg(rs.getString("FoodProg"));
            plans.add(plannings);
        }
        return plans;
    }
}
