<?php

namespace App\Controller;

use App\Form\EventDetailsType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EventbController extends AbstractController
{
    #[Route('/eventb', name: 'app_events')]
    public function index(Request $request): Response
    {
        $form = $this->createForm(EventDetailsType::class);

        // Handle the form submission in a POST request
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            // TODO: Add your form handling logic here
        }

        return $this->render('dashboard\eventsback.html.twig', [
            'controller_name' => 'EventbController',
            'form' => $form->createView(),
        ]);
    }
}