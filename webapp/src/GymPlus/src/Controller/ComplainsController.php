<?php

namespace App\Controller;

use App\Entity\Complains;
use App\Form\ComplainsType;
use App\Repository\ComplainsRepository;
use App\Repository\PostRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ComplainsController extends AbstractController
{
    #[Route('/dashboard/complaints', name: 'show_complaints')]
    public function index(ComplainsRepository $complainsRepository): Response
    {
        $complains = $complainsRepository->findAll();
        
        return $this->render('dashboard/blog/index.html.twig', [
            'complains' => $complains,
        ]);
    }
    #[Route('/complaints/add/{id}', name: 'add_complaints')]
    public function new($id, Request $req, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $user = $this->getUser();

        $complain = new Complains();
        $form = $this->createForm(ComplainsType::class, $complain);
        $form->handleRequest($req);
        if ($form->isSubmitted()) {
            if (!$form->isValid()) {
                $this->addFlash('error', 'You can not send an empty feedback!');
            } else {
                $complain->setUser($user);
                // $complain->setPost($post);
                $complain->setPostId($id);

                $em->persist($complain);
                $em->flush();
                $this->addFlash('success', 'Complaint sent successfully');
                return $this->redirectToRoute('getAll_post');
            }
        }
        return $this->renderForm('main/post/addNewComplain.html.twig', [
            'form' => $form,
        ]);
    }

    #[Route('/dashboard/complaints/delete/{id}', name: 'delete_complain')]
    public function delete(ComplainsRepository $rep, $id, ManagerRegistry $manager): Response
    {
        $em = $manager->getManager();
        $complain = $rep->find($id);
        $em->remove($complain);
        $em->flush();

        return $this->redirectToRoute('show_complaints');
    }
}
