<?php

namespace App\Controller;

use App\Entity\Produit;
use App\Repository\ProduitRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use App\Entity\Facture;
use App\Entity\Detailfacture;
use App\Entity\Livraison;
use App\Entity\User;
use App\Repository\DetailfactureRepository;
use App\Repository\FactureRepository;
use Doctrine\ORM\EntityManagerInterface;
use SebastianBergmann\Environment\Console;

class PanierController extends AbstractController
{
    // #[Route('/panier', name: 'app_panier')]
    // public function index(): Response
    // {
    //     return $this->render('panier/index.html.twig', [
    //         'controller_name' => 'PanierController',
    //     ]);
    // }

    #[Route('/panier', name: 'app_panier')]
    public function index(SessionInterface $session , ProduitRepository $produitRepository): Response
    {       
        // print_r($repo->findMaxFacture());
        // var_dump($repo->findMaxFacture()[0]["maxId"]);
        // var_dump( $repo->searchFactureById($repo->findMaxFacture()[0]["maxId"])); 
        // $entityManager->getRepository(User::class)->findOneBy([]);


        $panier = $session->get('panier' , []);
        $panierWithData = [];
        foreach($panier as $idproduit => $quantite)
        {
            $panierWithData[] = [
                'produit'=> $produitRepository->find($idproduit),
                'quantite' => $quantite
            ];             
        }
        //dd($panierWithData);

        $total = 0;
        foreach($panierWithData as $item)
        {
            $totalItem = $item['produit']->getPrix() * $item['quantite'] * (1 - $item['produit']->getPromo());
            $total += $totalItem;
        }

        $gagnant = $session->get('gagnant');//, false);

        return $this->render('panier/index.html.twig', [
            //'controller_name' => 'PanierController',
            'items' => $panierWithData,
            'total' => $total,
            'gagnant' => $gagnant,
        ]);
    }

    // #[Route('/paniertest', name: 'panier_test', methods: ['GET'])]
    // public function show(): Response
    // {
    //     return $this->render('panier/test.html.twig', [
    //     ]);
    // }

    #[Route('/play-game', name: 'play_game')]
    public function playGame(Request $request, SessionInterface $session): Response
    {
        // Récupérer l'information sur la victoire ou la défaite de la session
        $aGagne = $request->query->get('aGagne');//, false);

        // Stocker le résultat dans la session
        $session->set('gagnant', $aGagne);

        // Rediriger l'utilisateur vers la page de panier avec le message approprié
        if ($aGagne) {
            return $this->redirectToRoute('app_panier', ['gagnant' => true]);
        } else {
            return $this->redirectToRoute('app_panier', ['gagnant' => false]);
        }
    }

    #[Route('/app_MemmoryGame_index', name: 'app_MemmoryGame_index', methods: ['GET'])]
    public function showGame(): Response
    {
        return $this->render('MemmoryGame/index.html.twig', [
        ]);
    }


    #[Route('/panier/add/{idproduit}', name: 'panier_add')]
    public function add($idproduit, SessionInterface $session ): Response
    {

        $panier = $session->get('panier' , []);

        if(!empty($panier[$idproduit]))
        {
            $panier[$idproduit]++;
        }
        else{
            $panier[$idproduit]=1;
        }

        $session->set('panier' , $panier);
        // dd($session->get('panier'));

        //Ajout => redirection Panier
        return $this->redirectToRoute('app_panier', [], Response::HTTP_SEE_OTHER);

        //Ajout =>Redirectio Meme page il reste dans la page All Produit 
        // return $this->redirectToRoute('app_produit_index', [], Response::HTTP_SEE_OTHER);

    }

    #[Route('/panier/remove/{idproduit}', name: 'panier_remove')]
    public function remove($idproduit, SessionInterface $session ): Response
    {

        $panier = $session->get('panier' , []);

        if(!empty($panier[$idproduit]))
        {
            unset($panier[$idproduit]);
        }
        
        $session->set('panier' , $panier);

        return $this->redirectToRoute('app_panier', [], Response::HTTP_SEE_OTHER);

    }

        

    #[Route('/panier/update/{idproduit}', name: 'update_cart')]
    public function updateCart(Request $request, $idproduit, SessionInterface $session): JsonResponse
    {
        // Récupérez la nouvelle quantité depuis la requête
        $newQuantity = $request->query->get('quantity');

        // Mettez à jour la quantité dans le panier en session
        $panier = $session->get('panier', []);
        $panier[$idproduit] = $newQuantity;
        $session->set('panier', $panier);

        // Recalculez le total
        $total = $this->calculateTotal($panier);

        // Recalculez les totaux de chaque article
        $totalItems = $this->calculateTotalItems($panier);

        // Retournez le total général et les totaux des articles mis à jour en JSON
        return new JsonResponse(['total' => $total, 'totalItems' => $totalItems]);
    }

    private function calculateTotalItems(array $panier): array
    {
        $totalItems = [];

        foreach ($panier as $idproduit => $quantite) {
            // Calculez le total de l'article
            $produit = $this->getDoctrine()->getRepository(Produit::class)->find($idproduit);
            if ($produit) {
                $totalItems[] = $produit->getPrix() * $quantite * (1 - $produit->getPromo());
            } else {
                $totalItems[] = 0;
            }
        }

        return $totalItems;
    }



    private function calculateTotal(array $panier): float
    {
        $total = 0;
        foreach ($panier as $idproduit => $quantite) {
            // Ajoutez le prix du produit multiplié par sa quantité au total
            $produit = $this->getDoctrine()->getRepository(Produit::class)->find($idproduit);
            if ($produit) {
                $total += $produit->getPrix() * $quantite * (1 - $produit->getPromo());
            }
        }

        return $total;
    }



    #[Route('/valider-panier', name: 'valider_panier', methods: ['POST'])]
    public function validerPanier(Request $request, EntityManagerInterface $entityManager, FactureRepository $repo, SessionInterface $session): Response
    {
        // Récupérer les données envoyées depuis le front-end
        // $datavar = '{"methodeDePaiement":"lll","total":"400","panierDetails":[{"idproduit":"2","quantite":"1","prixunitaire":"100","promo":"0","totalUnArticle":"100"},{"idproduit":"3","quantite":"1","prixunitaire":"300","promo":"0","totalUnArticle":"300"}]}';
        // $data = json_decode($datavar , true);
        //json_decode($data->getContent(), true);

            //Récupérer le contenu JSON de la requête
            $content = $request->getContent();
        
            // Convertir le contenu JSON en tableau associatif
            $data = json_decode($content, true);

        
            //*****Partie Facture */


        // Créer une nouvelle entité Facture
        $facture = new Facture();
        $facture->setDatevente(new \DateTime());
        $facture->setPrixtatalpaye($data['total']);
        $facture->setMethodedepaiement($data['methodeDePaiement']);

        // Récupérer l'utilisateur par son ID (à adapter selon votre logique)
        $defaultUser = $entityManager->getRepository(User::class)->findOneBy([]);
        $facture->setId($defaultUser); //setUser($defaultUser);

        // Enregistrer la facture dans la base de données
        $entityManager->persist($facture);
        $entityManager->flush();



        //*****Partie Detail Facture */


        //Enregistrer les détails de chaque produit dans le panier
        // $defaultfacture = $repo->searchFactureById($repo->findMaxFacture()[0]["maxId"]); 
        $defaultfacture= $entityManager->getRepository(Facture::class)->find($repo->findMaxFacture()[0]["maxId"]+0);
        $indice = 1;
        foreach ($data['panierDetails'] as $detailData) {
            $detailFacture = new Detailfacture();
            // $iddetailfacture = mt_rand(1, 999);
            // dd($detailData);
            $detailFacture->setIddetailfacture($indice);
            $detailFacture->setPrixventeunitaire($detailData['prixunitaire']);
            $detailFacture->setQuantite($detailData['quantite']);
            $detailFacture->setTauxremise($detailData['promo']);
            $detailFacture->setPrixtotalarticle($detailData['totalUnArticle']);
            //$detailFacture->setIdproduit($detailData['idproduit']);
            $produit = $entityManager->getRepository(Produit::class)->find($detailData['idproduit']);

            if ($produit) {
                // Déduire la quantité achetée du stock du produit
                $quantiteAchetee = $detailData['quantite'];
                $nouveauStock = $produit->getStock() - $quantiteAchetee;
        
                // Mettre à jour le stock du produit
                $produit->setStock($nouveauStock);
        
                // Ajouter le détail de la facture
                $detailFacture->setIdproduit($produit);
            }

            // $detailFacture->setIdproduit($produit);

            $detailFacture->setIdFacture($defaultfacture);

            $entityManager->persist($detailFacture);
            $indice++;
        }

        $entityManager->flush();

        //*****Partie Livraison */

        $livraison = new Livraison();
        $livraison->setIdfacture($repo->findMaxFacture()[0]["maxId"]+0);
        $livraison->setIdclient(12345632);
        $livraison->setLieu($data['lieu']); 
        $livraison->setEtat("In progress");

    
        // Enregistrer la livr$livraison dans la base de données
        $entityManager->persist($livraison);
        $entityManager->flush();

        $session->set('panier', []); // Cela rendra le panier vide

        return new JsonResponse(['success' => true]);

    }

    #[Route('/panier/Apres_validation_panier', name: 'Apres_validation_panier')]
    public function ApresValidationPanier(FactureRepository $factureRepository, DetailfactureRepository $detailFactureRepository): Response
    {
        // Récupérer l'ID de la dernière facture
        $derniereFactureId = $factureRepository->findMaxFacture()[0]["maxId"];
        
        // Récupérer la dernière facture
        $derniereFacture = $factureRepository->find($derniereFactureId);

        // Récupérer les détails de la dernière facture
        $detailsFacture = $detailFactureRepository->findDetailsByFactureId($derniereFacture->getIdfacture());

        // Afficher la dernière facture avec ses détails dans un template Twig
        return $this->render('panier/PanierValider.html.twig', [
            'facture' => $derniereFacture,
            'detailsFacture' => $detailsFacture,
        ]);
    }

}

