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
use App\Entity\EventParticipants;
use App\Repository\EventParticipantsRepository;
use App\Repository\EventDetailsRepository;



class EventbController extends AbstractController
{   private $eventParticipantsRepository;

    public function __construct(EventParticipantsRepository $eventParticipantsRepository)
    {
        $this->eventParticipantsRepository = $eventParticipantsRepository;
    }
    #[Route('/add_event', name: 'app_events')]
    public function add_event(Request $request, SessionInterface $session, ManagerRegistry $registry): Response
    {
        $user = $session->get('user');
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
    
    $events = $registry->getRepository(EventDetails::class)->findAll();

    $eventsWithUserStatus = [];
    foreach ($events as $event) {
        $isUserParticipant = $this->eventParticipantsRepository->findOneBy([
            'user_id' => $user->getId(),
            'event_details_id' => $event->getId()
        ]) !== null;

        $eventsWithUserStatus[] = [
            'event' => $event,
            'isUserParticipant' => $isUserParticipant
        ];
    }

    return $this->render('main\gestion_events/showeventsf.html.twig', [
        'controller_name' => 'EventbController',
        'events' => $eventsWithUserStatus,
        'user' => $user,
    ]);
}
    #user join event by clicking on join
    #[Route('/eventf/join/{id}', name: 'event_join')]
    public function join($id, ManagerRegistry $registry, SessionInterface $session): Response
    {
        $user = $session->get('user');
        
        $event = $registry->getRepository(EventDetails::class)->find($id);
        $event->setNbPlaces($event->getNbPlaces()-1);
        $entityManager = $registry->getManager();
        $entityManager->flush();
        $eventParticipant = new EventParticipants();
        $eventParticipant->setUserId($user->getId());
        $eventParticipant->setEventDetailsId($id);
        $entityManager->persist($eventParticipant);
        $entityManager->flush();
        return $this->redirectToRoute('app_eventsf');



}
#[Route('/eventf/leave/{id}', name: 'event_leave')]
public function leaveEvent($id, ManagerRegistry $registry, SessionInterface $session)
{
    $user = $session->get('user');
   
    $event = $registry->getRepository(EventDetails::class)->find($id);
        $event->setNbPlaces($event->getNbPlaces()+1);

    $eventParticipant = $registry->getRepository(EventParticipants::class)->findOneBy([
        'user_id' => $user->getId(),
        'event_details_id' => $id
    ]);

    if ($eventParticipant) {
        $entityManager = $registry->getManager();
        $entityManager->remove($eventParticipant);
        $entityManager->flush();
    }

    return $this->redirectToRoute('app_eventsf');
}

}