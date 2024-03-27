<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class UserController extends AbstractController
{
    #[Route('/dashboard', name: 'app_user')]
    public function dashboard(): Response
    {
        return $this->render('dashboard/index.html.twig', [
            'controller_name' => 'UserController',
        ]);
    }

    #[Route('/home', name: 'app_home')]
    public function home(): Response
    {
        return $this->render('main/index.html.twig', [
            'controller_name' => 'UserController',
        ]);
    }

    #[Route('/auth/login', name: 'app_login')]
    public function login(): Response
    {
        return $this->render('main/login.html.twig', [
            'controller_name' => 'UserController',
        ]);
    }

    
    #[Route('/auth/signup', name: 'app_signup')]
    public function signup(): Response
    {
        return $this->render('main/signup.html.twig', [
            'controller_name' => 'UserController',
        ]);
    }
}
