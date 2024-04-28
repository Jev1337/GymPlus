<?php

namespace App\Controller;

use App\Entity\Notif;
use App\Form\Notif1Type;
use App\Repository\NotifRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Produit;

#[Route('/notif')]
class NotifController extends AbstractController
{
    #[Route('/', name: 'app_notif_index', methods: ['GET'])]
    public function index(NotifRepository $notifRepository): Response
    {
        return $this->render('notif/index.html.twig', [
            'notifs' => $notifRepository->findAll(),
        ]);
    }

    // #[Route('/new', name: 'app_notif_new', methods: ['GET', 'POST'])]
    // public function new(Request $request, EntityManagerInterface $entityManager): Response
    // {
    //     $notif = new Notif();
    //     $form = $this->createForm(Notif1Type::class, $notif);
    //     $form->handleRequest($request);

    //     if ($form->isSubmitted() && $form->isValid()) {
    //         $entityManager->persist($notif);
    //         $entityManager->flush();

    //         return $this->redirectToRoute('app_notif_index', [], Response::HTTP_SEE_OTHER);
    //     }

    //     return $this->renderForm('notif/new.html.twig', [
    //         'notif' => $notif,
    //         'form' => $form,
    //     ]);
    // }

    #[Route('/new', name: 'app_notif_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager , NotifRepository $notifRepository): Response
    {
        // Récupérez les données du formulaire
        $idProduit = $request->request->get('id_produit');
        $nomUser = $request->request->get('author');
        $description = $request->request->get('comment');

        $produit = $entityManager->getRepository(Produit::class)->find($idProduit);

        $notif = new Notif();

        $notif->setIdProduit($idProduit);
        $notif->setNomUser($nomUser);
        $notif->setDescription($description);
        $notif->setTitre("Comment");
        $notif->setEtat("Unread");
        $notif->setEtatCommentaire("yes");
        $notif->setDatevente(new \DateTime());

        $entityManager->persist($notif);
        $entityManager->flush();

        // Récupérer le nom de la photo sans extension
        $photoNameWithoutExtension = pathinfo($produit->getPhoto(), PATHINFO_FILENAME);
        
        // Construire le chemin de la vidéo
        $videoPath = '/videos/' . $photoNameWithoutExtension . '.mp4';
        // var_dump($videoPath);   

        // Construire l'URL complète vers la vidéo
        $videoUrl = $request->getSchemeAndHttpHost() . $videoPath;
        // var_dump($videoUrl);    

        // Vérifier si la vidéo existe
        $videoExists = file_exists($_SERVER['DOCUMENT_ROOT'] . $videoPath);

        $detailsnotif = $notifRepository->findDetailsWithCondition();
        
        return $this->renderForm('produit/show.html.twig', [
            'notif' => $notif,
            'produit' => $produit,
            'videoUrl' => $videoUrl,
            'videoExists' => $videoExists,
            'detailsnotif' => $detailsnotif,
        ]);
    }

    #[Route('/{id}', name: 'app_notif_show', methods: ['GET'])]
    public function show(Notif $notif): Response
    {
        return $this->render('notif/show.html.twig', [
            'notif' => $notif,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_notif_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Notif $notif, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(Notif1Type::class, $notif);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_notif_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('notif/edit.html.twig', [
            'notif' => $notif,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_notif_delete', methods: ['POST'])]
    public function delete(Request $request, Notif $notif, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$notif->getId(), $request->request->get('_token'))) {
            $entityManager->remove($notif);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_notif_index', [], Response::HTTP_SEE_OTHER);
    }
}
