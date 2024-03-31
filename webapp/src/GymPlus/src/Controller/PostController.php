<?php

namespace App\Controller;

use App\Entity\Post;
use App\Form\PostType;
use App\Repository\CommentaireRepository;
use App\Repository\PostRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;

class PostController extends AbstractController
{
    #[Route('/post', name: 'getAll_post')]
    public function getAllPosts(Request $req, PostRepository $rep, CommentaireRepository $crep, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $dateImmutable = date_create('now');
        
        $user = $this->getUser();
        if ($user->getRole() != 'client') {
            return $this->redirectToRoute('app_dashboard');
        }

        $post = new Post();
        $post->setDate($dateImmutable);
        $post->setNbComnts(0);
        $post->setIdUser($user->getId());
        $post->setLikes(0);
        $form = $this->createForm(PostType::class, $post); //bch thot les info mte3i ml formulaire lel $author
        $form->handleRequest($req); //bch te5ou request eli tbaathet mel form
        if ($form->isSubmitted()) {
            $em->persist($post);
            $em->flush();
            return $this->redirectToRoute('getAll_post');
        }
        //Affichage
        $posts = $rep->findAll();
        for ($i=0; $i < sizeof($posts); $i++) { 
            $this->updateNbComnt($crep,$posts[$i],$rep,$manager);
        }
        return $this->renderForm('main/post/index.html.twig', [
            'posts' => $posts,
            'user' => $user, 
            'form' => $form
        ]);
    }
    
    #[Route('/post/{id}', name: 'update_post')]
    public function updatePosts(Request $req, $id, PostRepository $rep, ManagerRegistry $manager): Response
    {
        $user = $this->getUser();
        $em = $manager->getManager();
        $post = $rep->find($id);
        $form = $this->createForm(PostType::class, $post);
        $form->handleRequest($req);
        if ($form->isSubmitted()) {
            $em->persist($post);
            $em->flush();
            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('main/post/addNewPost.html.twig', [
            "form" => $form,
            "user" => $user
        ]);
    }
    #[Route('/post/delete/{id}', name: 'delete_post')]
    public function delete(PostRepository $rep, $id, ManagerRegistry $manager, CommentaireRepository $repc): Response
    {
        // $user = $this->getUser();
        $em = $manager->getManager();
        $post = $rep->find($id);
        $comntController = new CommentaireController();
        $comntController->deleteByIdPost($repc, $id, $manager);
        $em->remove($post);
        $em->flush();

        return $this->redirectToRoute('getAll_post');
    }

    public function updateNbComnt(CommentaireRepository $crep, $post, PostRepository $rep, ManagerRegistry $manager)
    {
        $em = $manager->getManager();
        // $post = $rep->find($id);
        $post->setNbComnts($crep->getNbComnts($post->getId()));
        $em->persist($post);
        $em->flush();
    }
}
