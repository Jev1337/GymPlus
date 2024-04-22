<?php

namespace App\Controller;

use App\Entity\Conversation;
use App\Entity\Message;
use App\Repository\ConversationRepository;
use App\Repository\MessageRepository;
use App\Repository\ParticipantRepository;
use Doctrine\ORM\EntityManagerInterface;
use Exception;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mercure\PublisherInterface;
use Symfony\Component\Mercure\Update;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\SerializerInterface;

class MessageController extends AbstractController
{
    const ATTRIBUTE = ['id', 'content', 'createdAt', 'isMine'];
    private $rep;
    private $prep;
    private $publisherInterface;

    private $manager;
    function __construct(MessageRepository $rep, ParticipantRepository $prep, EntityManagerInterface $manager, PublisherInterface $publisherInterface)
    {
        $this->rep = $rep;
        $this->prep = $prep;
        $this->manager = $manager;
        $this->publisherInterface = $publisherInterface;
    }

    #[Route('/blog/conversation/message/{id}', name: 'get_message')]
    public function index($id, ConversationRepository $crep, Conversation $conversation): Response
    {
        //check if this user can access a conversation
        $result = $crep->checkIfUserIsParticipant($conversation->getId(), $this->getUser()->getId());
        if ($result === null) {
            throw new Exception("access denied");
        }
        // show conersations
        $messages = $this->rep->findMessagesByConversationId($conversation->getId());
        /** 
         * @var $message Message 
        */
        array_map(function($message) {
            $message->setIsMine(
                $message->getUser()->getId()===$this->getUser()->getId()? true : false
            );
        }, $messages);
        // dd($messages);
        // return $this->json($messages, Response::HTTP_OK, [], [
        //     'attrbutes' => self::ATTRIBUTE
        // ]);
        return $this->renderForm('message/index.html.twig', [
            'messages' => $messages,
        ]);
    }

    #[Route('/blog/conversation/message', name: 'new_message')]
    public function addMessage(Request $req, Conversation $conversation, SerializerInterface $serializerInterface){
        $recipent = $this->prep->findParticipantByConversationIdAndUserId($conversation->getId(), $this->getUser()->getId());
        $user = $this->getUser();
        $content = $req->get('content', null);

        $message = new Message();
        $message->setContent($content);
        $message->setUser($user);

        
        $conversation->addMessage($message);
        $conversation->setLastMessage($message);

        $this->manager->getConnection()->beginTransaction();

        try {
            $this->manager->persist($message);
            $this->manager->persist($conversation);
            $this->manager->flush();
            $this->manager->commit();
            
        } catch (\Exception $th) {
            $this->manager->rollback();
            throw $th;
        }
        $message->setIsMine(false);
        $messageSerialized = $serializerInterface->serialize($message, 'json', [
            'attributes' => ['id', 'content', 'createdAt', 'isMine', 'conversation' => ['id']]
        ]);
        $update = new Update(
            [
                sprintf("/blog/conversation/%s", $conversation->getId()),
                sprintf("/blog/conversation/%s", $recipent->getUser->getUsername()),
            ],
            $messageSerialized,
        );

        $this->publisherInterface->__invoke($update);

        $message->setIsMine(true);
        return $this->json($message, Response::HTTP_CREATED, [], [
            'attributes' => self::ATTRIBUTE
        ]);
    }
}
