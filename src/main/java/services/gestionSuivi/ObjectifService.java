package services.gestionSuivi;

import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectifService implements IService<Objectif> {
    private Connection connection;
    public ObjectifService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    public int getCoachIdByName(String name_coach_selected) {
        String query = "SELECT id  FROM user WHERE username = ? AND role='Coach'";
        int coachId =0 ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name_coach_selected);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                coachId = rs.getInt("id");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coachId;
    }




    @Override
    public void add(Objectif obj) throws SQLException {
        try {
            java.sql.PreparedStatement ps = connection.prepareStatement("INSERT INTO OBJECTIF(userId , PoidsObj, DateD, DateF, PoidsAct, Taille, Alergie, TypeObj, CoachId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, obj.getId_user());
            ps.setFloat(2, obj.getPoids_Obj());
            ps.setDate(3, obj.getDateD());
            ps.setDate(4, obj.getDateF());
            ps.setFloat(5, obj.getPoids_Act());
            ps.setFloat(6, obj.getTaille());
            ps.setString(7, obj.getAlergie());
            ps.setString(8, obj.getTypeObj());
            ps.setInt(9, obj.getCoachId());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            java.sql.PreparedStatement ps=  connection.prepareStatement("DELETE FROM OBJECTIF WHERE `idObjectif`=?");
            ps.setInt(1,id);
            ps.executeUpdate();
            ps.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(Objectif obj) throws SQLException {
        try {
            System.out.printf("here");
            java.sql.PreparedStatement ps = connection.prepareStatement("UPDATE `objectif` \n "

                    +"SET  \n "
                    +"`PoidsObj`=?, \n"
                    +"`DateF`=?,\n"
                    +"`PoidsAct`=?,\n"
                    +"`Taille`=?,\n"
                    +"`Alergie`=?,\n"
                    +"`TypeObj`=?, \n"
                    +"`CoachId`=? \n"
                    + "WHERE `idObjectif`=?");
            ps.setFloat(1, obj.getPoids_Obj());
            ps.setDate(2, obj.getDateF());
            ps.setFloat(3, obj.getPoids_Act());
            ps.setFloat(4, obj.getTaille());
            ps.setString(5, obj.getAlergie());
            ps.setString(6, obj.getTypeObj());
            ps.setInt(7, obj.getCoachId());
            ps.setInt(8, obj.getId_objectif());
            ps.executeUpdate();
            ps.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public List getAll() throws SQLException {
        int userId= 2;
        List<Objectif> obj =new ArrayList<>();
        String query = "SELECT o.idObjectif , o.poidsObj, o.DateD, o.DateF, o.PoidsAct, o.Taille, o.Alergie, o.TypeObj, u.username AS username " +
                "FROM objectif o " +
                "JOIN user u ON o.CoachId = u.id " +
                "WHERE o.userId = ?  AND u.role='Coach' ";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
Objectif objectifs = new Objectif();

            objectifs.setId_objectif(rs.getInt("idObjectif"));
            objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
            objectifs.setDateD(rs.getDate("DateD"));
            objectifs.setDateF(rs.getDate("DateF"));
            objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
            objectifs.setTaille(rs.getFloat("Taille"));
            objectifs.setAlergie(rs.getString("Alergie"));
            objectifs.setTypeObj(rs.getString("TypeObj"));
            objectifs.setCoachName(rs.getString("username"));
            obj.add(objectifs);
        }
        return obj;
    }
}
