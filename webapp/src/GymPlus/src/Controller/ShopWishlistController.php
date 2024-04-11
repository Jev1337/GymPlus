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
use Doctrine\ORM\EntityManagerInterface;
use SebastianBergmann\Environment\Console;

class ShopWishlistController extends AbstractController
{
    #[Route('/wishList', name: 'app_wishList')]
    public function index(SessionInterface $session , ProduitRepository $produitRepository): Response
    {       
        $wishList = $session->get('wishList' , []);
        $wishListWithData = [];
        foreach($wishList as $idproduit => $quantite)
        {
            $wishListWithData[] = [
                'produit'=> $produitRepository->find($idproduit),
                'quantite' => $quantite
            ];             
        }
       
        return $this->render('wishList/index.html.twig', [
            'items' => $wishListWithData,
        ]);
    }

    #[Route('/wishList/add/{idproduit}', name: 'wishList_add')]
    public function add($idproduit, SessionInterface $session ): Response
    {
        $wishList = $session->get('wishList' , []);

        if(!empty($wishList[$idproduit]))
        {
            $wishList[$idproduit]++;
        }
        else{
            $wishList[$idproduit]=1;
        }

        $session->set('wishList' , $wishList);
        // dd($session->get('wishList'));

        //Ajout => redirection wishList
        return $this->redirectToRoute('app_wishList', [], Response::HTTP_SEE_OTHER);

    }

    #[Route('/wishList/remove/{idproduit}', name: 'wishList_remove')]
    public function remove($idproduit, SessionInterface $session ): Response
    {
        $wishList = $session->get('wishList' , []);

        if(!empty($wishList[$idproduit]))
        {
            unset($wishList[$idproduit]);
        }
        
        $session->set('wishList' , $wishList);

        return $this->redirectToRoute('app_wishList', [], Response::HTTP_SEE_OTHER);

    }


}

