package test;

import entities.produit;
import services.ProduitService;

import entities.facture;
import services.FactureService;

import services.DetailFactureService;
import entities.detailfacture;

import utils.MyDataBase;

import java.net.SocketOption;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        MyDataBase db = new MyDataBase();

        //produit
        produit p1 = new produit("produit3", 2.500F , 23 , "food1" , "food" , "azertyuiop" , 2 , 0.05F);
        produit p2 = new produit("produit5", 5.555F , 203 , "vetement1" , "vetement" , "azertyuiop" , 5 , 0.0F);

        ProduitService ps = new ProduitService();
        try
        {
            //ps.ajouterProduit(p1);
            //ps.ajouterProduit(p2);
            //System.out.println(ps.recupererProduit());
            //ps.modifierProduit(p2);
            //ps.supprimerProduit(8);
            System.out.println(ps.recupererProduit());
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }



        //facture
        facture f1 = new facture(12.5F, "carte" ,10 );

        FactureService fs = new FactureService();
        try
        {
            //fs.ajouterFacture(f1);
            System.out.println(fs.recupererfacture());
            //fs.modifierfacture(f1);
            //fs.supprimerfacture(2);
            //System.out.println(fs.recupererfacture());
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //detailfacture
        detailfacture df1 = new detailfacture(3,6,5,2,0.5F);

        DetailFactureService df = new DetailFactureService();
        try
        {
            //df.ajouterDetailFacture(df1);
            System.out.println(df.recupererDetailfacture());
            //df.modifierDetailfacture(df1);
            //df.supprimerDetailfacture(6);
            //System.out.println(df.recupererDetailfacture());
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }



    }
}