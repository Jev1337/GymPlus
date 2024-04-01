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
use Doctrine\ORM\EntityManagerInterface;
use App\Form\EventDetailsEditType;






class EventbController extends AbstractController
{   private $eventParticipantsRepository;

    public function __construct(EventParticipantsRepository $eventParticipantsRepository)
    {
        $this->eventParticipantsRepository = $eventParticipantsRepository;
    }
    #[Route('/add_event', name: 'app_events')]
    public function add_event(Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
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
    public function eventb(ManagerRegistry $registry): Response
    {   
        $user = $this->getUser();
        
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
    if (!$event) {
        throw $this->createNotFoundException('No event found for id '.$id);
    }
    $entityManager = $registry->getManager();
    $entityManager->remove($event);
    $entityManager->flush();
    return $this->redirectToRoute('eventb');
}
    #[Route('/eventb/edit/{id}', name: 'event_edit')]
    public function edit($id, Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $event = $registry->getRepository(EventDetails::class)->find($id);
        
        if (!$event) {
            throw $this->createNotFoundException('No event found for id '.$id);
        }

        $form = $this->createForm(EventDetailsEditType::class, $event);
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
            
        ]);
    }
    #[Route('/eventf', name: 'app_eventsf')]
    public function eventf(ManagerRegistry $registry): Response
{    /** @var User $user */
    $user = $this->getUser();
    
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
    public function join($id, ManagerRegistry $registry): Response
    {   /** @var User $user */
        $user = $this->getUser();
        
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
public function leaveEvent($id, ManagerRegistry $registry)
{   /** @var User $user */
    $user = $this->getUser();
   
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
#[Route('event_participants/{id}', name: 'eventParticipant')]
public function eventParticipants($id, ManagerRegistry $registry): Response
{
    $user = $this->getUser();
   

    $event = $registry->getRepository(EventDetails::class)->find($id);
    $eventParticipants = $registry->getRepository(EventParticipants::class)->findBy(['event_details_id' => $id]);

    // Fetch the User entities associated with the EventParticipants entities
    $participantsUsers = array_map(function($participant) use ($registry) {
        $userId = $participant->getUserId();
        return $registry->getRepository(User::class)->find($userId);
    }, $eventParticipants);

    return $this->render('dashboard\gestion_events/event_participants.html.twig', [
        'controller_name' => 'EventbController',
        'event' => $event,
        'eventParticipants' => $eventParticipants,
        'participantsUsers' => $participantsUsers,
        'user' => $user,
    ]);
}
#[Route('/eventf/kick/{userId}/{eventDetailsId}', name: 'eventParticipant_kick')]
public function kick($userId, $eventDetailsId, ManagerRegistry $registry): Response
{
    // Use both parts of the composite key to find the EventParticipants entity
    $eventParticipant = $registry->getRepository(EventParticipants::class)->findOneBy([
        'user_id' => $userId,
        'event_details_id' => $eventDetailsId,
    ]);

    if (!$eventParticipant) {
        throw $this->createNotFoundException('No participant found for id '.$userId);
    }
 
    $event = $registry->getRepository(EventDetails::class)->find($eventDetailsId);
    $event->setNbPlaces($event->getNbPlaces()+1);

    $entityManager = $registry->getManager();
    $entityManager->remove($eventParticipant);
    $entityManager->flush();

    return $this->redirectToRoute('eventParticipant', ['id' => $eventDetailsId]);
}
#rewards hub
#[Route('/rewards', name: 'rewards')]
public function rewards(ManagerRegistry $registry, SessionInterface $session): Response
{
    $user = $session->get('user');
    return $this->render('main\gestion_events\event_hub.html.twig', [
        'controller_name' => 'EventbController',
        'user' => $user,
    ]);
}
#rewards hub
#[Route('/rewards/whey', name: 'whey')]
public function claimWhey(Request $request, EntityManagerInterface $entityManager): Response
{   /** @var User $user */
    $user = $this->getUser();
    if ($user === null) {
        return new Response('No user is logged in');
    }

    if ($user->getEventPoints() >= 3500) {
        $user->setEventPoints($user->getEventPoints() - 3500);

        // Update user points in the database
        $entityManager->persist($user);
        $entityManager->flush();

        return new Response('Claim successful');
    } else {
        return new Response('Not enough points');
    }
}
#[Route('/rewards/belt', name: 'belt')]
public function claimBelt(Request $request, EntityManagerInterface $entityManager): Response
{   /** @var User $user */
    $user = $this->getUser();
    if ($user === null) {
        return new Response('No user is logged in');
    }

    if ($user->getEventPoints() >= 2500) {
        $user->setEventPoints($user->getEventPoints() - 2500);

        // Update user points in the database
        $entityManager->persist($user);
        $entityManager->flush();

        return new Response('Claim successful');
    } else {
        return new Response('Not enough points');
    }
}
    #[Route('/rewards/bag', name: 'bag')]
    public function claimBag(Request $request, EntityManagerInterface $entityManager): Response
    {   /** @var User $user */
        $user = $this->getUser();
        if ($user === null) {
            return new Response('No user is logged in');
        }
    
        if ($user->getEventPoints() >= 3000) {
            $user->setEventPoints($user->getEventPoints() - 3000);
    
            // Update user points in the database
            $entityManager->persist($user);
            $entityManager->flush();
    
            return new Response('Claim successful');
        } else {
            return new Response('Not enough points');
        }
    }
}