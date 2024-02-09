package services.gestionSuivi;

import entities.gestionSuivi.Objectif;
import services.IService;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ObjectifService implements IService<Objectif> {
    private Connection connection;
    public ObjectifService() {
        connection = MyDatabase.getInstance().getConnection();
    }



    @Override
    public void add(Objectif obj) throws SQLException {
        try {
            java.sql.PreparedStatement ps=connection.prepareStatement("INSERT INTO OBJECTIF(PoidsObj,DateD,DateF,PoidsAct,Taille,Alergie,TypeObj,CoachId) VALUES(?,?,?,?,?,?,?,?)");
            ps.setFloat(1,obj.getPoids_Obj());
            ps.setDate(2,obj.getDateD());
            ps.setDate(3,obj.getDateF());
            ps.setFloat(4,obj.getPoids_Act());
            ps.setFloat(5,obj.getTaille());
            ps.setString(6,obj.getAlergie());
            ps.setString(7,obj.getTypeObj());
            ps.setInt(8,obj.getCoachId());

            ps.execute();
            ps.close();
        }catch (Exception e ){
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
            int userId = 2;
            java.sql.PreparedStatement ps = connection.prepareStatement("UPDATE `objectif` \n "

                    +"SET  \n "
                    +"`PoidsObj`=?, \n"
                    +"`DateF`=?,\n"
                    +"`PoidsAct`=?,\n"
                    +"`Taille`=?,\n"
                    +"`Alergie`=?,\n"
                    +"`TypeObj`=?, \n"
                    +"`CoachId`=? \n"
                    + "WHERE `userId`=? AND `DateF`=?");
            ps.setFloat(1, obj.getPoids_Obj());
            ps.setDate(2, obj.getDateF());
            ps.setFloat(3, obj.getPoids_Act());
            ps.setFloat(4, obj.getTaille());
            ps.setString(5, obj.getAlergie());
            ps.setString(6, obj.getTypeObj());
            ps.setInt(7, obj.getCoachId());
            ps.setInt(8, userId);
            ps.setDate(9, obj.getDateF());

            ps.executeUpdate();
            ps.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public List getAll() throws SQLException {
        return null;
    }
}
