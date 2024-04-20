<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Picqer\Barcode\BarcodeGeneratorHTML;

class GenerateController extends AbstractController
{
    // #[Route('/generate', name: 'app_generate')]
    // public function index(): Response
    // {
    //     return $this->render('generate/index.html.twig', [
    //         'controller_name' => 'GenerateController',
    //     ]);
    // }

    #[Route('/generate', name: 'app_generate')]
    public function generateBarcode(Request $request): Response
    {
        // Récupérer la chaîne de texte à partir de la requête
        $text = $request->query->get('text');

        // Diviser la chaîne en ses composants d'origine (ID et prix)
        $productInfo = explode(',', $text);
        $productId = $productInfo[0];
        $productPrice = $productInfo[1];
        $productPromo = $productInfo[2];
        
        // Calculer le prix du produit avec la promo
        $productPriceWithPromo = $productPrice * $productPromo;
        
        // Créer une nouvelle chaîne avec l'ID du produit sur une ligne et le prix sur une autre ligne
        $barcodeText = $productId . "\n" . $productPriceWithPromo;

        // Utilisez ces informations pour générer le code-barres approprié
        $generator = new BarcodeGeneratorHTML();
        $code = $generator->getBarcode($barcodeText, BarcodeGeneratorHTML::TYPE_CODE_128);

        return $this->render('generate/index.html.twig', [
            'code' => $code,
            'productId' => $productId,
            'productPriceWithPromo' => $productPriceWithPromo,
        ]);
    }


}
