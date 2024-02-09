package services;

import entities.facture;
import java.sql.SQLException;
import java.util.List;

public interface IServicefacture {

    void ajouterFacture (facture p) throws SQLException;
    void modifierfacture (facture p) throws SQLException;
    void supprimerfacture(int id) throws SQLException;
    List<facture> recupererfacture() throws SQLException;

}






