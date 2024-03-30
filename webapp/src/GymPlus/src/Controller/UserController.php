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
use App\Repository\AbonnementRepository;
use App\Entity\Abonnement;
use App\Repository\UserRepository;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\AbonnementDetailsRepository;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpClient\HttpClient;
use App\Form\ModifyUserType;
use App\Annotation\ProtectedRoute;
use Karser\Recaptcha3Bundle\Validator\Constraints\Recaptcha3Validator;

class UserController extends AbstractController
{
    #[Route('/dashboard/home', name: 'app_dashboard')]
    public function dashboard(SessionInterface $session): Response
    {
        $user = $session->get('user');
    
        return $this->render('dashboard/index.html.twig', [
            'controller_name' => 'UserController',
            'user' => $user
        ]);
    }

    #[Route('/home', name: 'app_home')]
    public function home(SessionInterface $session): Response
    {
        $user = $session->get('user');
       
        return $this->render('main/index.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user')
        ]);
    }

    #[Route('/auth/login', name: 'app_login')]
    public function login(Request $request, SessionInterface $session, UserRepository $repo, Recaptcha3Validator $recaptcha3Validator): Response
    {
        $user = $session->get('user');
    
        $form = $this->createForm(LoginType::class);
        $form->handleRequest($request);
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                foreach ($form->getErrors(true) as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            }else{
                //$score = $recaptcha3Validator->getLastResponse()->getScore();
                $data = $form->getData();
                $user = $repo->findOneBy(['email' => $data['email']]);
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
    public function signup(Request $request, SessionInterface $session, ManagerRegistry $reg, UserRepository $repo): Response
    {
        $user = $session->get('user');
        
        $form = $this->createForm(UserType::class);
        $form->handleRequest($request);
        
        if($form->isSubmitted() ){
            if(!$form->isValid()){
                foreach($form->getErrors(true) as $error){
                    $this->addFlash('error', $error->getMessage());
                }
            }else{
                $data = $form->getData();
                if ($repo->findUserByEmail($data->getEmail())) {
                    $this->addFlash('error', 'Email already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserByUsername($data->getUsername())) {
                    $this->addFlash('error', 'Username already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserByPhone($data->getNumTel())) {
                    $this->addFlash('error', 'Phone number already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserById($data->getId())) {
                    $this->addFlash('error', 'CIN already exists');
                    return $this->redirectToRoute('app_signup');
                }
                $user = new User();
                $user->setEmail($data->getEmail());
                $user->setPassword(password_hash($data->getPassword(), PASSWORD_DEFAULT));
                $user->setRole('client');
                $user->setUsername($data->getUsername());
                $user->setNumTel($data->getNumTel());
                $user->setAdresse($data->getAdresse());
                $user->setFaceidTs(new \DateTime());
                $user->setDateNaiss($data->getDateNaiss());
                $user->setFirstname($data->getFirstname());
                $user->setLastname($data->getLastname());
                $user->setId($data->getId());
                $photo = $form['photo']->getData();
                $filename = 'USERIMG' . $user->getId() . '.' . $photo->guessExtension();
                $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
                $photo->move($targetdir, $filename);
                $user->setPhoto($filename);
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                $session->set('user', $user);
            }
        }
        

        return $this->render('main/signup.html.twig', [
            'controller_name' => 'UserController',
            'form' => $form->createView(),
            'user' => $session->get('user')
        ]);
    }

    #[Route('/auth/logout', name: 'app_logout')]
    public function logout(SessionInterface $session): Response
    {
        $session->remove('user');
        return $this->redirectToRoute('app_login');
    }

    #[Route('/member/profile', name: 'app_profile')]
    public function profile(SessionInterface $session, Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $user = $session->get('user');
       
        $user = $repo->findUserById($user->getId());
        $form = $this->createForm(ModifyUserType::class, $user);
        $form->handleRequest($request);
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                foreach ($form->getErrors(true) as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            }else{
                $data = $form->getData();
                $user->setUsername($data->getUsername());
                $user->setNumTel($data->getNumTel());
                $user->setAdresse($data->getAdresse());
                $user->setDateNaiss($data->getDateNaiss());
                $user->setFirstname($data->getFirstname());
                $user->setLastname($data->getLastname());
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                $session->set('user', $user);
                return $this->redirectToRoute('app_profile');
            }
        }
        return $this->render('main/profile.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user'),
            'form' => $form->createView()
        ]);
    }

    #[Route('/api/modifyImage', name: 'app_photo')]
    public function modifyImage(SessionInterface $session, Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $user = $session->get('user');
        $user = $repo->findUserById($user->getId());
        $photo = $request->files->get('image');
        if ($photo) {
            $filename = 'USERIMG' . $user->getId() . '.' . $photo->guessExtension();
           
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
            $photo->move($targetdir, $filename);
            $user->setPhoto($filename);
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            $session->set('user', $user);
        }
        return $this->redirectToRoute('app_profile');
    }


    #[Route('/member/subscriptions', name: 'app_subs')]
    public function subscriptions(SessionInterface $session, AbonnementRepository $repo): Response
    {

        $user = $session->get('user');
        if ($repo->isUserSubscribed($user->getId())) {
            return $this->render('main/mysubscriptions.html.twig', [
                'controller_name' => 'UserController',
                'user' => $session->get('user'),
                'subs' => $repo->getOldSubscriptionsByUserId($user->getId()),
                'subnow' => $repo->getCurrentSubByUserId($user->getId()),
                'diff' => $repo->getCurrentSubByUserId($user->getId())->getDatefinab()->diff(new \DateTime())->format('%a')
            ]);
        }
        
        return $this->render('main/subscriptions.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user')
        ]);
    }

    #[Route('/payment/gp', name: 'app_buy')]
    public function buy(SessionInterface $session, Request $request, AbonnementRepository $repo, AbonnementDetailsRepository $repo0): Response
    {
        $id = $request->query->get('id');
        if (!$id && id != 1 && id != 2 && id != 3) {
            return $this->redirectToRoute('app_home');
        }

        $user = $session->get('user');
        if ($repo->isUserSubscribed($user->getId())) {
            return $this->redirectToRoute('app_home');
        }
        $price = $repo0->getAbonnementPriceByName('GP ' . $id);
        return $this->render('main/buy.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user'),
            'id' => $id,
            'price' => $price
        ]);
    }

    #[Route('/payment/validate', name: 'app_validate_pp')]
    public function validatePayment(Request $request, AbonnementRepository $repo, AbonnementDetailsRepository $repo0, UserRepository $repo1, ManagerRegistry $reg): Response
    {
        $id = $request->query->get('order');
        $userid = $request->query->get('user');
        $gp = $request->query->get('gp');
        $user = $repo1->findUserById($userid);
        $abonnement = new Abonnement();
        $abonnement->setUser($user);
        $abonnement->setDatefinab(new \DateTime('+1 month'));
        $type = $repo0->getAbonnementDetailsByName('GP ' . $gp);
        $abonnement->setType($type);
        $reg->getManager()->persist($abonnement);
        $reg->getManager()->flush();

        $client = HttpClient::create();
        $auth = base64_encode('Af9N6m41ryO-aKuZO3cwgGr1PZeBsbxLTeSYVJ7iUr71UO3tA8Uc0p50NeEMbewLzmq00go8MoXAzXPC:EBfq6ayRpNgVeWdALsblGLrMKUspTHt5GzfuH5MOM1FN7gqsoR6y5TCOTTYlm4R3adGWPgKoWYOI46Xz');
        $response = $client->request('POST', 'https://api-m.sandbox.paypal.com/v1/oauth2/token', [
            'headers' => [
                'Content-Type' => 'application/x-www-form-urlencoded',
                'Authorization' => 'Basic '. $auth
            ],
            'body' => [
                'grant_type' => 'client_credentials'
            ]
        ]);
        $token = $response->toArray()['access_token'];
        

        $response = $client->request('POST', 'https://api-m.sandbox.paypal.com/v2/checkout/orders/' . $id . '/capture', [
            'headers' => [
                'Content-Type' => 'application/json',
                'Authorization' => 'Bearer '. $token
            ]
        ]);

        
        if ($response->getStatus() === "COMPLETED"){
            return new JsonResponse(['status' => 'success'], 200);
        }
        return new JsonResponse(['status' => 'error'], 400);
    }
    #[Route('/dashboard/manageusers', name: 'app_usermgmt')]
    public function manageUsers(SessionInterface $session, UserRepository $repo): Response
    {
        $user = $session->get('user');
        if ($user->getRole() != 'admin') {
            return $this->redirectToRoute('app_home');
        }
        return $this->render('dashboard/manageusers.html.twig', [
            'controller_name' => 'UserController',
            'user' => $session->get('user'),
            'users' => $repo->findAll()
        ]);
    }
}
