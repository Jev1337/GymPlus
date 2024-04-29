<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\EquipementsDetailsRepository;
use App\Form\EquipementType;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\MaintenancesRepository;
use App\Form\MaintenanceType;
use App\Form\ModifyMaintenanceType;
use Symfony\Component\Validator\Constraints\Json;
use Symfony\Component\HttpFoundation\JsonResponse;
use App\Repository\UserRepository;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mime\Part\DataPart;
use Symfony\Component\Mime\Part\File;
use Knp\Snappy\Pdf;
use Knp\Bundle\SnappyBundle\Snappy\Response\PdfResponse;

class EquipmentController extends AbstractController
{
    #[Route('/dashboard/equipments', name: 'app_equipments')]
    public function manageEquipements(EquipementsDetailsRepository $repo, Request $req, ManagerRegistry $reg): Response
    {
        
        $form = $this->createForm(EquipementType::class);
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $equipement = $form->getData();
            $entityManager = $reg->getManager();
            $entityManager->persist($equipement);
            $entityManager->flush();
        }

        $equipements = $repo->getEquipementsDetails();

        return $this->render('dashboard/equipment/equipements.html.twig', [
            'controller_name' => 'EquipmentController',
            'equipments' => $equipements,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/equipments/edit/{id}', name: 'app_equipments_edit')]
    public function manageEquipementsDetails(EquipementsDetailsRepository $repo, Request $req, ManagerRegistry $reg, $id, MailerInterface $mailer, UserRepository $repo1): Response
    {
        $equipement = $repo->find($id);
        $form = $this->createForm(EquipementType::class, $equipement);
        if ($equipement->getEtat() == 'Under Maintenance')
            $form->remove('etat');
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            if ($equipement->getEtat() == 'Bad'){
                
                try{
                    $admins = $repo1->getAdminList();
                    $emails = [];
                    foreach ($admins as $admin) 
                        $emails[] = $admin->getEmail();
                    $emails = implode(',', $emails);
                    $html = file_get_contents('dashboard/htmlassets/index.html');
                    $html = str_replace("{E1}", $id, $html);
                    
                    $email = (new Email())
                        ->from('gymplus-noreply@grandelation.com')
                        ->to($emails)
                        ->subject('Maintenance Request')
                        ->embed(fopen('dashboard/htmlassets/assets/image-5.png', 'r'), 'image-5', 'image/png')
                        ->html($html);
                    $mailer->send($email);
                }catch(\Exception $e){
                    $this->addFlash('error', 'Error sending email');
                }
            
                //$this->addFlash('success', 'Email sent successfully');
            }
                    
            $entityManager = $reg->getManager();
            $entityManager->persist($equipement);
            $entityManager->flush();
            return $this->redirectToRoute('app_equipments');
        }

        return $this->render('dashboard/equipment/edit_equipements.html.twig', [
            'controller_name' => 'EquipmentController',
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/equipments/delete/{id}', name: 'app_equipments_delete')]
    public function deleteEquipements(EquipementsDetailsRepository $repo, $id): Response
    {
        $equipement = $repo->find($id);

        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->remove($equipement);
        $entityManager->flush();

        return $this->redirectToRoute('app_equipments');
    }

    #[Route('/dashboard/maintenances/', name: 'app_maintenances')]
    public function manageMaintenances(MaintenancesRepository $repo, Request $req, ManagerRegistry $reg, MailerInterface $mailer, UserRepository $repo1): Response
    {
        $form = $this->createForm(MaintenanceType::class);
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $maintenance = $form->getData();
            $equipment = $maintenance->getEquipementsDetails();
            $equipment->setEtat('Under Maintenance');
            try{
                $admins = $repo1->getAdminList();
                $emails = [];
                foreach ($admins as $admin) 
                    $emails[] = $admin->getEmail();
                $emails = implode(',', $emails);
                $html = file_get_contents('dashboard/htmlassets/index.html');
                $html = str_replace("{E1}", $equipment->getId(), $html);
                
                $email = (new Email())
                    ->from('gymplus-noreply@grandelation.com')
                    ->to($emails)
                    ->subject('Maintenance Request')
                    ->embed(fopen('dashboard/htmlassets/assets/image-5.png', 'r'), 'image-5', 'image/png')
                    ->html($html);
                $mailer->send($email);
            }catch(\Exception $e){
                $this->addFlash('error', 'Error sending email');
            }
            // $this->addFlash('success', 'Email sent successfully');
            $entityManager = $reg->getManager();
            $entityManager->persist($maintenance);
            $entityManager->flush();
        }
        $maintenances = $repo->getMaintenances();

        return $this->render('dashboard/equipment/maintenances.html.twig', [
            'controller_name' => 'EquipmentController',
            'maintenances' => $maintenances,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/maintenances/edit/{id}', name: 'app_maintenances_edit')]
    public function manageMaintenancesDetails(MaintenancesRepository $repo, Request $req, ManagerRegistry $reg, $id): Response
    {
        $maintenance = $repo->find($id);
        $form = $this->createForm(ModifyMaintenanceType::class, $maintenance);
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $reg->getManager();
            $entityManager->persist($maintenance);
            $entityManager->flush();
            return $this->redirectToRoute('app_maintenances');
        }

        return $this->render('dashboard/equipment/edit_maintenances.html.twig', [
            'controller_name' => 'EquipmentController',
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/maintenances/delete/{id}', name: 'app_maintenances_delete')]
    public function deleteMaintenances(MaintenancesRepository $repo, $id): Response
    {
        $maintenance = $repo->find($id);
        $entityManager = $this->getDoctrine()->getManager();
        $equipment = $maintenance->getEquipementsDetails();
        $equipment->setEtat('Good');
        $entityManager->remove($maintenance);
        $entityManager->flush();

        return $this->redirectToRoute('app_maintenances');
    }

    #[Route('/equipments', name: 'app_memequipments')]
    public function index(EquipementsDetailsRepository $repo): Response
    {
        return $this->render('main/equipments/equipments.html.twig', [
            'controller_name' => 'EquipmentController',
            'equipments' => $repo->getEquipementsDetails(),
        ]);
    }

    #[Route('/api/sendmailmaint/{id}', name: 'app_sendmail')]
    public function sendMail($id, UserRepository $repo1, MailerInterface $mailer): Response
    {
        //bundle: Symfony/Mailer
        try{
            $admins = $repo1->getAdminList();
            $emails = [];
            foreach ($admins as $admin) 
                $emails[] = $admin->getEmail();
            $emails = implode(',', $emails);
            $html = file_get_contents('dashboard/htmlassets/index.html');
            $html = str_replace("{E1}", $id, $html);
            
            $email = (new Email())
                ->from('gymplus-noreply@grandelation.com')
                ->to($emails)
                ->subject('Maintenance Request')
                ->embed(fopen('dashboard/htmlassets/assets/image-5.png', 'r'), 'image-5', 'image/png')
                ->html($html);

            $mailer->send($email);
        }catch(\Exception $e){
            return new JsonResponse(['status' => 'error']);
        }
        return new JsonResponse(['status' => 'success']);
    }

    #[Route('/api/generatePDFmaint/{idm}', name: 'app_generatePDF')]
    public function generatePDF($idm, MaintenancesRepository $repo, Pdf $pdf): Response
    {
        $maintenance = $repo->find($idm);
        $html = file_get_contents('dashboard/htmlassets/attestation.html');
        $html = str_replace("{E1}", $maintenance->getEquipementsDetails()->getId(), $html);
        $days = date_diff($maintenance->getDateMaintenance(), new \DateTime())->format('%a');
        $html = str_replace("{E2}", $days, $html);
        $price = 200*$days;
        $html = str_replace("{E3}", $price, $html);
        $curdate = new \DateTime();
        $html = str_replace("{E4}", $curdate->format('Y-m-d'), $html);
        $html = str_replace("{E5}", $maintenance->getId(), $html);
        $pdf->setOption('enable-local-file-access', true);
        $html = str_replace("assets/",realpath('dashboard/htmlassets/assets/')."/", $html);

        return new PdfResponse(
            $pdf->getOutputFromHtml($html),
            'files.pdf'
        );
    }

}
