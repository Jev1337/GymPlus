<?php

namespace App\Controller;

use App\Entity\Facture;
use App\Entity\User;
use App\Form\FactureType;
use App\Repository\DetailfactureRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/facture')]
class FactureController extends AbstractController
{
    #[Route('/', name: 'app_facture_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager): Response
    {
        $factures = $entityManager
            ->getRepository(Facture::class)
            ->findAll();

        return $this->render('facture/index.html.twig', [
            'factures' => $factures,
        ]);
    }

    // #[Route('/new', name: 'app_facture_new', methods: ['GET', 'POST'])]
    // public function new(Request $request, EntityManagerInterface $entityManager): Response
    // {
    //     $facture = new Facture();    

    //     $form = $this->createForm(FactureType::class, $facture);
    //     $form->handleRequest($request);
    

    //     if ($form->isSubmitted() && $form->isValid()) {
    //         $entityManager->persist($facture);
    //         $entityManager->flush();

    //         return $this->redirectToRoute('app_facture_index', [], Response::HTTP_SEE_OTHER);
    //     }

    //     return $this->renderForm('facture/new.html.twig', [
    //         'facture' => $facture,
    //         'form' => $form,
    //     ]);
    // }



    #[Route('/new', name: 'app_facture_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $facture = new Facture();    

        // Récupérer un utilisateur par défaut, par exemple, le premier utilisateur de la base de données
        $defaultUser = $entityManager->getRepository(User::class)->findOneBy([]); // Récupère le premier utilisateur
        $facture->setId($defaultUser); //setUser($defaultUser);

        $form = $this->createForm(FactureType::class, $facture);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($facture);
            $entityManager->flush();

            return $this->redirectToRoute('app_facture_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('facture/new.html.twig', [
            'facture' => $facture,
            'form' => $form,
        ]);
    }

    
    #[Route('/{idfacture}', name: 'app_facture_show', methods: ['GET'])]
    public function show(Facture $facture): Response
    {
        return $this->render('facture/show.html.twig', [
            'facture' => $facture,
        ]);
    }

    // #[Route('/{idfacture}', name: 'app_facture_show', methods: ['GET'])]
    // public function show(Facture $facture, DetailFactureRepository $detailFactureRepository): Response
    // {
    //     // Utiliser la méthode personnalisée pour récupérer les détails de la facture
    //     $detailsFacture = $detailFactureRepository->findDetailsByFactureId($facture->getIdfacture());

    //     return $this->render('facture/show.html.twig', [
    //         'facture' => $facture,
    //         'detailsFacture' => $detailsFacture,
    //     ]);
    // }

    #[Route('/factureDetail/{idfacture}', name: 'app_factureDetail_show', methods: ['GET'])]
    public function showFactureDetail(Facture $facture, DetailfactureRepository $detailFactureRepository): Response
    {
        // Utiliser la méthode personnalisée pour récupérer les détails de la facture
        $detailsFacture = $detailFactureRepository->findDetailsByFactureId($facture->getIdfacture());

        return $this->render('facture/showFactureAndDetail.html.twig', [
            'facture' => $facture,
            'detailsFacture' => $detailsFacture,
        ]);
    }



    #[Route('/{idfacture}/edit', name: 'app_facture_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Facture $facture, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(FactureType::class, $facture);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_facture_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('facture/edit.html.twig', [
            'facture' => $facture,
            'form' => $form,
        ]);
    }

    #[Route('/{idfacture}', name: 'app_facture_delete', methods: ['POST'])]
    public function delete(Request $request, Facture $facture, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$facture->getIdfacture(), $request->request->get('_token'))) {
            $entityManager->remove($facture);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_facture_index', [], Response::HTTP_SEE_OTHER);
    }
}
