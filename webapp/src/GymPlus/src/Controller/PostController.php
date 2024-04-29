<?php

namespace App\Controller;

use App\Entity\Complains;
use App\Entity\Post;
use App\Form\PostType;
use App\Repository\CommentaireRepository;
use App\Repository\ComplainsRepository;
use App\Repository\PostRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class PostController extends AbstractController
{
    #[Route('/post', name: 'getAll_post')]
    public function getAllPosts(Request $req, PostRepository $rep, EntityManagerInterface $entityManager, CommentaireRepository $crep, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $dateImmutable = date_create('now');
        
        $user = $this->getUser();
        $post = new Post();
        $form = $this->createForm(PostType::class, $post); //bch thot les info mte3i ml formulaire lel $author
        // $showError = false;
        
        $form->handleRequest($req); //bch te5ou request eli tbaathet mel form
        if ($form->isSubmitted()) {
            if (!$form->isValid()) {
                    $this->addFlash('error', 'You can not add an empty post!');
            }else{
                $post->setDate($dateImmutable);
                $post->setUser($user);

                $photo = $form['photo']->getData();
                if ($photo) {
                    $filename = 'USERIMG' . $user->getId() . '.' . $photo->guessExtension();
                    $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
                    $photo->move($targetdir, $filename);
                    $post->setPhoto($filename);
                }else{
                    $post->setPhoto('');
                }
                $post->setNbComnts(0);
                $post->setLikes(0);
                $em->persist($post);
                $em->flush();
                return $this->redirectToRoute('getAll_post');
            }
           
        }
        //affichage 

        $posts = $rep->findAll();
        // $posts1 = array();
        // $j = 0;


        for ($i=0; $i < sizeof($posts); $i++) {
            if($this->checkPostComplaintsCount($posts[$i], $entityManager)){
                $this->updateNbComnt($crep,$posts[$i],$rep,$manager);
            }else {
                $key = array_search($posts[$i], $posts);
                array_splice($posts, $key, 1);
            }
        }
        
        return $this->renderForm('main/post/index.html.twig', [
            'posts' => $posts,
            'form' => $form
        ]);
    }
    
    #[Route('/post/{id}', name: 'update_post')]
    public function updatePosts(Request $req, $id, PostRepository $rep, ManagerRegistry $manager): Response
    {
        // $user = $this->getUser();
        $em = $manager->getManager();
        $post = $rep->find($id);
        $form = $this->createForm(PostType::class, $post);
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($post);
            $em->flush();

            return $this->redirectToRoute('getAll_post');
        }

        return $this->renderForm('main/post/addNewPost.html.twig', [
            "form" => $form,
            "post" => $post
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
        if ($this->getUser()->getRoles() === "admin") {
            return $this->redirectToRoute('show_complaints');
        }else{
            return $this->redirectToRoute('getAll_post');
        }
    }

    public function updateNbComnt(CommentaireRepository $crep, $post, PostRepository $rep, ManagerRegistry $manager)
    {
        $em = $manager->getManager();
        // $post = $rep->find($id);
        $post->setNbComnts($crep->getNbComnts($post->getId()));
        $em->persist($post);
        $em->flush();
    }
    
    #[Route('/post/like/{id}', name: 'updateLikes_post')]
    public function updateNbLikes($id, PostRepository $rep, ManagerRegistry $manager)
    {
        $em = $manager->getManager();
        $post = $rep->find($id);
        $likes = $post->getLikes()+1;
        $post->setLikes($likes);
        $em->persist($post);
        $em->flush();

        return $this->redirectToRoute('getAll_post');

    }

    //check if Post is signaled or not

    public function checkPostComplaintsCount(Post $post, EntityManagerInterface $entityManager): bool
    {
        $postCount = $entityManager->getRepository(Complains::class)
            ->createQueryBuilder('c')
            ->select('COUNT(c.id)')
            ->where('c.postId = :post')
            ->setParameter('post', $post->getId())
            ->getQuery()
            ->getSingleScalarResult();

        return $postCount < 5;
    }

}
