<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Form\LoginType;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\User;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

class UserController extends AbstractController
{
    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(SessionInterface $session): Response
    {
        $user = $session->get('user');
        if(!$user){
            return $this->redirectToRoute('app_login');
        }
        if ($user->getRole() == 'client') {
            return $this->redirectToRoute('app_home');
        }
        return $this->render('dashboard/index.html.twig', [
            'controller_name' => 'UserController',
            'user' => $user
        ]);
    }

    #[Route('/home', name: 'app_home')]
    public function home(SessionInterface $session): Response
    {
        return $this->render('main/index.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user')
        ]);
    }

    #[Route('/auth/login', name: 'app_login')]
    public function login(Request $request, SessionInterface $session): Response
    {
        $user = $session->get('user');
        if ($user) {
            if ($user->getRole() == 'client') {
                return $this->redirectToRoute('app_home');
            } else {
                return $this->redirectToRoute('app_dashboard');
            }
        }
        $form = $this->createForm(LoginType::class);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $data = $form->getData();
            $user = $this->getDoctrine()->getRepository(User::class)->findOneBy(['email' => $data['email']]);
            if($user){
                if(password_verify($data['password'], $user->getPassword())){
                    $session->set('user', $user);
                    if ($user->getRole() != 'client') {
                        return $this->redirectToRoute('app_dashboard');
                    } else {
                        return $this->redirectToRoute('app_home');
                    }
                }else{
                    $this->addFlash('error', 'Invalid Email or Password');
                }
            }else{
                $this->addFlash('error', 'Invalid Email or Password');
            }
            
        }
        return $this->render('main/login.html.twig', [
            'controller_name' => 'UserController',
            'form' => $form->createView(),
            'user' => $session->get('user')
        ]);
    }

    
    #[Route('/auth/signup', name: 'app_signup')]
    public function signup(Request $request, SessionInterface $session): Response
    {
        $user = $session->get('user');
        if ($user) {
            if ($user->getRole() == 'client') {
                return $this->redirectToRoute('app_home');
            } else {
                return $this->redirectToRoute('app_dashboard');
            }
        }
        return $this->render('main/signup.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user')
        ]);
    }

    #[Route('/auth/logout', name: 'app_logout')]
    public function logout(SessionInterface $session): Response
    {
        $session->remove('user');
        return $this->redirectToRoute('app_login');
    }
}
