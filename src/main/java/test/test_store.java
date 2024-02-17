package test;

import utils.MyDatabase;
import entities.gestionStore.produit;
import entities.gestionStore.facture;
import entities.gestionStore.detailfacture;
import services.gestionStore.ProduitService;
import services.gestionStore.FactureService;
import services.gestionStore.DetailFactureService;

import java.util.List;

public class test_store {

    public static void main(String[] args)
    {
        MyDatabase db = new MyDatabase();

        //*******************************produit

        produit p1 = new produit("produit13", 9.999F , 10 , "equipement1" , "equipement" , "azertyuiop" , 2 , 0.00F);
        produit p2 = new produit("produit13", 5.555F , 100 , "azerty" , "Food" , "azertyuiop" , 10 , 0.10F);
        produit p3 = new produit("rania", 6.5F , 20 , "rania" , "rania" , "rania" , 200 , 0.50F);

        ProduitService ps = new ProduitService();

        try
        {
            //****************Add
            //ps.add(p1);
            //****************Update
            //p3.setIdProduit(15);
            //ps.update(p3);
            //****************Delete
            //ps.delete(16);
            //****************Affichage
            //System.out.println(ps.getOne(12));
            //System.out.println(ps.getAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //*******************************facture

        facture f1 = new facture("espece" ,10 );
        facture f2 = new facture(200.5F, "azerty" ,10 );
        facture f5 = new facture(200.5F , "hiiii" ,10 );

        FactureService fs = new FactureService();

        try
        {
            //****************Add
            //fs.add(f1);
            //****************Update
            f1.setIdFacture(12);
            fs.update(f1);
            //****************Delete
            //fs.delete(3);
            //****************Affichage
            //System.out.println(fs.getAll());
            //List<facture> listeDeFactures = fs.getAll();
            //System.out.println(listeDeFactures.get(1).ListeDetails);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //*******************************detailfacture

        detailfacture df10 = new detailfacture(11,2,13,10,0.10F);
        detailfacture df3 = new detailfacture(11,16,15,5,0.10F);

        DetailFactureService df = new DetailFactureService();

        try
        {
            //****************Add
            // df.add(df10);
            //****************Update
            //f1.setIdFacture(7);
            //df1.setIdDetailFacture(1);
            //df.update(df3);
            //****************Delete
            //df.delete(1);
            //****************Affichage
            //System.out.println(df.getAll());
            //System.out.println(df.getDetailFacture(8));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
