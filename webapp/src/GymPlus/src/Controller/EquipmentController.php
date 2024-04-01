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

        return $this->render('equipment/equipements.html.twig', [
            'controller_name' => 'EquipmentController',
            'equipments' => $equipements,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/dashboard/equipments/edit/{id}', name: 'app_equipments_edit')]
    public function manageEquipementsDetails(EquipementsDetailsRepository $repo, Request $req, ManagerRegistry $reg, $id): Response
    {
        $equipement = $repo->find($id);
        $form = $this->createForm(EquipementType::class, $equipement);
        if ($equipement->getEtat() == 'Under Maintenance')
            $form->remove('etat');
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $reg->getManager();
            $entityManager->persist($equipement);
            $entityManager->flush();
            return $this->redirectToRoute('app_equipments');
        }

        return $this->render('equipment/edit_equipements.html.twig', [
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
    public function manageMaintenances(MaintenancesRepository $repo, Request $req, ManagerRegistry $reg): Response
    {
        $form = $this->createForm(MaintenanceType::class);
        $form->handleRequest($req);
        if ($form->isSubmitted() && $form->isValid()) {
            $maintenance = $form->getData();
            $equipment = $maintenance->getEquipementsDetails();
            $equipment->setEtat('Under Maintenance');
            $entityManager = $reg->getManager();
            $entityManager->persist($maintenance);
            $entityManager->flush();
        }
        $maintenances = $repo->getMaintenances();

        return $this->render('equipment/maintenances.html.twig', [
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

        return $this->render('equipment/edit_maintenances.html.twig', [
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

    
    

}
