<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Form\LoginType;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\User;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Twilio\Rest\Client;
use App\Form\UserType;

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

    #[Route('/api/sendSms', name: 'api_send_sms')]
    public function sendSms(Request $request): Response
    {
        $phone = $request->query->get('phone');
        if (!$phone) {
            return new Response('Invalid Phone Number', 400);
        }
        $sid = "ACa0a9c02e124f285821fe62b736260421";
        $token = "e8cd361a90ce0dbcd5485d5719f935fb";
        $twilio = new Client($sid, $token);
        $verification = $twilio->verify->v2->services("VA10dd8bfd053741ce7361fd967c83a1e6")
                                   ->verifications
                                   ->create("whatsapp:+216". $phone, "whatsapp");
        return new Response($verification->status, 200);
    }

    #[Route('/api/verifyPhone', name: 'api_verify_phone')]
    public function verifyPhone(Request $request): Response
    {
        $phone = $request->query->get('phone');
        $code = $request->query->get('code');
        if (!$phone || !$code) {
            return new Response('Invalid Phone Number or Code', 400);
        }
        $sid = "ACa0a9c02e124f285821fe62b736260421";
        $token = "e8cd361a90ce0dbcd5485d5719f935fb";
        $twilio = new Client($sid, $token);
        $verificationCheck = $twilio->verify->v2->services("VA10dd8bfd053741ce7361fd967c83a1e6")
                                   ->verificationChecks
                                   ->create(["to"=> "whatsapp:+216. " . $phone, "code" => $code]);
        return new Response($verificationCheck->status, 200);
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
        $form = $this->createForm(UserType::class);
        $form->handleRequest($request);
        
        if($form->isSubmitted() && $form->isValid()){
            $data = $form->getData();
            $user = new User();
            
            $user->setEmail($data->getEmail());
            $user->setPassword(password_hash($data->getPassword(), PASSWORD_DEFAULT));
            $user->setRole('client');
            $user->setUsername($data->getUsername());
            $user->setNumTel($data->getNumTel());
            $user->setAdresse($data->getAdresse());
            $user->setFaceidTs(new \DateTime());
            $this->getDoctrine()->getManager()->persist($user);
            $this->getDoctrine()->getManager()->flush();
            return $this->redirectToRoute('app_login');
        }
        return $this->render('main/signup.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user'),
            'form' => $form->createView()
        ]);
    }

    #[Route('/auth/logout', name: 'app_logout')]
    public function logout(SessionInterface $session): Response
    {
        $session->remove('user');
        return $this->redirectToRoute('app_login');
    }

    #[Route('/subscriptions', name: 'app_subs')]
    public function subscriptions(SessionInterface $session): Response
    {
        return $this->render('main/subscriptions.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user')
        ]);
    }
}
