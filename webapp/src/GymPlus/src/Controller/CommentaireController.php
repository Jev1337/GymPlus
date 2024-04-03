<?php

namespace App\Controller;

use App\Entity\Commentaire;
use App\Form\CommentaireType;
use App\Repository\CommentaireRepository;
use App\Repository\PostRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;

class CommentaireController extends AbstractController
{
    public ?int $idPost=0;

    #[Route('/post/commentaire/{id}', name: 'getAll_commentaire')]
    public function getAllComments(CommentaireRepository $rep, PostRepository $prep,$id, Request $req, ManagerRegistry $manager, PostController $pc): Response
    {
        $this->idPost = (int)$id;
        $user = $this->getUser();
        $dateImmutable = date_create('now');
        $em = $manager->getManager();
        
        $commentaire = new Commentaire();
        $commentaire->setDate($dateImmutable);
        $commentaire->setPostId($this->idPost);
        $commentaire->setUserId($user->getId());
        
        $post = $prep->find($id);
        $form = $this->createForm(CommentaireType::class, $commentaire); 
        $form->handleRequest($req); 
        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($commentaire);
            $em->flush();
            $pc->updateNbComnt($rep,$post,$prep,$manager);
            return $this->redirectToRoute('getAll_commentaire',['id' => $id]);
        }
        
        $comments = $rep->findByPostId($this->idPost);
        return $this->renderForm('main/commentaire/index.html.twig', [
            'comments' => $comments,
            'post' => $post,
            'user' => $user,
            'form'=> $form
        ]);
    }
    // #[Route('/post/commentaire/{{$idPost}}/add', name: 'add_commentaire')]
    // public function addCommentaires(Request $req, ManagerRegistry $manager): Response
    // {
    //     $dateImmutable = date_create('now');
    //     $em = $manager->getManager();
        
    //     $commentaire = new Commentaire();
    //     $commentaire->setDate($dateImmutable);
    //     $commentaire->setPostId($this->idPost);
        
    //     $form = $this->createForm(CommentaireType::class, $commentaire); 
    //     $form->handleRequest($req); 
    //     if ($form->isSubmitted()) {
    //         $em->persist($commentaire);
    //         $em->flush();
    //         return $this->redirectToRoute('getAll_commentaire');
    //     }

    //     return $this->renderForm('main/commentaire/addNewComment.html.twig', [ 
    //         "form" => $form,
    //     ]);
    // }
    #[Route('/post/commentaire/update/{id}', name: 'update_commentaire')]
    public function updateCommentaires(Request $req, $id, CommentaireRepository $rep, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $commentaire = $rep->find($id);
        $form = $this->createForm(CommentaireType::class, $commentaire); 
        $form->handleRequest($req); 
        if ($form->isSubmitted()) {

            $em->persist($commentaire);
            $em->flush();
            return $this->redirectToRoute('getAll_commentaire',['id' => $commentaire->getPostId()]);
        }

        return $this->renderForm('main/commentaire/addNewComment.html.twig', [ 
            "form" => $form,
            "comnt"=>$commentaire
        ]);
    }
    #[Route('/post/commentaire/delete/{id}', name: 'delete_commentaire')]
    public function delete(CommentaireRepository $rep, $id, ManagerRegistry $manager, PostController $pc, PostRepository $prep): Response
    {
        $em = $manager->getManager();
        $commentaire = $rep->find($id);
        $post = $prep->find($commentaire->getPostId());
        $em->remove($commentaire);
        $em->flush();
        $pc->updateNbComnt($rep,$post,$prep,$manager);

        return $this->redirectToRoute('getAll_commentaire',['id' => $commentaire->getPostId()]);
    }
    public function deleteByIdPost(CommentaireRepository $rep, $id, ManagerRegistry $manager)
    {
        $em = $manager->getManager();
        $commentaire = $rep->findByPostId($id);
        for ($i=0; $i < sizeof($commentaire); $i++) { 
            $em->remove($commentaire[$i]);
        }
        $em->flush();

    }
}
