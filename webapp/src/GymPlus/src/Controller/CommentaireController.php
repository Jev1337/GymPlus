<?php

namespace App\Controller;

use App\Entity\Commentaire;
use App\Form\CommentaireType;
use App\Repository\CommentaireRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class CommentaireController extends AbstractController
{
    public ?int $idPost=0;

    #[Route('/post/commentaire/{id}', name: 'getAll_commentaire')]
    public function getAllComments(CommentaireRepository $rep, $id): Response
    {
        $this->idPost = (int)$id;
        $comments = $rep->findByPostId($this->idPost);
        return $this->render('commentaire/index.html.twig', [
            'comments' => $comments,
        ]);
    }
    #[Route('/post/commentaire/{{$idPost}}/add', name: 'add_commentaire')]
    public function addCommentaires(Request $req, ManagerRegistry $manager): Response
    {
        $dateImmutable = date_create('now');
        $em = $manager->getManager();
        
        $commentaire = new Commentaire();
        $commentaire->setDate($dateImmutable);
        $commentaire->setPostId($this->idPost);
        
        $form = $this->createForm(CommentaireType::class, $commentaire); 
        $form->handleRequest($req); 
        if ($form->isSubmitted()) {
            $em->persist($commentaire);
            $em->flush();
            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('commentaire/addNewComment.html.twig', [ 
            "form" => $form,
        ]);
    }
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
            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('commentaire/addNewComment.html.twig', [ 
            "form" => $form,
        ]);
    }
    #[Route('/post/commentaire/delete/{id}', name: 'delete_commentaire')]
    public function delete(CommentaireRepository $rep, $id, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $commentaire = $rep->find($id);

        $em->remove($commentaire);
        $em->flush();

        return $this->redirectToRoute('getAll_post');
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
