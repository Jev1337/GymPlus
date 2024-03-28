<?php

namespace App\Controller;


use App\Form\EventDetailsType;
use App\Entity\EventDetails;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Entity\User; 
use Doctrine\Persistence\ManagerRegistry;


class EventbController extends AbstractController
{
    #[Route('/add_event', name: 'app_events')]
    public function add_event(Request $request, SessionInterface $session, ManagerRegistry $registry): Response
    {
        $user = $session->get('user');
        if (!$user instanceof User) {
            throw new \Exception('No user in session');
        }

        $event = new EventDetails();
        $form = $this->createForm(EventDetailsType::class, $event);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            
            $event->setNbTotal($event->getNbPlaces());
    
            $entityManager = $registry->getManager();
            $entityManager->persist($event);
            $entityManager->flush();
    
            return $this->redirectToRoute('app_events'); 
        }
        return $this->render('dashboard\gestion_events/add_event.html.twig', [
            'controller_name' => 'EventbController',
            'user' => $user, 
            'form' => $form->createView(),
            
        ]);
    }
    #[Route('/eventb', name: 'eventb')]
    public function eventb(ManagerRegistry $registry, SessionInterface $session): Response
    {   
        $user = $session->get('user');
        if (!$user instanceof User) {
            throw new \Exception('No user in session');
        }
        
        $events = $registry->getRepository(EventDetails::class)->findAll();
    
        return $this->render('dashboard\gestion_events/eventsback.html.twig', [
            'controller_name' => 'EventbController',
            'events' => $events, 
            'user' => $user,
        ]);
    }
    #[Route('/eventb/delete/{id}', name: 'event_delete')]
    public function delete($id, ManagerRegistry $registry): Response
    {
        $event = $registry->getRepository(EventDetails::class)->find($id);
        $entityManager = $registry->getManager();
        $entityManager->remove($event);
        $entityManager->flush();
        return $this->redirectToRoute('eventb');
    }
    #[Route('/eventb/edit/{id}', name: 'event_edit')]
    public function edit($id, Request $request, ManagerRegistry $registry, SessionInterface $session): Response
    {    $user = $session->get('user');
        if (!$user instanceof User) {
            throw new \Exception('No user in session');
        }
        $event = $registry->getRepository(EventDetails::class)->find($id);
        $form = $this->createForm(EventDetailsType::class, $event);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $registry->getManager();
            $entityManager->flush();
            return $this->redirectToRoute('eventb');
        }
        return $this->render('dashboard\gestion_events/edit_event.html.twig', [
            'controller_name' => 'EventbController',
            'form' => $form->createView(),
            'user' => $user,
            'isEditAction' => true,
        ]);
    }
    #[Route('/eventf', name: 'app_eventsf')]
    public function eventf(ManagerRegistry $registry, SessionInterface $session): Response
    {   
        $user = $session->get('user');
        if (!$user instanceof User) {
            throw new \Exception('No user in session');
        }
        
        $events = $registry->getRepository(EventDetails::class)->findAll();
    
        return $this->render('main\gestion_events/showeventsf.html.twig', [
            'controller_name' => 'EventbController',
            'events' => $events, 
            'user' => $user,
        ]);
    }


}