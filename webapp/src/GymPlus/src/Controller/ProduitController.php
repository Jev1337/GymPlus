<?php

namespace App\Controller;

use App\Entity\Produit;
use App\Form\ProduitType;
use App\Repository\ProduitRepository;
use Doctrine\ORM\EntityManagerInterface;
use MercurySeries\FlashyBundle\FlashyNotifier;
use SebastianBergmann\Environment\Console;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Knp\Component\Pager\PaginatorInterface;



#[Route('/produit')]
class ProduitController extends AbstractController
{
    // #[Route('/', name: 'app_produit_index', methods: ['GET'])]
    // public function index(EntityManagerInterface $entityManager): Response
    // {
    //     $produits = $entityManager
    //         ->getRepository(Produit::class)
    //         ->findAll();

    //     return $this->render('produit/index.html.twig', [
    //         'produits' => $produits,
    //     ]);
    // }
    #[Route('/', name: 'app_produit_index', methods: ['GET'])]
    // public function index(EntityManagerInterface $entityManager, ProduitRepository $productRepository): Response
    // {
    //     // Récupérer les trois derniers produits ajoutés
    //     $latestProducts = $productRepository->findLatestProducts();

    //     // Récupérer tous les produits
    //     $produits = $entityManager->getRepository(Produit::class)->findAll();

    //     return $this->render('produit/index.html.twig', [
    //         'latestProducts' => $latestProducts,
    //         'produits' => $produits,
    //     ]);
    // }


    public function index(SessionInterface $session ,EntityManagerInterface $entityManager, ProduitRepository $productRepository , PaginatorInterface $paginator , Request $request): Response
    {
        // Récupérer les trois derniers produits ajoutés
        $latestProducts = $productRepository->findLatestProducts();

        // Récupérer tous les produits
        // $produits = $entityManager->getRepository(Produit::class)->findAll();
        // Récupérer tous les produits
        $data = $productRepository->findAll();

        $produits = $paginator->paginate(
            $data,
            $request->query->getInt('page' , 1),
            6 //limit des nb produits dans la page
        );

        //Panier
        $panier = $session->get('panier' , []);
        $panierWithData = [];
        foreach($panier as $idproduit => $quantite)
        {
            $panierWithData[] = [
                'produit'=> $productRepository->find($idproduit),
                'quantite' => $quantite
            ];             
        }
        $total = 0;
        foreach($panierWithData as $item)
        {
            $totalItem = $item['produit']->getPrix() * $item['quantite'] * (1 - $item['produit']->getPromo());
            $total += $totalItem;
        }

        return $this->render('produit/index.html.twig', [
            'latestProducts' => $latestProducts,
            'produits' => $produits,

            'items' => $panierWithData,
            'total' => $total,
        ]);
    }

    #[Route('/Admin', name: 'app_produit_indexAdmin', methods: ['GET'])]
    public function indexAdmin(EntityManagerInterface $entityManager, ProduitRepository $productRepository , FlashyNotifier $flashy): Response
    {
        $produits = $entityManager->getRepository(Produit::class)->findAll();
        
        $stockInsuffisant = [];
        
        foreach ($produits as $produit) {
            if ($produit->getSeuil() >= $produit->getStock()) {
                // Stock insuffisant
                $stockInsuffisant[$produit->getIdproduit()] = $produit->getName();
            }
        }
    
        $messageAvertissement = '';
        foreach ($stockInsuffisant as $idProduit => $nomProduit) {
            $messageAvertissement .= " ID: $idProduit Name: $nomProduit ";
        }
    
        if (!empty($messageAvertissement)) {
            $messageFinal = '';
            if (!empty($messageAvertissement)) {
                $messageFinal .= "Insufficient stock : $messageAvertissement";
            }
            $flashy->error($messageFinal);

        } else {
            $flashy->success("All products have sufficient stock.");
        }
    
        return $this->render('produit/indexAdmin.html.twig', [
            'produits' => $produits,
        ]);
    }
    



    // #[Route('/sort-by-price-ajax', name: 'app_produit_sort_by_price_ajax', methods: ['GET'])]
    // public function sortByPriceAjax(EntityManagerInterface $entityManager, ProduitRepository $productRepository): Response
    // {
    //     // Récupérer les produits triés par prix croissant
    //     $produits = $productRepository->findAllSortedByPriceAscending();

    //     // Rendre le contenu HTML des produits triés
    //     $html = $this->renderView('produit/_produits.html.twig', ['produits' => $produits]);

    //     return new Response($html);
    // }


    // #[Route('/new', name: 'app_produit_new', methods: ['GET', 'POST'])]
    // public function new(Request $request, EntityManagerInterface $entityManager): Response
    // {
    //     $produit = new Produit();
    //     $form = $this->createForm(ProduitType::class, $produit);
    //     $form->handleRequest($request);

    //     if ($form->isSubmitted() && $form->isValid()) {
    //         $entityManager->persist($produit);
    //         $entityManager->flush();

    //         return $this->redirectToRoute('app_produit_index', [], Response::HTTP_SEE_OTHER);
    //     }

    //     return $this->renderForm('produit/new.html.twig', [
    //         'produit' => $produit,
    //         'form' => $form,
    //     ]);
    // }

    #[Route('/new', name: 'app_produit_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $produit = new Produit();
    $form = $this->createForm(ProduitType::class, $produit);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $produit = $form->getData();

        if ($photo = $form['photo']->getData()) {
            $photoDir= "/public/images";
            $originalFilename = pathinfo($photo->getClientOriginalName(), PATHINFO_FILENAME);
            $fileName = $originalFilename.'.'.$photo->guessExtension();
            $photo->move($photoDir, $fileName);
            $produit->setPhoto($fileName);
        }

        $entityManager->persist($produit);
        $entityManager->flush();

        return $this->redirectToRoute('app_produit_indexAdmin', [], Response::HTTP_SEE_OTHER);
    }

    return $this->renderForm('produit/new.html.twig', [
        'produit' => $produit,
        'form' => $form,
    ]);
    }

    #[Route('/{idproduit}', name: 'app_produit_show', methods: ['GET'])]
    // public function show(Produit $produit): Response
    // {
    //     return $this->render('produit/show.html.twig', [
    //         'produit' => $produit,
    //     ]);
    // }
    public function show(Request $request, Produit $produit): Response
    {
        // Récupérer le nom de la photo sans extension
        $photoNameWithoutExtension = pathinfo($produit->getPhoto(), PATHINFO_FILENAME);
        
        // Construire le chemin de la vidéo
        $videoPath = '/videos/' . $photoNameWithoutExtension . '.mp4';
        // var_dump($videoPath);   

        // Construire l'URL complète vers la vidéo
        $videoUrl = $request->getSchemeAndHttpHost() . $videoPath;
        // var_dump($videoUrl);    

        // Vérifier si la vidéo existe
        $videoExists = file_exists($_SERVER['DOCUMENT_ROOT'] . $videoPath);
        
        // Passer les données à votre modèle Twig pour l'affichage
        return $this->render('produit/show.html.twig', [
            'produit' => $produit,
            'videoUrl' => $videoUrl,
            'videoExists' => $videoExists,
        ]);
    }
   

    

    // #[Route('/{idproduit}/edit', name: 'app_produit_edit', methods: ['GET', 'POST'])]
    // public function edit(Request $request, Produit $produit, EntityManagerInterface $entityManager): Response
    // {
    //     $form = $this->createForm(ProduitType::class, $produit);
    //     $form->handleRequest($request);

    //     if ($form->isSubmitted() && $form->isValid()) {
    //         $entityManager->flush();

    //         return $this->redirectToRoute('app_produit_index', [], Response::HTTP_SEE_OTHER);
    //     }

    //     return $this->renderForm('produit/edit.html.twig', [
    //         'produit' => $produit,
    //         'form' => $form,
    //     ]);
    // }

    #[Route('/{idproduit}/edit', name: 'app_produit_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Produit $produit, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ProduitType::class, $produit);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Vérifier si un nouveau fichier a été téléchargé
            if ($photoFile = $form['photo']->getData()) {
                // Déplacer et traiter le nouveau fichier
                $photoDir= "/public/images";
                // $photoDir = $this->getParameter("/public/images"); 
                $originalFilename = pathinfo($photoFile->getClientOriginalName(), PATHINFO_FILENAME);
                $newFilename = $originalFilename.'.'.$photoFile->guessExtension();
                $photoFile->move($photoDir, $newFilename);

                // Mettre à jour le chemin de la photo dans l'entité Produit
                $produit->setPhoto($newFilename);
            }

            $entityManager->flush();

            return $this->redirectToRoute('app_produit_indexAdmin', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('produit/edit.html.twig', [
            'produit' => $produit,
            'form' => $form,
        ]);
    }


    #[Route('/{idproduit}', name: 'app_produit_delete', methods: ['POST'])]
    public function delete(Request $request, Produit $produit, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$produit->getIdproduit(), $request->request->get('_token'))) {
            $entityManager->remove($produit);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_produit_indexAdmin', [], Response::HTTP_SEE_OTHER);
    }




}
