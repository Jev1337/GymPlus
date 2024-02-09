package services;

import entities.detailfacture;

import java.sql.SQLException;
import java.util.List;

public interface IServiceDetailFacture {

    void ajouterDetailFacture (detailfacture p) throws SQLException;
    void modifierDetailfacture (detailfacture p) throws SQLException;
    void supprimerDetailfacture(int id) throws SQLException;
    List<detailfacture> recupererDetailfacture() throws SQLException;

}
