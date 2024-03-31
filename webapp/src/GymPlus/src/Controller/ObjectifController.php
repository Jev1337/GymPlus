<?php
namespace App\Controller;
use App\Form\ObjectifType;
use App\Entity\Objectif;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Entity\User; 
use Doctrine\Persistence\ManagerRegistry;

class ObjectifController extends AbstractController
{
    #[Route('/add_Objectif', name: 'app_objectif')]
    public function add_Objectif(Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $obj = new Objectif();
        $form = $this->createForm(ObjectifType::class, $obj);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $registry->getManager();
            $entityManager->persist($obj);
            $entityManager->flush();
            return $this->redirectToRoute('app_objectif'); 
        }
        return $this->render('main\ObjectifFront.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user, 
            'form' => $form->createView(),
        ]);
    }



    #[Route('/Schedule_Objectif', name: 'app_Schedule_objectif')]
    public function Schedule_Objectif(Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $userId = $user->getId();
        $entityManager = $registry->getManager();
        $objectifs = $entityManager->getRepository(Objectif::class)->findBy(['userid' => $userId]);

        return $this->render('main/displayObjectif.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'objectifs' => $objectifs,
        ]);
    }
    
/*
    #[Route('/eventb', name: 'eventb')]
    public function eventb(ManagerRegistry $registry, SessionInterface $session): Response
    {   
        $user = $session->get('user');
        if (!$user instanceof User) {
            throw new \Exception('No user in session');
        }
        
        $events = $registry->getRepository(Objectif::class)->findAll();
    
        return $this->render('dashboard\gestion_events/eventsback.html.twig', [
            'controller_name' => 'EventbController',
            'events' => $events, 
            'user' => $user,
        ]);
    }
    #[Route('/eventb/delete/{id}', name: 'event_delete')]
    public function delete($id, ManagerRegistry $registry): Response
    {
        $event = $registry->getRepository(Objectif::class)->find($id);
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
        $event = $registry->getRepository(Objectif::class)->find($id);
        $form = $this->createForm(ObjectifType::class, $event);
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

*/
}