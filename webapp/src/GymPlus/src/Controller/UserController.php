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
use Symfony\Component\Mime\Part\DataPart;
use Symfony\Component\Mime\Part\Multipart\FormDataPart;
use GuzzleHttp\Client as GuzzleClient;
use App\Entity\AbonnementDetails;
use App\Form\GPPricesType;
use App\Repository\MaintenancesRepository;
use Google_Service_Oauth2;
use Google_Client;

class UserController extends AbstractController
{
    #[Route('/', name: 'app_indexred')]
    public function indexred(): Response
    {
        return $this->redirectToRoute('app_home');
    }
    #[Route('/dashboard/home', name: 'app_dashboard')]
    public function dashboard(UserRepository $repoUser, AbonnementRepository $repoAbon, MaintenancesRepository $repoMaint ): Response
    {
        $stats = [
            ['title' => 'Members Count', 'content' => $repoUser->getClientCount(), 'percent' => '100'],
            ['title' => 'Active Membership Count', 'content' => $repoAbon->getActiveMembershipCount(),'percent' => '100'],
            ['title' => 'Active Membership Percent', 'content' => $repoAbon->getActiveMembershipPercent()/$repoUser->getClientCount()*100, 'percent' => $repoAbon->getActiveMembershipPercent()/$repoUser->getClientCount()*100],
            ['title' => 'Event Count', 'content' => '', 'percent' => '100'] // Replace with actual event count
        ];

        $barchart1 = $repoMaint->getEachMonthMaintenancesCount();
        // $barchart2 = eventchart

        $start = microtime(true);
        $res = $repoUser->findUserById($this->getUser()->getId());
        $end = microtime(true);
        $dblatency = $end - $start;
        // trunc to 3 decimal places
        $dblatency = round($dblatency*1000, 2);
        
        $gpc = $repoAbon->getArrayGPCount();

        return $this->render('dashboard/index.html.twig', [
            'controller_name' => 'UserController',
            'stats' => $stats,
            'barchart1' => $barchart1,
            'barchart2' => [],
            'dblatency' => $dblatency,
            'gpc' => $gpc
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
        return $this->render('main/user/login.html.twig', [
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
        $sid = "AC6dc66bc270d75e383b6b6faedf1a7805";
        $token = "8ef079e198d30e29784c3f5c3840dbc8";
        $twilio = new Client($sid, $token);
        $verification = $twilio->verify->v2->services("VA1a9fc1403057f6ddc645002f416c50bc")
                                   ->verifications
                                   ->create("sms:+216". $phone, "sms");
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
        $sid = "AC6dc66bc270d75e383b6b6faedf1a7805";
        $token = "8ef079e198d30e29784c3f5c3840dbc8";
        $twilio = new Client($sid, $token);
        $verificationCheck = $twilio->verify->v2->services("VA1a9fc1403057f6ddc645002f416c50bc")
                                   ->verificationChecks
                                   ->create(["to"=> "sms:+216. " . $phone, "code" => $code]);
        if ($verificationCheck->status == 'approved') {
            return new JsonResponse(['status' => 'success'], 200);
        }
        return new JsonResponse(['status' => 'error'], 400);
    }

    
    #[Route('/api/auth/verify', name: 'api_verify')]
    public function verify(Request $request, UserRepository $repo): Response
    {
        $form = $this->createForm(UserType::class);
        $form->handleRequest($request);
        
        if($form->isSubmitted() ){
            if(!$form->isValid()){
                $errorArray = [];
                foreach ($form->getErrors(true) as $error) {
                    $errorArray[] = $error->getMessage();
                }
                return new JsonResponse(['status' => 'error', 'errors' => $errorArray], 200);
            }else{
                $usersig = $form->getData();
                $errorArray = [];
                if ($repo->findUserByEmail($usersig->getEmail())) {
                    //append error to array
                    array_push($errorArray, 'Email already exists');
                }
                if ($repo->findUserByUsername($usersig->getUsername())) {
                    //append error to array
                    array_push($errorArray, 'Username already exists');
                }
                if ($repo->findUserByPhone($usersig->getNumTel())) {
                    //append error to array
                    array_push($errorArray, 'Phone number already exists');
                }
                if ($repo->findUserById($usersig->getId())) {
                    //append error to array
                    array_push($errorArray, 'CIN already exists');
                }
                if (count($errorArray) > 0) {
                    return new JsonResponse(['status' => 'error', 'errors' => $errorArray], 200);
                }
                return new JsonResponse(['status' => 'success'], 200);
            }
        }
    }

    #[Route('/auth/signup', name: 'app_signup')]
    public function signup(Request $request, ManagerRegistry $reg, UserRepository $repo): Response
    {
        if ($this->getUser()) {
            return $this->redirectToRoute('app_home');
        }
        $form = $this->createForm(UserType::class);
        $form->handleRequest($request);
        
        if($form->isSubmitted() ){
            if(!$form->isValid()){
                $this->addFlash('error', 'There was an error with the form, please check the fields and try again!');
            }else{
                $usersig = $form->getData();
                $usersig->setPassword(password_hash($usersig->getPassword(), PASSWORD_BCRYPT));
                $usersig->setRole('client');
                if ($request->get('faceid') != null && $request->get('faceid') != '' && $request->get('faceid') != 'undefined'){
                    $usersig->setFaceid($request->get('faceid'));
                    $usersig->setFaceidTs(new \DateTime());
                }
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
        

        return $this->render('main/user/signup.html.twig', [
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
        return $this->render('main/user/profile.html.twig', [
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
    public function subscriptions(AbonnementRepository $repo, AbonnementDetailsRepository $repo0): Response
    {

        if ($repo->isUserSubscribed($this->getUser()->getId())) {
            return $this->render('main/user/mysubscriptions.html.twig', [
                'controller_name' => 'UserController',
                'subs' => $repo->getOldSubscriptionsByUserId($this->getUser()->getId()),
                'subnow' => $repo->getCurrentSubByUserId($this->getUser()->getId()),
                'diff' => $repo->getCurrentSubByUserId($this->getUser()->getId())->getDatefinab()->diff(new \DateTime())->format('%a')
            ]);
        }
        
        return $this->render('main/user/subscriptions.html.twig', [
            'controller_name' => 'UserController',
            'gp1price' => $repo0->getAbonnementPriceByName('GP 1'),
            'gp2price' => $repo0->getAbonnementPriceByName('GP 2'),
            'gp3price' => $repo0->getAbonnementPriceByName('GP 3')
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
        return $this->render('main/user/buy.html.twig', [
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
            $abonnement = new Abonnement();
            $abonnement->setUser($user);
            if ($gp == 1)
                $abonnement->setDatefinab(new \DateTime('+3 month'));
            else if($gp == 2)
                $abonnement->setDatefinab(new \DateTime('+6 month'));
            else
                $abonnement->setDatefinab(new \DateTime('+12 month'));
            $type = $repo0->getAbonnementDetailsByName('GP ' . $gp);
            $abonnement->setType($type);
            $reg->getManager()->persist($abonnement);
            $reg->getManager()->flush();
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


        return $this->render('dashboard/user/userlist.html.twig', [
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
        return $this->render('dashboard/user/profile.html.twig', [
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
        
        return $this->render('dashboard/user/profile.html.twig', [
            'controller_name' => 'UserController',
            'useredit' => $user,
            'form' => $form->createView()
        ]);
    }

    #[Route('/api/deleteuser/{id}', name: 'app_user_delete')]
    public function deleteUser(UserRepository $repo, $id, ManagerRegistry $reg): Response
    {
        $userdel = $repo->findUserById($id);
        if (!$userdel) {
            return new JsonResponse(['status' => 'error'], 400);
        }
        $reg->getManager()->remove($userdel);
        $reg->getManager()->flush();
        if ($this->getUser()->getId() == $userdel->getId()) {
            $this->get('security.token_storage')->setToken(null);
            $this->get('session')->invalidate();
            return new JsonResponse(['status' => 'success'], 200);
        }
        return new JsonResponse(['status' => 'success'], 200);
    }

    #[Route('/api/deletecurrentuser/', name: 'app_user_delete_current')]
    public function deleteCurrentUser(UserRepository $repo, ManagerRegistry $reg): Response
    {
        $userdel = $this->getUser();
        if (!$userdel) {
            return $this->redirectToRoute('app_usermgmt');
        }
        $reg->getManager()->remove($userdel);
        $reg->getManager()->flush();
        $this->get('security.token_storage')->setToken(null);
        $this->get('session')->invalidate();
        return $this->redirectToRoute('app_login');
    }

    #[Route('/dashboard/subscriptions', name: 'app_submgmt')]
    public function subscriptionManagement(AbonnementRepository $repo0, AbonnementDetailsRepository $repo2, UserRepository $repo1, ManagerRegistry $reg, Request $request): Response
    {
       
        
        if ($this->getUser()->getRole() == "admin"){

            $form = $this->createForm(GPPricesType::class);
            //init form values with 3 number fields
            $form->get('gpprice1')->setData($repo2->getAbonnementPriceByName('GP 1'));
            $form->get('gpprice2')->setData($repo2->getAbonnementPriceByName('GP 2'));
            $form->get('gpprice3')->setData($repo2->getAbonnementPriceByName('GP 3'));

            $form->handleRequest($request);

            if ($form->isSubmitted() && $form->isValid()) {
                $data = $form->getData();
                if ($data['gpprice1'] == null || $data['gpprice2'] == null || $data['gpprice3'] == null) {
                    $this->addFlash('error', 'Prices cannot be null!');
                    return $this->redirectToRoute('app_submgmt');
                }
                //check nan
                if (is_nan($data['gpprice1']) || is_nan($data['gpprice2']) || is_nan($data['gpprice3'])) {
                    $this->addFlash('error', 'Prices must be numbers!');
                    return $this->redirectToRoute('app_submgmt');
                }
                if ($data['gpprice1'] < 0 || $data['gpprice2'] < 0 || $data['gpprice3'] < 0) {
                    $this->addFlash('error', 'Prices cannot be negative!');
                    return $this->redirectToRoute('app_submgmt');
                }
                $repo2->getAbonnementDetailsByName('GP 1')->setPrix($data['gpprice1']);
                $repo2->getAbonnementDetailsByName('GP 2')->setPrix($data['gpprice2']);
                $repo2->getAbonnementDetailsByName('GP 3')->setPrix($data['gpprice3']);
                $reg->getManager()->flush();
            }
            return $this->render('dashboard/user/subscriptions.html.twig', [
                'controller_name' => 'UserController',
                'form' => $form->createView()
            ]);
        }
        return $this->render('dashboard/user/subscriptions.html.twig', [
            'controller_name' => 'UserController',
        ]);

    }
    

    #[Route('/api/subscribe/{id}/{sub}', name: 'app_subuser')]
    public function subscribeUser(UserRepository $repo, AbonnementRepository $repo1, AbonnementDetailsRepository $repo0, $id,$sub, ManagerRegistry $reg): Response
    {
        $user = $repo->findUserById($id);
        $abonnement = null;
        if ($repo1->isUserSubscribed($user->getId())) {
            $abonnement = $repo1->getCurrentSubByUserId($user->getId());
        }else{
            $abonnement = new Abonnement();
            $abonnement->setUser($user);
        }
        if ($sub == 1)
            $abonnement->setDatefinab((new \DateTime('+3 month')));
        else if($sub == 2)
            $abonnement->setDatefinab((new \DateTime('+6 month')));
        else
            $abonnement->setDatefinab((new \DateTime('+12 month')));
        $type = $repo0->getAbonnementDetailsByName('GP ' . $sub);
        $abonnement->setType($type);
        $reg->getManager()->persist($abonnement);
        $reg->getManager()->flush();
        return new JsonResponse(['status' => 'success'], 200);
    }

    #[Route('/api/unsubscribe/{id}', name: 'app_removesub')]
    public function unsubscribeUser(AbonnementRepository $repo, $id, ManagerRegistry $reg): Response
    {
        $sub = $repo->getCurrentSubByUserId($id);
        $reg->getManager()->remove($sub);
        $reg->getManager()->flush();
        return new JsonResponse(['status' => 'success'], 200);
    }

    #[Route('/api/getUserSubDetails/{id}', name: 'app_getsubdetails')]
    public function getUserSubDetails(UserRepository $repo0, AbonnementRepository $repo, $id): Response
    {
        $sub = $repo->getCurrentSubByUserId($id);
        $user = $repo0->findUserById($id);
        $status = $repo->isUserSubscribed($id);
        if ($status) {
            return new JsonResponse(['status' => $status, 'userphoto'=> $user->getPhoto(), 'subtype'=> $sub->getType()->getName(), 'expdate' => $sub->getDatefinab(), 'userid' => $user->getId(), 'userfn' => $user->getFirstname(), 'userln' => $user->getLastname(), 'userdob' => $user->getDateNaiss()], 200);
        }
        return new JsonResponse(['status' => $status, 'userphoto'=> $user->getPhoto(), 'userid' => $user->getId(), 'userfn' => $user->getFirstname(), 'userln' => $user->getLastname(), 'userdob' => $user->getDateNaiss()], 200);
        
    }

    #[Route('/api/checkface', name: 'app_faceidcheck')]
    public function checkFace(Request $request, UserRepository $user, ManagerRegistry $reg): Response
    {
        $image = $request->get('image');
        $user = $user->findUserByEmail($request->get('email'));
        if (!$user) {
            return new JsonResponse(['status' => 'error', 'details' => 'User does not exist!'], 400);
        }
        $faceid = $user->getFaceid();
        if (!$faceid) {
            return new JsonResponse(['status' => 'error', 'details' => 'User does not have a faceid!'], 400);
        }
        $fidts = $user->getFaceidTs();
        if ($fidts->diff(new \DateTime())->format('%a') >= 30) {
            $user->setFaceid(null);
            $user->setFaceidTs(null);
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            return new JsonResponse(['status' => 'error', 'details' => 'Faceid expired!'], 400);
        }
        try{
            $guzzle = new GuzzleClient();
            $resp = $guzzle->request('POST', 'https://api-us.faceplusplus.com/facepp/v3/detect', [
                'multipart' => [
                    [
                        'name' => 'api_key',
                        'contents' => 'oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6'
                    ],
                    [
                        'name' => 'api_secret',
                        'contents' => 'e76obC1xsr-zSMynWZoQCt62vWDgtZ6O'
                    ],
                    [
                        'name' => 'image_base64',
                        'contents' => $image,
                    ],
                    [
                        'name' => 'return_attributes',
                        'contents' => 'emotion'
                    ]
                ]
            ]);
            $faceidcmp = json_decode((string) $resp->getBody(), true)['faces'][0]['face_token'];
            dump("Request 1 Sent...");
            $resp = $guzzle->request('POST', 'https://api-us.faceplusplus.com/facepp/v3/compare', [
                'multipart' => [
                    [
                        'name' => 'api_key',
                        'contents' => 'oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6'
                    ],
                    [
                        'name' => 'api_secret',
                        'contents' => 'e76obC1xsr-zSMynWZoQCt62vWDgtZ6O'
                    ],
                    [
                        'name' => 'face_token1',
                        'contents' => $faceidcmp
                    ],
                    [
                        'name' => 'face_token2',
                        'contents' => $faceid
                    ]
                ]
            ]);
            dump("Request 2 Sent...");
            $confidence = json_decode((string) $resp->getBody(),true)['confidence'];
            if ($confidence > 80) {
                $user->setFaceid($faceidcmp);
                $user->setFaceidTs(new \DateTime());
                $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
                $this->get('security.token_storage')->setToken($token);
                $this->get('session')->set('_security_main', serialize($token));
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                return new JsonResponse(['status' => 'success', 'details'=> 'Match!'], 200);
            }else{
                return new JsonResponse(['status' => 'error', 'details'=> 'No match!'], 400);
            }
        } catch (\GuzzleHttp\Exception\RequestException $e) {
            $content = json_decode($e->getResponse()->getBody()->getContents(), true);
            return new JsonResponse(['status' => 'error', 'details'=> $content], 400);
        }
        return new JsonResponse(['status' => 'error', 'details'=> 'Error in network!'], 400);
    }

    #[Route('/api/verifylogin', name: 'app_verifylogin')]
    public function verifyLogin(Request $request, UserRepository $repo): Response
    {
        $form = $this->createForm(LoginType::class);
        $form->handleRequest($request);
        if ($form->isSubmitted()) {
            if (!$form->isValid()) {
                $errorArray = [];
                foreach ($form->getErrors(true) as $error) {
                    $errorArray[] = $error->getMessage();
                }
                return new JsonResponse(['status' => 'error', 'errors' => $errorArray], 200);
            }else{
                $userlog = $form->getData();
                $user = $repo->findUserByEmail($userlog->getEmail());
                if($user){
                    if(password_verify($userlog->getPassword(), $user->getPassword())){
                        $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
                        $this->get('security.token_storage')->setToken($token);
                        $this->get('session')->set('_security_main', serialize($token));
                        return new JsonResponse(['status' => 'success'], 200);
                    }else{
                        return new JsonResponse(['status' => 'error', 'errors' => ['Invalid Email or Password']], 200);
                    }
                }else{
                    return new JsonResponse(['status' => 'error', 'errors' => ['Invalid Email or Password']], 200);
                }
            }
        }
    }

    //checks if email exists
    #[Route('/api/verifyface', name: 'app_verifyface')]
    public function verifyEmail(Request $request, UserRepository $repo): Response
    {
        $email = $request->get('email');
        $user = $repo->findUserByEmail($email);
        if ($user && $user->getFaceid() != null) {
            if ($user->getFaceidTs()->diff(new \DateTime())->format('%a') < 30) {
                return new JsonResponse(['exists' => true], 200);
            }else{
                $user->setFaceid(null);
                $user->setFaceidTs(null);
                $repo->getManager()->persist($user);
                $repo->getManager()->flush();
            }
        }
        return new JsonResponse(['exists' => false], 200);
    }



    #[Route('/api/getfacetoken', name: 'app_getfacetoken')]
    public function getFaceToken(Request $request, UserRepository $repo): Response
    {
        $image = $request->get('image');
        try{
            $guzzle = new GuzzleClient();
            $resp = $guzzle->request('POST', 'https://api-us.faceplusplus.com/facepp/v3/detect', [
                'multipart' => [
                    [
                        'name' => 'api_key',
                        'contents' => 'oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6'
                    ],
                    [
                        'name' => 'api_secret',
                        'contents' => 'e76obC1xsr-zSMynWZoQCt62vWDgtZ6O'
                    ],
                    [
                        'name' => 'image_base64',
                        'contents' => $image,
                    ],
                    [
                        'name' => 'return_attributes',
                        'contents' => 'emotion'
                    ]
                ]
            ]);
            $faceidcmp = json_decode((string) $resp->getBody(), true)['faces'][0]['face_token'];
            dump("Request 1 Sent...");
            return new JsonResponse(['status' => 'success', 'facetoken' => $faceidcmp], 200);
        } catch (\GuzzleHttp\Exception\RequestException $e) {
            $content = json_decode($e->getResponse()->getBody()->getContents(), true);
            return new JsonResponse(['status' => 'error', 'details'=> $content], 400);
        }
    }

    #[Route('/api/updateface', name: 'app_updatefaceid')]
    public function updateFaceId(Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $image = $request->get('image');
        try{
            $guzzle = new GuzzleClient();
            $resp = $guzzle->request('POST', 'https://api-us.faceplusplus.com/facepp/v3/detect', [
                'multipart' => [
                    [
                        'name' => 'api_key',
                        'contents' => 'oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6'
                    ],
                    [
                        'name' => 'api_secret',
                        'contents' => 'e76obC1xsr-zSMynWZoQCt62vWDgtZ6O'
                    ],
                    [
                        'name' => 'image_base64',
                        'contents' => $image,
                    ],
                    [
                        'name' => 'return_attributes',
                        'contents' => 'emotion'
                    ]
                ]
            ]);
            $faceidcmp = json_decode((string) $resp->getBody(), true)['faces'][0]['face_token'];
            dump("Request 1 Sent...");
            $user = $this->getUser();
            $user->setFaceid($faceidcmp);
            $user->setFaceidTs(new \DateTime());
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            return new JsonResponse(['status' => 'success', 'facetoken' => $faceidcmp], 200);
        } catch (\GuzzleHttp\Exception\RequestException $e) {
            $content = json_decode($e->getResponse()->getBody()->getContents(), true);
            return new JsonResponse(['status' => 'error', 'details'=> $content], 400);
        }

    }

    #[Route('/api/updateface/{id}', name: 'app_updatefaceidmg')]
    public function updateFaceIdmg(Request $request, UserRepository $repo, $id, ManagerRegistry $reg): Response
    {
        $image = $request->get('image');
        try{
            $guzzle = new GuzzleClient();
            $resp = $guzzle->request('POST', 'https://api-us.faceplusplus.com/facepp/v3/detect', [
                'multipart' => [
                    [
                        'name' => 'api_key',
                        'contents' => 'oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6'
                    ],
                    [
                        'name' => 'api_secret',
                        'contents' => 'e76obC1xsr-zSMynWZoQCt62vWDgtZ6O'
                    ],
                    [
                        'name' => 'image_base64',
                        'contents' => $image,
                    ],
                    [
                        'name' => 'return_attributes',
                        'contents' => 'emotion'
                    ]
                ]
            ]);
            $faceidcmp = json_decode((string) $resp->getBody(), true)['faces'][0]['face_token'];
            dump("Request 1 Sent...");
            $user = $repo->findUserById($id);
            $user->setFaceid($faceidcmp);
            $user->setFaceidTs(new \DateTime());
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            return new JsonResponse(['status' => 'success', 'facetoken' => $faceidcmp], 200);
        } catch (\GuzzleHttp\Exception\RequestException $e) {
            $content = json_decode($e->getResponse()->getBody()->getContents(), true);
            return new JsonResponse(['status' => 'error', 'details'=> $content], 400);
        }
    }

    #[Route('/api/numberexist', name: 'app_numbercheck')]
    public function checkNumber(Request $request, UserRepository $repo): Response
    {
        $num = $request->get('phone');
        $user = $repo->findUserByPhone($num);
        if ($user) {
            return new JsonResponse(['exists' => true], 200);
        }
        return new JsonResponse(['exists' => false], 200);
    }

    #[Route('/api/resetpw', name: 'app_resetpw')]
    public function resetPassword(Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        $num = $request->get('phone');
        $user = $repo->findUserByPhone($num);
        if ($user) {
            $pw = $request->get('password');
            $user->setPassword(password_hash($pw, PASSWORD_BCRYPT));
            $reg->getManager()->persist($user);
            $reg->getManager()->flush();
            $this->addFlash('success', 'Password reset successfully!');
            return $this->redirectToRoute('app_login');
        }
        $this->addFlash('error', 'Error occured!');
        return $this->redirectToRoute('app_login');
    }
    
    #[Route('/api/ping', name: 'app_ping')]
    public function ping(): Response
    {
        return new JsonResponse(['status' => 'success'], 200);
    }

    #[Route('/api/userlist', name: 'app_userlist')]
    public function userList(UserRepository $repo, Request $req): Response
    {
       //datatable returns for ajax
        $users = [];
        $filter = $req->query->get('customSearch');
        if ($filter == null) 
            $users = $repo->findAll();
        else
            $users = $repo->getUserList($filter);
        $data = [];
        foreach ($users as $user) {
            $data[] = [
                $user->getPhoto(),
                $user->getId(),
                $user->getFirstname(),
                //$user->getUsername(),
                //$user->getDateNaiss()->format('Y-m-d'),
                $user->getLastname(),
                $user->getNumTel(),
                $user->getEmail(),
                $user->getAdresse(),
                $user->getRole(),
                
            ];
        }
        return new JsonResponse(['data' => $data], 200);
       
    }

    #[Route('/api/nonsubbedusers', name: 'app_nonsubbedusers')]
    public function nonSubbedUsers(AbonnementRepository $repo0, AbonnementDetailsRepository $repo2, UserRepository $repo1, ManagerRegistry $reg, Request $req): Response
    {
        $subs = $repo0->findAll();
        $filter = $req->query->get('customSearch');
        if ($filter == null) 
            $users = $repo1->getClientList();
        else
            $users = $repo1->getClientListFiltered($filter);
        $nonsubbedusers = [];
        $data = [];
        foreach ($users as $user) {
            if (!$repo0->isUserSubscribed($user->getId())) {
                array_push($nonsubbedusers, $user);
                $data[] = [
                    $user->getPhoto(),
                    $user->getId(),
                    $user->getFirstname() . ' ' . $user->getLastname(),
                    $user->getNumTel(),
                ];
            }
        }
        return new JsonResponse(['data' => $data], 200);
    }

    #[Route('/api/subbedusers', name: 'app_subbedusers')]
    public function subbedUsers(AbonnementRepository $repo0, AbonnementDetailsRepository $repo2, UserRepository $repo1, ManagerRegistry $reg, Request $req): Response
    {
        $subs = $repo0->findAll();
        $filter = $req->query->get('customSearch');
        if ($filter == null) 
            $users = $repo1->getClientList();
        else
            $users = $repo1->getClientListFiltered($filter);

        $subbedusers = array_map(function($user) use ($repo0) {
            return [
                'user' => $user,
                'sub' => $repo0->getCurrentSubByUserId($user->getId())
            ];
        }, array_filter($users, function($user) use ($repo0) {
            return $repo0->isUserSubscribed($user->getId());
        }));
        
        $data = [];
        foreach ($subbedusers as $user) {
            $data[] = [
                $user['user']->getPhoto(),
                $user['user']->getId(),
                $user['user']->getFirstname() . ' ' . $user['user']->getLastname(),
                $user['user']->getNumTel(),
                $user['sub']->getType()->getName(),
                $user['sub']->getDatefinab()->format('Y-m-d'),
            ];
        }
        return new JsonResponse(['data' => $data], 200);
    }

    #[Route('/api/googleauthcallback', name: 'app_googleauthcallback')]
    public function googleAuthCallback(Request $request, UserRepository $repo, ManagerRegistry $reg): Response
    {
        if (!$request->get('credential')) {
            return $this->redirectToRoute('app_login');
        }
        $client = new Google_Client(['client_id' => '767861281457-q8k0trd5fdj0jvgcf63lnclkqllkdt1e.apps.googleusercontent.com']);
        $payload = $client->verifyIdToken($request->get('credential'));
        if ($payload) {
            $userid = $payload['sub'];
            $user = $repo->findUserByEmail($payload['email']);
            if ($user) {
                $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
                $this->get('security.token_storage')->setToken($token);
                $this->get('session')->set('_security_main', serialize($token));
            }else{
                $lasteight = substr($userid, -8);
                $user = new User();
                $user->setId(intval($lasteight));
                $user->setEmail($payload['email']);
                $user->setFirstname($payload['given_name']);
                $user->setLastname($payload['family_name']);
                $user->setUsername($lasteight);
                $user->setNumTel($lasteight);
                $user->setPassword(password_hash($lasteight, PASSWORD_BCRYPT));
                $user->setAdresse('NA');
                $user->setDateNaiss(new \DateTime());
                $photo = file_get_contents($payload['picture']);
                $filename = 'USERIMG' . $user->getId() . '.jpg';
                $targetdir = $this->getParameter('kernel.project_dir') . '/public/profileuploads/';
                file_put_contents($targetdir . $filename, $photo);
                $user->setPhoto($filename);
                $user->setRole('client');
                $reg->getManager()->persist($user);
                $reg->getManager()->flush();
                $user = $repo->findUserByEmail($payload['email']);
                if ($user) {
                    $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
                    $this->get('security.token_storage')->setToken($token);
                    $this->get('session')->set('_security_main', serialize($token));
                }
            }
        }
        return $this->redirectToRoute('app_home');

    }
}
