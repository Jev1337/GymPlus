<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Form\LoginType;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\User;
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
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use App\Form\AdminUserType;
use Karser\Recaptcha3Bundle\Validator\Constraints\Recaptcha3Validator;


class UserController extends AbstractController
{
    #[Route('/dashboard/home', name: 'app_dashboard')]
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
    public function login(Request $request, UserRepository $repo, Recaptcha3Validator $recaptcha3Validator): Response
    {
        if ($this->getUser()) {
            return $this->redirectToRoute('app_home');
        }
        $userlog = new User();
        $form = $this->createForm(LoginType::class, $userlog);
        $form->handleRequest($request);
        if($form->isSubmitted()){
            /*
            $client = HttpClient::create();
            $response = $client->request('POST', 'https://www.google.com/recaptcha/api/siteverify', [
                'body' => [
                    'secret' => '6LdlPKkpAAAAAJt_IYp6Nk2pIimy6h4UEJTyk9tQ',
                    'response' => $form['captcha']->getData(),
                ]]);
            $data = $response->toArray();
            if (!$data['success']) {
                $this->addFlash('error', 'Recaptcha validation failed, please try again!');
                return $this->redirectToRoute('app_login');
            }else{
                if ($data['score'] < 0.5) {
                    $this->addFlash('error', 'Recaptcha validation failed, please try again!');
                    return $this->redirectToRoute('app_login');
                }
            }*/
            if (!$form->isValid()) {
               $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $user = $repo->findUserByEmail($userlog->getEmail());
                if($user){
                    if(password_verify($userlog->getPassword(), $user->getPassword())){
                        $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
                        $this->get('security.token_storage')->setToken($token);
                        $this->get('session')->set('_security_main', serialize($token));
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
            'form' => $form->createView()
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
        if ($verificationCheck->status == 'approved') {
            return new JsonResponse(['status' => 'success'], 200);
        }
        return new JsonResponse(['status' => 'error'], 400);
    }

    
    #[Route('/auth/signup', name: 'app_signup')]
    public function signup(Request $request, ManagerRegistry $reg, UserRepository $repo): Response
    {
        if ($this->getUser()) {
            return $this->redirectToRoute('app_home');
        }
        $usersig = new User();
        $form = $this->createForm(UserType::class, $usersig);
        $form->handleRequest($request);
        
        if($form->isSubmitted() ){
            if(!$form->isValid()){
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                
                if ($repo->findUserByEmail($usersig->getEmail())) {
                    $this->addFlash('error', 'Email already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserByUsername($usersig->getUsername())) {
                    $this->addFlash('error', 'Username already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserByPhone($usersig->getNumTel())) {
                    $this->addFlash('error', 'Phone number already exists');
                    return $this->redirectToRoute('app_signup');
                }
                if ($repo->findUserById($usersig->getId())) {
                    $this->addFlash('error', 'CIN already exists');
                    return $this->redirectToRoute('app_signup');
                }
                $usersig->setPassword(password_hash($usersig->getPassword(), PASSWORD_BCRYPT));
                $usersig->setRole('client');
                $photo = $form['photo']->getData();
                $filename = 'USERIMG' . $usersig->getId() . '.' . $photo->guessExtension();
                $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
                $photo->move($targetdir, $filename);
                $usersig->setPhoto($filename);
                $reg->getManager()->persist($usersig);
                $reg->getManager()->flush();
                $token = new UsernamePasswordToken($usersig, null, 'main', $usersig->getRoles());
                $this->get('security.token_storage')->setToken($token);
                $this->get('session')->set('_security_main', serialize($token));
            }
        }
        

        return $this->render('main/signup.html.twig', [
            'controller_name' => 'UserController',
            'form' => $form->createView()
        ]);
    }

    #[Route('/auth/logout', name: 'app_logout')]
    public function logout(): Response
    {
        $this->get('security.token_storage')->setToken(null);
        $this->get('session')->invalidate();
        return $this->redirectToRoute('app_login');
    }

    #[Route('/member/profile', name: 'app_profile')]
    public function profile(Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $user = $this->getUser();
        $form = $this->createForm(ModifyUserType::class, $user);
        $form->handleRequest($request);
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $data = $form->getData();
                if ($repo->findUserByUsername($data->getUsername()) && $repo->findUserByUsername($data->getUsername())->getId() != $user->getId()){
                    $this->addFlash('error', 'Username already exists');
                    return $this->redirectToRoute('app_profile');
                }
                if ($repo->findUserByPhone($data->getNumTel()) && $repo->findUserByPhone($data->getNumTel())->getId() != $user->getId()) {
                    $this->addFlash('error', 'Phone number already exists, please remove it from the other account!');
                    return $this->redirectToRoute('app_profile');
                }
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                $this->addFlash('success', 'User updated successfully!');
                return $this->redirectToRoute('app_profile');
            }
        }
        return $this->render('main/profile.html.twig', [
            'controller_name' => 'UserController',
            'form' => $form->createView()
        ]);
    }

    #[Route('/api/modifyImage', name: 'app_photo')]
    public function modifyImage(Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $user = $this->getUser();
        $photo = $request->files->get('image');
        if ($photo) {
            $filename = 'USERIMG' . $user->getId() . '.' . $photo->guessExtension();
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
            $photo->move($targetdir, $filename);
            $user->setPhoto($filename);
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            $this->addFlash('success', 'Image updated successfully!');
        }
        return $this->redirect($request->headers->get('referer'));
    }

    #[Route('/api/dashboard/modifyImage/{id}', name: 'app_photo_admin')]
    public function modifyImageAdmin(Request $request, UserRepository $repo, ManagerRegistry $reg, $id): Response
    {   
        $userimg = $repo->findUserById($id);
        $photo = $request->files->get('image');
        if ($photo) {
            $filename = 'USERIMG' . $userimg->getId() . '.' . $photo->guessExtension();
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
            $photo->move($targetdir, $filename);
            $userimg->setPhoto($filename);
            $reg->getManager()->persist($userimg);
            $reg->getManager()->flush();
            $this->addFlash('success', 'Image updated successfully!');
        }
        return $this->redirect($request->headers->get('referer'));
    }



    #[Route('/member/subscriptions', name: 'app_subs')]
    public function subscriptions(AbonnementRepository $repo): Response
    {

        if ($repo->isUserSubscribed($this->getUser()->getId())) {
            return $this->render('main/mysubscriptions.html.twig', [
                'controller_name' => 'UserController',
                'subs' => $repo->getOldSubscriptionsByUserId($this->getUser()->getId()),
                'subnow' => $repo->getCurrentSubByUserId($this->getUser()->getId()),
                'diff' => $repo->getCurrentSubByUserId($this->getUser()->getId())->getDatefinab()->diff(new \DateTime())->format('%a')
            ]);
        }
        
        return $this->render('main/subscriptions.html.twig', [
            'controller_name' => 'UserController',

        ]);
    }

    #[Route('/payment/gp', name: 'app_buy')]
    public function buy( Request $request, AbonnementRepository $repo, AbonnementDetailsRepository $repo0): Response
    {
        $user = $this->getUser();
        $id = $request->query->get('id');
        if (!$id && id != 1 && id != 2 && id != 3) {
            return $this->redirectToRoute('app_home');
        }

        if ($repo->isUserSubscribed($user->getId())) {
            return $this->redirectToRoute('app_home');
        }
        $price = $repo0->getAbonnementPriceByName('GP ' . $id);
        return $this->render('main/buy.html.twig', [
            'controller_name' => 'UserController',
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
        

        
        if ($response->toArray()['status'] === "COMPLETED"){
            return new JsonResponse(['status' => 'success'], 200);
        }
        return new JsonResponse(['status' => 'error'], 400);
    }
    
    #[Route('/dashboard/manageusers', name: 'app_usermgmt')]
    public function manageUsers(UserRepository $repo, Request $req, ManagerRegistry $reg): Response
    {
        $form = $this->createForm(AdminUserType::class);
        $form->handleRequest($req);
        
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $user = $form->getData();
                if ($repo->findUserByUsername($user->getUsername())) {
                    $this->addFlash('error', 'Username already exists');
                } else if ($repo->findUserByPhone($user->getNumTel())) {
                    $this->addFlash('error', 'Phone number already exists, please remove it from the other account!');
                } else if ($repo->findUserById($user->getId())) {
                    $this->addFlash('error', 'CIN already exists');
                } else if ($repo->findUserByEmail($user->getEmail())) {
                    $this->addFlash('error', 'Email already exists');
                }else{
                    $photo = $form['photo']->getData();
                    $filename = 'USERIMG' . $user->getId() . '.' . $photo->guessExtension();
                    $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
                    $photo->move($targetdir, $filename);
                    $user->setPhoto($filename);
                    $user->setPassword(password_hash($user->getPassword(), PASSWORD_BCRYPT));

                    $reg->getManager()->persist($user);
                    $reg->getManager()->flush();
                    $this->addFlash('success', 'User updated successfully!');
                    return $this->redirectToRoute('app_usermgmt');
                }
            }
        }


        return $this->render('dashboard/userlist.html.twig', [
            'controller_name' => 'UserController',
            'users' => $repo->findAll(),
            'form' => $form->createView()
        ]);
    }

    #[Route('/dashboard/manageuser/{id}', name: 'app_user_edit')]
    public function manageUser(UserRepository $repo, Request $req, ManagerRegistry $reg, $id): Response
    {
        $useredit = $repo->findUserById($id);
        $form = $this->createForm(ModifyUserType::class, $useredit);
        $form->handleRequest($req);
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $data = $form->getData();
                if ($repo->findUserByUsername($data->getUsername()) && $repo->findUserByUsername($data->getUsername())->getId() != $useredit->getId()){
                    $this->addFlash('error', 'Username already exists');
                    return $this->redirectToRoute('app_dashboard_profile');
                }
                if ($repo->findUserByPhone($data->getNumTel()) && $repo->findUserByPhone($data->getNumTel())->getId() != $useredit->getId()) {
                    $this->addFlash('error', 'Phone number already exists, please remove it from the other account!');
                    return $this->redirectToRoute('app_dashboard_profile');
                }
            
                $reg->getManager()->persist($useredit);
                $reg->getManager()->flush();
                $this->addFlash('success', 'User updated successfully!');
                return $this->redirectToRoute('app_user_edit', ['id' => $id]);
            }
        }
        return $this->render('dashboard/profile.html.twig', [
            'controller_name' => 'UserController',
            'useredit' => $useredit,
            'form' => $form->createView()
        ]);
    }

    #[Route('/dashboard/profile', name: 'app_dashboard_profile')]
    public function dashboardProfile(UserRepository $repo, Request $req, ManagerRegistry $reg): Response
    {
        $user = $this->getUser();
        $form = $this->createForm(ModifyUserType::class, $user);
        $form->handleRequest($req);
        if($form->isSubmitted() ){
            if (!$form->isValid()) {
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $data = $form->getData();
                if ($repo->findUserByUsername($data->getUsername()) && $repo->findUserByUsername($data->getUsername())->getId() != $user->getId()){
                    $this->addFlash('error', 'Username already exists');
                    return $this->redirectToRoute('app_dashboard_profile');
                }
                if ($repo->findUserByPhone($data->getNumTel()) && $repo->findUserByPhone($data->getNumTel())->getId() != $user->getId()) {
                    $this->addFlash('error', 'Phone number already exists, please remove it from the other account!');
                    return $this->redirectToRoute('app_dashboard_profile');
                }
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                $this->addFlash('success', 'User updated successfully!');
                return $this->redirectToRoute('app_dashboard_profile');
            }
        }
        
        return $this->render('dashboard/profile.html.twig', [
            'controller_name' => 'UserController',
            'useredit' => $user,
            'form' => $form->createView()
        ]);
    }

    #[Route('/dashboard/deleteuser/{id}', name: 'app_user_delete')]
    public function deleteUser(UserRepository $repo, $id, ManagerRegistry $reg): Response
    {
        $userdel = $repo->findUserById($id);
        if (!$userdel) {
            return $this->redirectToRoute('app_usermgmt');
        }
        $reg->getManager()->remove($userdel);
        $reg->getManager()->flush();
        if ($this->getUser()->getId() == $userdel->getId()) {
            $this->get('security.token_storage')->setToken(null);
            $this->get('session')->invalidate();
            return $this->redirectToRoute('app_login');
        }
        return $this->redirectToRoute('app_usermgmt');
    }
}
