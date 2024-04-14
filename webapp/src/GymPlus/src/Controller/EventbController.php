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
use App\Entity\BlackListed;
use Endroid\QrCode\Builder\BuilderInterface;
use Endroid\QrCodeBundle\Response\QrCodeResponse;
use Symfony\Component\HttpFoundation\JsonResponse;
use Eluceo\iCal\Component\Calendar;
use Eluceo\iCal\Component\Event;
use Endroid\QrCode\QrCode;

use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\Encoding\Encoding;
use Endroid\QrCode\ErrorCorrectionLevel;
use Endroid\QrCode\Label\LabelAlignment;
use Endroid\QrCode\Label\Font\NotoSans;
use Endroid\QrCode\RoundBlockSizeMode;
use Endroid\QrCode\Writer\PngWriter;


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
    
            return $this->redirectToRoute('eventb'); 
        }
        return $this->render('dashboard\gestion_events/add_event.html.twig', [
            'controller_name' => 'EventbController',
            'user' => $user, 
            'form' => $form->createView(),
            
        ]);
    }
    #[Route('/eventb', name: 'eventb')]
    public function eventb(ManagerRegistry $registry): Response
    {      /** @var User $user */
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
{
    
    /** @var User $user */
    $user = $this->getUser();

    // Get the EventParticipants repository
    $eventParticipantsRepository = $registry->getRepository(EventParticipants::class);

    // Get the next event date
    $userId = $user->getId();
    $nextEventDate = $this->eventParticipantsRepository->getNextEventDate($userId);

    // Check if the user is in the blacklist
    $blacklist = $registry->getRepository(BlackListed::class)->findOneBy(['idUser' => $user]);

    if ($blacklist) {
        // Redirect to a different route
        return $this->redirectToRoute('app_home');
    }

    $userPoints = $user->getEventPoints();
    $events = $registry->getRepository(EventDetails::class)->findAll();

    $eventsWithUserStatus = [];
    foreach ($events as $event) {
        $isUserParticipant = $this->eventParticipantsRepository->findOneBy([
            'user_id' => $user->getId(),
            'event_details_id' => $event->getId()
        ]) !== null;

        $eventsWithUserStatus[] = [
            'event' => $event,
            'isUserParticipant' => $isUserParticipant,
        ];
    }

    return $this->render('main\gestion_events/showeventsf.html.twig', [
        'controller_name' => 'EventbController',
        'events' => $eventsWithUserStatus,
        'user' => $user,
        'user_points' => $userPoints,
        'nextEventDate' => $nextEventDate, // Pass the next event date to the template
    ]);
}

    #user join event by clicking on join
    #user gets 100 points for joining an event 

#[Route('/eventf/join/{id}', name: 'event_join')]
public function join($id, ManagerRegistry $registry, BuilderInterface $qrCodeBuilder): Response
{
    /** @var User $user */
    $user = $this->getUser();
    
    $eventDetails = $registry->getRepository(EventDetails::class)->find($id);
    $eventDetails->setNbPlaces($eventDetails->getNbPlaces()-1);
    $user->setEventPoints($user->getEventPoints() + 100);
    $entityManager = $registry->getManager();
    $entityManager->persist($user);
    $entityManager->flush();
    $entityManager->persist($eventDetails);
    $entityManager->flush();
    $eventParticipant = new EventParticipants();
    $eventParticipant->setUserId($user->getId());
    $eventParticipant->setEventDetailsId($id);
    $entityManager->persist($eventParticipant);
    $entityManager->flush();

    // Create the iCalendar event
    $event = (new \Eluceo\iCal\Domain\Entity\Event())
        ->setSummary($eventDetails->getName())
        ->setDescription($eventDetails->getType())
        ->setOccurrence(
            new \Eluceo\iCal\Domain\ValueObject\SingleDay(
                new \Eluceo\iCal\Domain\ValueObject\Date(
                    \DateTimeImmutable::createFromFormat(
                        'Y-m-d', 
                        $eventDetails->getEventDate()->format('Y-m-d')
                    )
                )
            )
        );

    // Create the iCalendar file
    $calendar = new \Eluceo\iCal\Domain\Entity\Calendar([$event]);
    $componentFactory = new \Eluceo\iCal\Presentation\Factory\CalendarFactory();
    $iCalendarComponent = $componentFactory->createCalendar($calendar);
      /*  
    // Create the QR code
    $result = Builder::create()
        ->writer(new PngWriter())
        ->writerOptions([])
        ->data((string)$iCalendarComponent)
        ->encoding(new Encoding('UTF-8'))
        ->errorCorrectionLevel(ErrorCorrectionLevel::High)
        ->size(300)
        ->margin(10)
        ->roundBlockSizeMode(RoundBlockSizeMode::Margin)
        ->validateResult(false)
        ->build();

    $response = new QrCodeResponse($result);

    return $response;*/
    return $this->redirectToRoute('app_eventsf');
}

   
#[Route('/eventf/leave/{id}', name: 'event_leave')]
public function leaveEvent($id, ManagerRegistry $registry)
{   /** @var User $user */
    $user = $this->getUser();
   
    $event = $registry->getRepository(EventDetails::class)->find($id);
        $event->setNbPlaces($event->getNbPlaces()+1);
        $user->setEventPoints($user->getEventPoints() - 100);
        $entityManager = $registry->getManager();
        $entityManager->persist($user);
        $entityManager->flush();
        $entityManager->persist($event);
        $entityManager->flush();
    


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

    // Get the User entity of the kicked user and reduce its points
    
    $kickedUser = $registry->getRepository(User::class)->find($userId);
    $kickedUser->setEventPoints($kickedUser->getEventPoints() - 100);

    $event = $registry->getRepository(EventDetails::class)->find($eventDetailsId);
    $event->setNbPlaces($event->getNbPlaces()+1);

    $entityManager = $registry->getManager();
    $entityManager->remove($eventParticipant);
    $entityManager->persist($kickedUser); // Persist the changes to the User entity
    $entityManager->flush();

    return $this->redirectToRoute('eventParticipant', ['id' => $eventDetailsId]);
}
#rewards hub
#[Route('/rewards', name: 'rewards')]
public function rewards(ManagerRegistry $registry, SessionInterface $session): Response
{
    /** @var User $user */
    $user = $this->getUser();

    // Check if the user is in the blacklist
    $blacklist = $registry->getRepository(BlackListed::class)->findOneBy(['idUser' => $user]);
    if ($blacklist) {
        // Add a flash message to inform the user
       //$this->addFlash('warning', 'You are banned until ' . $blacklist->getEndBan()->format('Y-m-d') . '. You cannot access this page.');
       
        // Redirect to a different route (replace 'banned_route' with the actual route name)
        return $this->redirectToRoute('app_home');
    }
    return $this->render('main\gestion_events\event_hub.html.twig', [
        'controller_name' => 'EventbController',
        'user' => $user,
    ]);
}

#rewards hub
#[Route('/rewards/whey', name: 'whey')]
public function claimWhey(Request $request, EntityManagerInterface $entityManager): Response
{
    /** @var User $user */
    $user = $this->getUser();
    
    if ($user->getEventPoints() >= 3500) {
        $user->setEventPoints($user->getEventPoints() - 3500);

       
        $entityManager->persist($user);
        $entityManager->flush();

        $this->addFlash('claimStatus', 'success');
    } else {
        $this->addFlash('claimStatus', 'error');
    }

    return $this->redirectToRoute('rewards'); 
}

#[Route('/rewards/belt', name: 'belt')]
public function claimBelt(Request $request, EntityManagerInterface $entityManager): Response
{
    /** @var User $user */
    $user = $this->getUser();
    

    if ($user->getEventPoints() >= 2500) {
        $user->setEventPoints($user->getEventPoints() - 2500);

        // Update user points in the database
        $entityManager->persist($user);
        $entityManager->flush();

        $this->addFlash('success', 'Claim successful');
    } else {
        $this->addFlash('error', 'Not enough points');
    }

    return $this->redirectToRoute('rewards'); // Redirect back to the rewards page
}

#[Route('/rewards/bag', name: 'bag')]
public function claimBag(Request $request, EntityManagerInterface $entityManager): Response
{
    /** @var User $user */
    $user = $this->getUser();
   

    if ($user->getEventPoints() >= 3000) {
        $user->setEventPoints($user->getEventPoints() - 3000);

        // Update user points in the database
        $entityManager->persist($user);
        $entityManager->flush();

        $this->addFlash('success', 'Claim successful');
    } else {
        $this->addFlash('error', 'Not enough points');
    }

    return $this->redirectToRoute('rewards'); // Redirect back to the rewards page
 }
 #[Route('/blacklised', name: 'blacklised')]
 public function showblacklisted(ManagerRegistry $registry)
 {
     $user = $this->getUser();
     $users = $registry->getRepository(User::class)->findBy(['role' => 'client']); // Fetch only users that are clients
     $blacklisted = $registry->getRepository(BlackListed::class)->findAll();
     $blacklistedUsers = [];
     foreach ($blacklisted as $blacklist) {
         $blacklistedUsers[] = $blacklist->getIdUser()->getId();
     }
     return $this->render('dashboard\gestion_events/blacklisted.html.twig', [
         'controller_name' => 'EventbController',
         'users' => $users,
         'blacklistedUsers' => $blacklistedUsers,
         'user' => $user,
         'blacklisted' => $blacklisted,
     ]);
 }
 #[Route('/ban/{id}/{endBan}', name: 'ban')]
 public function ban($id, $endBan, ManagerRegistry $registry)
 {
     $user = $registry->getRepository(User::class)->find($id);
     $blacklist = new BlackListed();
     $blacklist->setIdUser($user);
     $blacklist->setStartBan(new \DateTime());
 
     // Convert the endBan string to a DateTime object
     $endBanDate = \DateTime::createFromFormat('Y-m-d', $endBan);
     $blacklist->setEndBan($endBanDate);
 
     $entityManager = $registry->getManager();
     $entityManager->persist($blacklist);
     $entityManager->flush();
     return $this->redirectToRoute('blacklised');
 }
    #[Route('/unban/{id}', name: 'unban')]
    public function unban($id, ManagerRegistry $registry)
    {
        $user = $registry->getRepository(User::class)->find($id);
        $blacklist = $registry->getRepository(BlackListed::class)->findOneBy(['idUser' => $user]);
        $entityManager = $registry->getManager();
        $entityManager->remove($blacklist);
        $entityManager->flush();
        return $this->redirectToRoute('blacklised');
    }
    
    
  
   
   



}
