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

use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;


class EventbController extends AbstractController
{   private $eventParticipantsRepository;
    private $eventdetailsRepository;

    public function __construct(EventParticipantsRepository $eventParticipantsRepository,EventDetailsRepository $eventDetailsRepository)
    {
        $this->eventParticipantsRepository = $eventParticipantsRepository;
        $this->eventdetailsRepository = $eventDetailsRepository;

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
        
        $events = $registry->getRepository(EventDetails::class)->findFutureEvents();
    
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

    $eventParticipantsRepository = $registry->getRepository(EventParticipants::class);

    $userId = $user->getId();
    $nextEventDate = $this->eventParticipantsRepository->getNextEventDate($userId);
    
    $blacklist = $registry->getRepository(BlackListed::class)->findOneBy(['idUser' => $user]);

    if ($blacklist) {
     
        $this->addFlash('warning', [
            'message' => 'You are banned until ' . $blacklist->getEndBan()->format('Y-m-d') . '. You cannot access this page.',
            'redirectUrl' => $this->generateUrl('app_home')
        ]);

    }

    $userPoints = $user->getEventPoints();

    $events = $registry->getRepository(EventDetails::class)->findFutureEvents();
    
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
        'nextEventDate' => $nextEventDate,
    ]);
}

    

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
      
    $calendar = new \Eluceo\iCal\Domain\Entity\Calendar([$event]);
    $componentFactory = new \Eluceo\iCal\Presentation\Factory\CalendarFactory();
    $iCalendarComponent = $componentFactory->createCalendar($calendar);
      
    
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

    // Save the QR code as an image
$qrCodePath = $this->getParameter('kernel.project_dir').'/public/main/images/qrcodes/qrcode.png';

// Check if the directory is writable
if (!is_writable(dirname($qrCodePath))) {
    throw new \Exception('The directory is not writable: ' . dirname($qrCodePath));
}

// Try to save the QR code and catch any errors
try {
    $result->saveToFile($qrCodePath);
} catch (\Exception $e) {
    throw new \Exception('Failed to save QR code: ' . $e->getMessage());
}
    
        return new JsonResponse(['qrCodePath' => '/main/images/qrcodes/qrcode.png']);
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
    
    $eventParticipant = $registry->getRepository(EventParticipants::class)->findOneBy([
        'user_id' => $userId,
        'event_details_id' => $eventDetailsId,
    ]);

    if (!$eventParticipant) {
        throw $this->createNotFoundException('No participant found for id '.$userId);
    }

    
    
    $kickedUser = $registry->getRepository(User::class)->find($userId);
    $kickedUser->setEventPoints($kickedUser->getEventPoints() - 100);

    $event = $registry->getRepository(EventDetails::class)->find($eventDetailsId);
    $event->setNbPlaces($event->getNbPlaces()+1);

    $entityManager = $registry->getManager();
    $entityManager->remove($eventParticipant);
    $entityManager->persist($kickedUser); 
    $entityManager->flush();

    return $this->redirectToRoute('eventParticipant', ['id' => $eventDetailsId]);
}
#rewards hub
#[Route('/rewards', name: 'rewards')]
public function rewards(ManagerRegistry $registry, SessionInterface $session): Response
{
    /** @var User $user */
    $user = $this->getUser();
    $userPoints = $user->getEventPoints();
    // Check if the user is in the blacklist
    $blacklist = $registry->getRepository(BlackListed::class)->findOneBy(['idUser' => $user]);
    if ($blacklist) {
        
        $this->addFlash('warning', [
            'message' => 'You are banned until ' . $blacklist->getEndBan()->format('Y-m-d') . '. You cannot access this page.',
            'redirectUrl' => $this->generateUrl('app_home')
        ]);
        
       
    }
    return $this->render('main\gestion_events\event_hub.html.twig', [
        'controller_name' => 'EventbController',
        'user' => $user,
        'user_points' => $userPoints,
    ]);
}

#rewards hub

#[Route('/rewards/whey', name: 'whey')]
public function claimWhey(Request $request, EntityManagerInterface $entityManager,MailerInterface $mailer): Response
{
    /** @var User $user */
    $user = $this->getUser();
    $emailu = $user->getEmail();
    
    if ($user->getEventPoints() >= 3500) {
        $user->setEventPoints($user->getEventPoints() - 3500);
        $entityManager->persist($user);
        $entityManager->flush();
        $this->addFlash('success', 'Successfully claimed GymPluswhey!');



       
$userInfo = 'ID: ' . $user->getId() . ', First Name: ' . $user->getFirstName() . ', Last Name: ' . $user->getLastName() . ', Username: ' . $user->getUsername();

$result = Builder::create()
    ->writer(new PngWriter())
    ->writerOptions([])
    ->data($userInfo)
    ->encoding(new Encoding('UTF-8'))
    ->errorCorrectionLevel(ErrorCorrectionLevel::High)
    ->size(300) 
    ->margin(10)  
    ->roundBlockSizeMode(RoundBlockSizeMode::Margin)
    ->validateResult(false)
    ->build();


    // Save the QR code as an image
$qrCodePath = $this->getParameter('kernel.project_dir').'/public/main/images/qrcodes/qrcode1.png';

// Check if the directory is writable
if (!is_writable(dirname($qrCodePath))) {
    throw new \Exception('The directory is not writable: ' . dirname($qrCodePath));
}

// Try to save the QR code and catch any errors
try {
    $result->saveToFile($qrCodePath);
} catch (\Exception $e) {
    throw new \Exception('Failed to save QR code: ' . $e->getMessage());
}
    


        $email = (new Email())
            ->from('gymplus-noreply@grandelation.com')
            ->to($emailu)
            ->subject('GymPlus Whey')
            ->text('Thank you for claiming GymPlus Whey!')
            ->attachFromPath($qrCodePath);
        $mailer->send($email);

    } else {
        $this->addFlash('error', 'Not enough points');
    }

    return $this->redirectToRoute('rewards'); 
}


#[Route('/rewards/belt', name: 'belt')]
public function claimBelt(Request $request, EntityManagerInterface $entityManager,MailerInterface $mailer): Response
{
    /** @var User $user */
    $user = $this->getUser();
    $emailu = $user->getEmail();

    if ($user->getEventPoints() >= 2500) {
        $user->setEventPoints($user->getEventPoints() - 2500);

        // Update user points in the database
        $entityManager->persist($user);
        $entityManager->flush();
        $this->addFlash('success', 'Successfully claimed GymPlus Belt!');
        $userInfo = 'ID: ' . $user->getId() . ', First Name: ' . $user->getFirstName() . ', Last Name: ' . $user->getLastName() . ', Username: ' . $user->getUsername();

$result = Builder::create()
    ->writer(new PngWriter())
    ->writerOptions([])
    ->data($userInfo)
    ->encoding(new Encoding('UTF-8'))
    ->errorCorrectionLevel(ErrorCorrectionLevel::High)
    ->size(300) 
    ->margin(10)  
    ->roundBlockSizeMode(RoundBlockSizeMode::Margin)
    ->validateResult(false)
    ->build();


    // Save the QR code as an image
$qrCodePath = $this->getParameter('kernel.project_dir').'/public/main/images/qrcodes/qrcode1.png';


if (!is_writable(dirname($qrCodePath))) {
    throw new \Exception('The directory is not writable: ' . dirname($qrCodePath));
}


try {
    $result->saveToFile($qrCodePath);
} catch (\Exception $e) {
    throw new \Exception('Failed to save QR code: ' . $e->getMessage());
}
    


        $email = (new Email())
            ->from('gymplus-noreply@grandelation.com')
            ->to($emailu)
            ->subject('GymPlus Whey')
            ->text('Thank you for claiming GymPlus Whey!')
            ->attachFromPath($qrCodePath); 
        $mailer->send($email);
    } else {
        $this->addFlash('error', 'Not enough points');
    }

    return $this->redirectToRoute('rewards');
}

#[Route('/rewards/bag', name: 'bag')]
public function claimBag(Request $request, EntityManagerInterface $entityManager,MailerInterface $mailer): Response
{
    /** @var User $user */
    $user = $this->getUser();
    $emailu=$user->getEmail();

    if ($user->getEventPoints() >= 3000) {
        $user->setEventPoints($user->getEventPoints() - 3000);

       
        $entityManager->persist($user);
        $entityManager->flush();
        $this->addFlash('success', 'Successfully claimed GymPlus Bag!');

        $userInfo = 'ID: ' . $user->getId() . ', First Name: ' . $user->getFirstName() . ', Last Name: ' . $user->getLastName() . ', Username: ' . $user->getUsername();

$result = Builder::create()
    ->writer(new PngWriter())
    ->writerOptions([])
    ->data($userInfo)
    ->encoding(new Encoding('UTF-8'))
    ->errorCorrectionLevel(ErrorCorrectionLevel::High)
    ->size(300) 
    ->margin(10)  
    ->roundBlockSizeMode(RoundBlockSizeMode::Margin)
    ->validateResult(false)
    ->build();


    // Save the QR code as an image
$qrCodePath = $this->getParameter('kernel.project_dir').'/public/main/images/qrcodes/qrcode1.png';

// Check if the directory is writable
if (!is_writable(dirname($qrCodePath))) {
    throw new \Exception('The directory is not writable: ' . dirname($qrCodePath));
}

// Try to save the QR code and catch any errors
try {
    $result->saveToFile($qrCodePath);
} catch (\Exception $e) {
    throw new \Exception('Failed to save QR code: ' . $e->getMessage());
}
    


        $email = (new Email())
            ->from('gymplus-noreply@grandelation.com')
            ->to($emailu)
            ->subject('GymPlus Whey')
            ->text('Thank you for claiming GymPlus Whey!')
            ->attachFromPath($qrCodePath); // Attach the QR code
        $mailer->send($email);
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
    #[Route('/past-events', name: 'past_events')]
        public function pastEvents(ManagerRegistry $registry)
    {
        $user = $this->getUser();

        $pastEvents = $registry->getRepository(EventParticipants::class)->findPastEventsByUser($user->getId());
        $votes = [];
        foreach ($pastEvents as $pastEvent) {
            $votes[$pastEvent['id']] = $registry->getRepository(EventParticipants::class)->findVoteByUser($pastEvent, $user->getId());
        }
        $valid = !empty($votes);
        //show user past events the events user joined in the past
        return $this->render('main\gestion_events/past_events.html.twig', [
            'controller_name' => 'EventbController',
            'pastEvents' => $pastEvents,
            'user' => $user,
            'valid'=> $valid,
            'votes'=> $votes, 
        ]);
    }

 
    #[Route('/event_vote/{id}', name: 'event_vote')]
    public function rateEvent($id, Request $request, EventParticipantsRepository $eventParticipantsRepository, EntityManagerInterface $entityManager)
    {
        $user = $this->getUser();
        $event = $this->eventdetailsRepository->find($id);
    
        $eventParticipant = $eventParticipantsRepository->findOneBy([
            'user_id' => $user->getId(),
            'event_details_id' => $event->getId(),
        ]);
    
        if ($eventParticipant) {
            $rating = $request->request->get('rating');
            $rating = filter_var($rating, FILTER_VALIDATE_INT);
    
            if ($rating === false) {
                $this->addFlash('error', 'Invalid rating. Please enter a valid number.');
                return $this->redirectToRoute('past_events');
            }
    
            $eventParticipant->setRate($rating);
    
          
            $entityManager->persist($eventParticipant);
            $entityManager->flush();
        }
    
        return $this->redirectToRoute('past_events');
    }
    

#[Route('/past-events-history', name: 'past_events_history')]
public function pastEventsHistory(ManagerRegistry $registry)
{
    $user = $this->getUser();

    $pastEvents = $registry->getRepository(EventDetails::class)->findAllPastEvents();
    $rates = [];
    foreach ($pastEvents as $event) {
        $rate[$event->getId()] = $registry->getRepository(EventDetails::class)->getEventRate($event->getId());
    }

    return $this->render('dashboard\gestion_events/past_events.html.twig', [
        'controller_name' => 'EventbController',
        'pastEvents' => $pastEvents,
        'user' => $user,
        'rate' => $rate,
    ]);
}


}
