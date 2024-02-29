package services.gestionSuivi;

import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import entities.gestionuser.Staff;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
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
            java.sql.PreparedStatement ps=connection.prepareStatement("INSERT INTO PLANNING(idObjectif,TrainingProg,FoodProg) VALUES(?,?,?)");
            ps.setInt(1,plan.getIdObjectif());
            ps.setString(2,plan.getTrainingProg());
            ps.setString(3,plan.getFoodProg());
            ps.execute();
            ps.close();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            java.sql.PreparedStatement ps=  connection.prepareStatement("DELETE FROM PLANNING WHERE `id_Planning`= ?");
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
            java.sql.PreparedStatement ps = connection.prepareStatement("UPDATE `PLANNING` \n "
                    +"SET  \n "
                    +"`TrainingProg`=?, \n"
                    +"`FoodProg`=? \n"
                    + "WHERE `id_Planning`=?");
            ps.setString(1, plan.getTrainingProg());
            ps.setString(2, plan.getFoodProg());
            ps.setInt(3, plan.getId_Planning());
            ps.executeUpdate();
            ps.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public List<Planning> getAll() throws SQLException {
       int userId =2;
        List<Planning> plans =new ArrayList<>();
        String query = "SELECT p.id_Planning, p.idObjectif, p.TrainningProg, p.FoodProg " +
                "FROM PLANNING p " +
                "JOIN OBJECTIF o ON o.idObjectif = p.idObjectif " +
                "WHERE o.userId = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Planning plannings = new Planning();
            plannings.setId_Planning(rs.getInt("id_Planning"));
            plannings.setIdObjectif(rs.getInt("idObjectif"));
            plannings.setTrainingProg(rs.getString("TrainningProg"));
            plannings.setFoodProg(rs.getString("FoodProg"));
            plans.add(plannings);
        }
        return plans;
    }
}
