<?php

namespace App\Controller;

use App\Entity\Detailfacture;
use App\Entity\Facture;
use App\Entity\Produit;
use App\Form\DetailfactureType;
use App\Repository\ProduitRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/detailfacture')]
class DetailfactureController extends AbstractController
{
    #[Route('/', name: 'app_detailfacture_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager): Response
    {
        $detailfactures = $entityManager
            ->getRepository(Detailfacture::class)
            ->findAll();

        return $this->render('detailfacture/index.html.twig', [
            'detailfactures' => $detailfactures,
        ]);
    }

    // #[Route('/new', name: 'app_detailfacture_new', methods: ['GET', 'POST'])]
    // public function new(Request $request, EntityManagerInterface $entityManager, ProduitRepository $service): Response
    // {
    //     $detailfacture = new Detailfacture();

    //     $defaultProduit = $entityManager->getRepository(Produit::class)->findOneBy([]); // Récupère le premier utilisateur
    //     $detailfacture->setIdproduit($defaultProduit);

    //     $defaultFacture = $entityManager->getRepository(Facture::class)->findOneBy([]); // Récupère le premier utilisateur
    //     $detailfacture->setIdfacture($defaultFacture);

    //     $form = $this->createForm(DetailfactureType::class, $detailfacture);
    //     $form->handleRequest($request);

    //     if ($form->isSubmitted() && $form->isValid()) {
    //         $entityManager->persist($detailfacture);
    //         $entityManager->flush();

    //         return $this->redirectToRoute('app_detailfacture_index', [], Response::HTTP_SEE_OTHER);
    //     }

    //     return $this->renderForm('detailfacture/new.html.twig', [
    //         'detailfacture' => $detailfacture,
    //         'form' => $form,
    //     ]);
    // }
    #[Route('/new', name: 'app_detailfacture_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // $detailfacture = new Detailfacture();

        $detailfacture = new Detailfacture();

            $defaultProduit = $entityManager->getRepository(Produit::class)->findOneBy([]); // Récupère le premier utilisateur
            $detailfacture->setIdproduit($defaultProduit);
    
            $defaultFacture = $entityManager->getRepository(Facture::class)->findOneBy([]); 
            $detailfacture->setIdfacture($defaultFacture);

        
        $form = $this->createForm(DetailfactureType::class, $detailfacture);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Récupérer la quantité de produit ajoutée dans le détail de la facture
            $quantite = $detailfacture->getQuantite();


            // Récupérer le prix unitaire du produit associé au détail de la facture
            $prixUnitaire = $detailfacture->getPrixventeunitaire();

            // Récupérer le taux de remise du détail de la facture (exprimé en décimal, par exemple, 0.1 pour 10%)
            $tauxRemise = $detailfacture->getTauxremise();

            // Calculer le prix total de l'article
            // $prixTotalArticle = $prixUnitaire * $quantite;
            $prixTotalArticle = $prixUnitaire * $quantite * (1 - $tauxRemise);


            // Mettre à jour l'attribut prixTotalArticle dans l'objet Detailfacture
            $detailfacture->setPrixTotalArticle($prixTotalArticle);


            // Récupérer le produit associé au détail de la facture
            $produit = $detailfacture->getIdproduit();

            // Vérifier si le produit existe
            if ($produit) {
                // Mettre à jour le stock du produit
                $nouveauStock = $produit->getStock() - $quantite;
                $produit->setStock($nouveauStock);

                // Persistez les changements dans la base de données
                $entityManager->persist($detailfacture);
                $entityManager->persist($produit);
                $entityManager->flush();

                return $this->redirectToRoute('app_detailfacture_index', [], Response::HTTP_SEE_OTHER);
            }
        }

        return $this->renderForm('detailfacture/new.html.twig', [
            'detailfacture' => $detailfacture,
            'form' => $form,
        ]);
    }


    #[Route('/{iddetailfacture}', name: 'app_detailfacture_show', methods: ['GET'])]
    public function show(Detailfacture $detailfacture): Response
    {
        return $this->render('detailfacture/show.html.twig', [
            'detailfacture' => $detailfacture,
        ]);
    }

    #[Route('/{iddetailfacture}/edit', name: 'app_detailfacture_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Detailfacture $detailfacture, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(DetailfactureType::class, $detailfacture);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_detailfacture_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('detailfacture/edit.html.twig', [
            'detailfacture' => $detailfacture,
            'form' => $form,
        ]);
    }

    #[Route('/{iddetailfacture}', name: 'app_detailfacture_delete', methods: ['POST'])]
    public function delete(Request $request, Detailfacture $detailfacture, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$detailfacture->getIddetailfacture(), $request->request->get('_token'))) {
            $entityManager->remove($detailfacture);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_detailfacture_index', [], Response::HTTP_SEE_OTHER);
    }
}
