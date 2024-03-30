<?php

namespace App\Controller;

use App\Entity\Post;
use App\Form\PostType;
use App\Repository\CommentaireRepository;
use App\Repository\PostRepository;
use Doctrine\Persistence\ManagerRegistry;
use FileUploader;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;

class PostController extends AbstractController
{
    #[Route('/post', name: 'getAll_post')]
    public function getAllPosts(PostRepository $rep, CommentaireRepository $crep, ManagerRegistry $manager): Response
    {
        $posts = $rep->findAll();
        for ($i=0; $i < sizeof($posts); $i++) { 
            $this->updateNbComnt($crep,$posts[$i],$rep,$manager);
        }
        return $this->render('post/index.html.twig', [
            'posts' => $posts,
        ]);
    }
    #[Route('/post/add', name: 'add_post')]
    public function addPosts(Request $req, ManagerRegistry $manager, SluggerInterface $slugger): Response
    {
        $em = $manager->getManager();
        $dateImmutable = date_create('now');

        $post = new Post();
        $post->setDate($dateImmutable);
        $post->setNbComnts(0);
        $post->setLikes(0);
        $form = $this->createForm(PostType::class, $post); //bch thot les info mte3i ml formulaire lel $author
        $form->handleRequest($req); //bch te5ou request eli tbaathet mel form
        if ($form->isSubmitted()) {
            $em->persist($post);
            $em->flush();
            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('post/addNewPost.html.twig', [ //fi3ou4 render bch n9oul raw bch nraja3 formulaire
            "form" => $form,
        ]);
    }
    #[Route('/post/{id}', name: 'update_post')]
    public function updatePosts(Request $req, $id, PostRepository $rep, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $post = $rep->find($id);
        $form = $this->createForm(PostType::class, $post);
        $form->handleRequest($req);
        if ($form->isSubmitted()) {

            $em->persist($post);
            $em->flush();
            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('post/addNewPost.html.twig', [
            "form" => $form,
        ]);
    }
    #[Route('/post/delete/{id}', name: 'delete_post')]
    public function delete(PostRepository $rep, $id, ManagerRegistry $manager, CommentaireRepository $repc): Response
    {
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
