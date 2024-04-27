<?php

namespace App\Controller;

use App\Repository\FactureRepository;
use Stripe\Checkout\Session;
use Stripe\Stripe;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;

class StripePaymentController extends AbstractController
{
    #[Route('/stripe/payment', name: 'app_stripe_payment')]
    public function index(): Response
    {
        return $this->render('stripe_payment/index.html.twig', [
            'controller_name' => 'StripePaymentController',
        ]);
    }

    #[Route('/checkout', name: 'checkout')]
    public function checkout($stripeSK, FactureRepository $factureRepository): Response
    {
        Stripe::setApiKey($stripeSK);
    
        $result = $factureRepository->findMaxFacture();
    
        if (!empty($result)) {
            $derniereFactureId = $result[0]['maxId'];
            
            $derniereFacture = $factureRepository->find($derniereFactureId);
    
            if ($derniereFacture !== null) {
                $montantTotal = $derniereFacture->getPrixtatalpaye();
    
                $session = Session::create([
                    'payment_method_types' => ['card'],
                    'line_items' => [
                        [
                            'price_data' => [
                                'currency' => 'usd',
                                'product_data' => [
                                    'name' => 'Invoice',
                                ],
                                'unit_amount' => $montantTotal * 100, // Convertir le montant total en cents (Stripe utilise les centimes)
                            ],
                            'quantity' => 1, 
                        ]
                    ],
                    'mode' => 'payment',
                    'success_url' => $this->generateUrl('success_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
                    'cancel_url' => $this->generateUrl('cancel_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
                ]);
    
                return $this->redirect($session->url, 303);
            }
        }
    
        return $this->redirectToRoute('error_page');
    }

    #[Route('/success-url', name: 'success_url')]
    public function successUrl(): Response
    {
        return $this->render('stripe_payment/success.html.twig', []);
    }

    #[Route('/cancel-url', name: 'cancel_url')]
    public function cancelUrl(): Response
    {
        return $this->render('stripe_payment/cancel.html.twig', []);
    }
}
