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

        //*******************************
        //produit
        produit p1 = new produit("produit13", 9.999F , 10 , "equipement1" , "equipement" , "azertyuiop" , 2 , 0.00F);
        produit p2 = new produit("produit13", 5.555F , 100 , "azerty" , "Food" , "azertyuiop" , 10 , 0.10F);
        produit p3 = new produit("produit15", 6.5F , 20 , "vetement1" , "vetement" , "azertyuiop" , 200 , 0.50F);

        ProduitService ps = new ProduitService();


        try
        {
            //ps.add(p1);
            //ps.add(p2);
            //ps.add(p3);
/*
            //*******update
            produit produitToUpdate = new produit();
            produitToUpdate.setIdProduit(12);
            produitToUpdate.setName("produit12");
            produitToUpdate.setPrix(5.000F);
            produitToUpdate.setStock(200);
            produitToUpdate.setDescription("equipement1");
            produitToUpdate.setCategorie("equipement");
            produitToUpdate.setPhoto("photo.jpg");
            produitToUpdate.setSeuil(2);
            produitToUpdate.setPromo(0.00f);
            ps.update(produitToUpdate);
 */
            //p3.setIdProduit(15);
            //ps.update(p3);

            //ps.delete(16);

            //System.out.println(ps.getOne(12));


            //System.out.println(ps.getAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //*******************************
        //facture
        facture f1 = new facture(100.9F, "espece" ,10 );
        facture f2 = new facture(200.5F, "rania" ,10 );
        detailfacture df2 = new detailfacture(7,555,13,1000,0.0F);

        f2.ListeDetails.add(df2);

        FactureService fs = new FactureService();

        try
        {
            //fs.add(f2);

            //f2.setIdFacture(9);
            //fs.update(f2);

            //fs.delete(3);

            //System.out.println(fs.getAll());

            List<facture> listeDeFactures = fs.getAll();
            System.out.println(listeDeFactures.get(1).ListeDetails);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //*******************************
        //detailfacture
        //detailfacture df1 = new detailfacture(7,1,12,100,0.50F);
        //detailfacture df2 = new detailfacture(7,2,13,10,0.10F);

        detailfacture df3 = new detailfacture(8,16,15,3,0.10F);


        DetailFactureService df = new DetailFactureService();

        try
        {
            //df.add(df3);
//System.out.println(df.getDetailFacture(8));
            //f1.setIdFacture(7);
            //df1.setIdDetailFacture(1);
            //df1.setIdProduit(12);
            //df.update(df1);

            //df.delete(1);

            //System.out.println(df.getAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
