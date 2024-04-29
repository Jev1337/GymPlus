<?php

namespace App\Controller;

use App\Entity\Conversation;
use App\Entity\ParticipantMessanger;
use App\Repository\ConversationRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\WebLink\Link;
use Symfony\Component\Mercure\HubInterface;
use Symfony\Component\Mercure\Update;

class ConversationController extends AbstractController
{
    private $rep;
    private $urep;
    private $manager;
    function __construct(UserRepository $urep, ConversationRepository $rep, EntityManagerInterface $manager)
    {
        $this->rep = $rep;
        $this->urep = $urep;
        $this->manager = $manager;
    }

    #[Route('/blog/conversation/{id}', name: 'new_conversation')]
    public function createNewConversation($id): Response
    {
        $user = $this->getUser();
        $otherUser = $this->urep->find($id);
        
        // cannot create a conversation with myself 
        if ($user === $otherUser) {
            throw new \Exception("You cannot create a conversation with yourself!");
        }
        // check if conversation already exist
        $conversation = $this->rep->findConversationByParticipants($otherUser->getId(), $user->getId());
        // dd($conversation);
        if (count($conversation)) {
            // throw new \Exception("convo already exists");
            return $this->redirectToRoute('get_message', ['id'=> $conversation->getId()]);
        }
        $conversation = new Conversation();
        //create participants
        $participant = new ParticipantMessanger();
        $participant->setUser($user);
        $participant->setConversation($conversation);
        $participant1 = new ParticipantMessanger();
        $participant1->setUser($otherUser);
        $participant1->setConversation($conversation);

        $this->manager->getConnection()->beginTransaction();
        try {
            $this->manager->persist($conversation);
            $this->manager->persist($participant);
            $this->manager->persist($participant1);

            $this->manager->flush();
            $this->manager->commit();
            echo("done ".$conversation->getId());
        } catch (\Exception $exception) {
            $this->manager->rollback();
            throw $exception;
        }
        // return $this->json([
        //     'id'=>$conversation->getId()
        // ], Response::HTTP_CREATED, [], []);
        //TODO rakah hkeyet messaget 
        return $this->render('conversation/index.html.twig', [
            'controller_name' => 'ConversationController',
        ]);
    }
    #[Route('/blog/conversation', name: 'get_conversation')]
    public function getConversations(Request $req){
        $user = $this->getUser();
        $users = $this->urep->findAll();
        $conversations = $this->rep->findConversationByUser($user->getId());
        
        $hubUrl = $this->getParameter('mercure.default_hub');
        $this->addLink($req, new Link('mercure', $hubUrl));

        return $this->renderForm('main/conversation/index.html.twig', [
            'conversations' => $conversations,
            "users" => $users
        ]);
    } 
    #[Route('/blog/publish', name: 'publish')]
    public function publish(HubInterface $hub): Response
    {
        $update = new Update(
            'https://example.com/books/1',
            json_encode(['status' => 'message recu']),
            true 
        );

        $hub->publish($update);

        return new Response('published!');
    }
}
