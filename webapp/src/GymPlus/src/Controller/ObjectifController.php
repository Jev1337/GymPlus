<?php
namespace App\Controller;
use App\Form\ObjectifType;
use App\Form\PlanningType;
use App\Entity\Objectif;
use App\Entity\Planning;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Entity\User; 
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\HttpClient\HttpClient;
use Symfony\Component\HttpFoundation\StreamedResponse;
use Symfony\Component\Process\Process;
use Symfony\Component\EventSource\Event;
use Symfony\Component\EventSource\EventSource;
use Mukadi\ChartJSBundle\Factory\ChartFactory;
use Mukadi\Chart\ChartView;
use Mukadi\Chart\ChartBuilder;
use Mukadi\Chart\Type;
use Mukadi\Chart\ChartBuilderInterface;
use OpenAI;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Console\Output\ConsoleOutput;
use App\Service\WebSocketServer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mime\Part\DataPart;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;


class ObjectifController extends AbstractController
{

 /*   #[Route('/chat', name: 'send_chat', methods:"POST")]
public function chat(Request $request): Response
{
    $question=$request->request->get('text');
    $myApiKey = $_ENV['OPENAI_KEY'];
    $client = OpenAI::client($myApiKey);
    $result = $client->completions()->create([
        'model' => 'gpt-3.5-turbo-instruct',
        'prompt' => $question,
        'max_tokens'=>2048
    ]);
    $response=$result->choices[0]->text;
    return new JsonResponse(['response' => $response]);
}
*/

  /*  #[Route('/chat', name: 'send_chat', methods:"POST")]
    public function chat(Request $request): JsonResponse
    {
        $question = $request->request->get('text');
        $myApiKey = $_ENV['OPENAI_KEY'];
        $client = OpenAI::client($myApiKey);
        $result = $client->completions()->create([
            'model' => 'gpt-3.5-turbo-instruct',
            'prompt' => $question,
            'max_tokens'=>2048
        ]);
        $response = $result->choices[0]->text;
        dump($response); 

        return new JsonResponse(['response' => $response]);
    }*/


  /*  #[Route('/RedirectionToChatGpt', name: 'app_ChatGpt')]
    public function chatGpt( ): Response
    {
        return $this->render('main/test2.html.twig', [
            
        ]);
    }*/
 /*   

    #[Route('/RedirectionToChatGpt', name: 'app_ChatGpt')]
    public function chatGpt( ? string $question, ? string $response): Response
    {
        return $this->render('main/test3.html.twig', [
            'question' => $question,
            'response' => $response
        ]);
    }


    
   #[Route('/chat', name: 'send_chat', methods:"POST")]
    public function chat(Request $request): Response
    {
        $question=$request->request->get('text');
        $myApiKey = $_ENV['OPENAI_KEY'];
        $client = OpenAI::client($myApiKey);
        $result = $client->completions()->create([
            'model' => 'gpt-3.5-turbo-instruct',
            'prompt' => $question,
            'max_tokens'=>2048
        ]);
        $response=$result->choices[0]->text;
  
        
        return $this->forward('App\Controller\ObjectifController::chatGpt', [
           
            'question' => $question,
            'response' => $response
        ]);
    }
*/

    /*#[Route('/add_Objectif', name: 'app_objectif')]
    public function add_Objectif(Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $obj = new Objectif();
        $form = $this->createForm(ObjectifType::class, $obj);
        $userId = $user->getId(); 
        $entityManager = $registry->getManager();
        $userEntity = $entityManager->getRepository(User::class)->find($userId); 
        $obj->setUserid($userEntity);
        $currentDate = new \DateTime(); // Get the current date and time
        $obj->setDated($currentDate); // Set the current date in the "dated" field

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $registry->getManager();
            $entityManager->persist($obj);
            $entityManager->flush();
            $this->addFlash(
                'success',
                'Success! Adding successfully submitted.'
            );
            return $this->redirectToRoute('app_objectif'); 
        }
        return $this->render('main\ObjectifFront.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user, 
            'form' => $form->createView(),
        ]);
    }*/
  
    #[Route('/add_Objectif', name: 'app_objectif')]
    public function add_Objectif(Request $request, ManagerRegistry $registry): Response {
        $user = $this->getUser();
        $obj = new Objectif();
        $form = $this->createForm(ObjectifType::class, $obj);
        $userId = $user->getId();
        $entityManager = $registry->getManager();
        $userEntity = $entityManager->getRepository(User::class)->find($userId);
        $obj->setUserid($userEntity);
        $currentDate = new \DateTime();
        $obj->setDated($currentDate);
        $form->handleRequest($request);
    
        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                $entityManager->persist($obj);
                $entityManager->flush();
    
                if ($request->isXmlHttpRequest()) {
                    return new JsonResponse(['success' => 'Success! Adding successfully submitted.']);
                } else {
                    $this->addFlash(
                        'success',
                        'Success! Adding successfully submitted.'
                    );
                    return $this->redirectToRoute('app_objectif');
                }
            } else {
                $errors = $form->getErrors(true);
                $errorsArray = [];
                foreach ($errors as $error) {
                    $errorsArray[$error->getOrigin()->getName()] = $error->getMessage();
                }
                return new JsonResponse($errorsArray, 400);
            }
        }
    
        return $this->render('main\ObjectifFront.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'form' => $form->createView(),
        ]);
    }
    



    #[Route('/Schedule_Objectif', name: 'app_Schedule_objectif')]
    public function Schedule_Objectif(Request $request, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $userId = $user->getId();
        $entityManager = $registry->getManager();
        $objectifs = $entityManager->getRepository(Objectif::class)->findBy(['userid' => $userId]);

        return $this->render('main/displayObjectif.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'objectifs' => $objectifs,
        ]);
    }


  

    #[Route('/StroringData', name: 'app_StroringData')]
    public function getData(): Response
    {
        
        //kernel + aaaa.txt
        $filePath = 'aaaa.txt';
        
        $streamedResponse = new StreamedResponse();

        $streamedResponse->setCallback(function () use ($filePath) {
            $fp = fsockopen('172.21.3.131', 80, $errno, $errstr, 30); 
            
            if ($fp) {

                $file = fopen($filePath, 'a'); 
                if ($file) {
                    $i = 0;
                    while (!feof($fp) && $i < 200) {
                        $i++;
                        $data = fgets($fp);
                        fwrite($file, $data);
                        echo $data;
                        flush(); 
                    }
                    fclose($file);
                } else {
                    echo "Failed to open the file";
                }
                fclose($fp);
            } else {
                echo "Failed to connect to the Arduino";
            }
            
        });
    
        $streamedResponse->headers->set('Content-Type', 'text/plain');
        $streamedResponse->headers->set('Cache-Control', 'no-cache');
        return $streamedResponse;
        
    }










    
    #[Route('/StoringData', name: 'app_StoringData')]
    public function getArduinoData(): Response
    {
        
        //kernel + aaaa.txt
        $filePath = 'aaaa.txt';
        
        $streamedResponse = new StreamedResponse();

        $streamedResponse->setCallback(function () use ($filePath) {
            $fp = fsockopen('192.168.50.65', 80, $errno, $errstr, 30); 
            
            if ($fp) {

                $file = fopen($filePath, 'a'); 
                if ($file) {
                    if (!feof($fp)) {
                        $data = fgets($fp);
                        fwrite($file, $data);
                        echo $data;
                        flush(); 
                    }
                    fclose($file);
                } else {
                    echo "Failed to open the file";
                }
                fclose($fp);
            } else {
                echo "Failed to connect to the Arduino";
            }
            
        });
    
        $streamedResponse->headers->set('Content-Type', 'text/plain');
        $streamedResponse->headers->set('Cache-Control', 'no-cache');
        return $streamedResponse;
        
    }
    



   /* #[Route('/RealTimeTracker', name: 'app_Tracker')]
    public function StatRedirectionTwig(Request $request): Response
    {
        $this->getArduinoData();

        $filePath = 'C:/Users/MSI/IdeaProjects/GymPlus/webapp/src/GymPlus/src/aaaa.txt';
        $values = file($filePath, FILE_IGNORE_NEW_LINES);
        $chart = new ChartView(ChartView::LINE);


     foreach ($values as $value) {
        $labels[] = round(microtime(true) * 1000);

    }
    $chart->setLabels($labels);


    $dataset = [
        'label' => 'Dataset 1',
        'data' => $values, 
        'backgroundColor' => 'rgba(255, 99, 132, 0.2)',
        'borderColor' => 'rgba(255, 99, 132, 1)',
        'borderWidth' => 1,
    ];
    $chart->setDatasets([$dataset]);
    $chart->pushOptions([
    'scales' => [
        'y' => [
            'beginAtZero' => true,
        ],
    ],
]);
$chart->setId('myChart');
        return $this->render('main/test2.html.twig', [
            'chart' => $chart,
            
        ]);
    }
*/
#[Route('/RealTimeTracker', name: 'app_Tracker')]
public function StatRedirectionTwig(Request $request): Response
{
    $this->getArduinoData();

    $filePath = 'aaaa.txt';
    $values = file($filePath, FILE_IGNORE_NEW_LINES);
    $chart = new ChartView(ChartView::LINE);


    foreach ($values as $value) {
        $labels[] = round(microtime(true) * 1000);

    }
    $chart->setLabels($labels);


    $dataset = [
        'label' => 'Dataset 1',
        'data' => $values, 
        'backgroundColor' => 'rgba(255, 99, 132, 0.2)',
        'borderColor' => 'rgba(255, 99, 132, 1)',
        'borderWidth' => 1,
    ];
    $chart->setDatasets([$dataset]);
    $chart->pushOptions([
    'scales' => [
        'y' => [
            'beginAtZero' => true,
        ],
    ],
    ]);
    $chart->setId('myChart');
        return $this->render('main/test2.html.twig', [
            'chart' => $chart,
            
        ]);
    }


  /*  #[Route('/RealTimeTracker', name: 'app_Tracker')]
    public function StatRedirectionTwig(Request $request): Response
    {
    return $this->render('main\test2.html.twig', [

    ]);
    }
*/

#[Route('/BmiCalculator', name: 'app_bmi')]
    public function Bmi(Request $request): Response
    {
    return $this->render('main\BMI_Calculator.html.twig', [

    ]);
    }




    
    
#[Route('/ExerciceGenerator', name: 'app_ExerciceGenerator')]
public function getExercices(Request $request): Response
{
    
    return $this->render('main\ExercicesGenerator.html.twig', [
        'controller_name' => 'ObjectifController',
    ]);
}


#[Route('/generatedex', name: 'app_GeneratedEx')]
public function getGeneratedExercices(Request $request): Response
{
    $response = $request->query->get('response');
    $responseArray = json_decode($response, true);

    return $this->render('main\GeneratedExercices.html.twig', [
        'responseArray' => $responseArray,
        'controller_name' => 'ObjectifController',

    ]);
}


    #[Route('/objectif/delete/{id}', name: 'obj_delete')]
    public function deleteObjective($id, ManagerRegistry $registry): Response
    {
        $obj = $registry->getRepository(Objectif::class)->find($id);
        $entityManager = $registry->getManager();
        $entityManager->remove($obj);
        $entityManager->flush();
        return $this->redirectToRoute('app_Schedule_objectif');
    }

 

    
    #[Route('/objectif/edit/{id}', name: 'obj_edit')]
    public function edit($id, Request $request, ManagerRegistry $registry): Response
    {  
        $user = $this->getUser();
        $obj = $registry->getRepository(Objectif::class)->find($id);
        $form = $this->createForm(ObjectifType::class, $obj);
        $form->handleRequest($request);
        $entityManager = $registry->getManager();

       if ($form->isSubmitted()) {
            if ($form->isValid()) {
                $entityManager->flush();
    
                if ($request->isXmlHttpRequest()) {
                    return new JsonResponse(['success' => 'Success! Adding successfully submitted.']);
                } else {
                    $this->addFlash(
                        'success',
                        'Success! Adding successfully submitted.'
                    );
                    return $this->redirectToRoute('app_objectif');
                }
            } else {
                $errors = $form->getErrors(true);
                $errorsArray = [];
                foreach ($errors as $error) {
                    $errorsArray[$error->getOrigin()->getName()] = $error->getMessage();
                }
                return new JsonResponse($errorsArray, 400);
            }
        }
        return $this->render('main\ObjectifFront.html.twig', [
            'controller_name' => 'ObjectifController',
            'form' => $form->createView(),
            'user' => $user,
        ]);
    }


    

      
    #[Route('/dashbord_Objective', name: 'Dashbord_Objective')]
    public function dash_obj(ManagerRegistry $registry): Response
    {  
     

        $user = $this->getUser();
        $userId = $user->getId(); 
        $entityManager = $registry->getManager();
        $objectifs = $entityManager->getRepository(Objectif::class)->findBy(['coachid' => $userId]);
        $objFinished = $entityManager->getRepository(Planning::class)->createQueryBuilder('p')
        ->innerJoin('p.idobjectif', 'o')
        ->where('o.coachid = :userId')
        ->andWhere('p.idobjectif = o.idobjectif')
        ->getQuery()
        ->setParameter('userId', $userId)
        ->getResult();

        $objectifsDefaultType = $entityManager->getRepository(Objectif::class)
        ->findBy([
            'coachid' => $userId,
            'typeobj' => 'Default' 
        ]);
        $countTypeDefault = count($objectifsDefaultType);


        $notInPlanning = $entityManager->createQueryBuilder()
        ->select('o.idobjectif')
        ->from(Planning::class, 'p')
        ->innerJoin('p.idobjectif', 'o')
        ->getQuery()
        ->getResult();
    
    $objNotFinished = $entityManager->createQueryBuilder()
        ->select('o')
        ->from(Objectif::class, 'o')
        ->where('o.idobjectif NOT IN (:notInPlanning)')
        ->andWhere('o.coachid = :userId')
        ->setParameter('notInPlanning', $notInPlanning)
        ->setParameter('userId', $userId)
        ->getQuery()
        ->getResult();

   

        $count = count($objFinished);
        $count2 = count($objNotFinished);
        $count3 =  $count2 +  $count ;
        $stats = [
            ['title' => 'Plans Finished', 'content' => $count, 'percent' => '100'],
            ['title' => 'Plans Unfinished', 'content' => $count2 ,'percent' => '100'],
            ['title' => 'Plans Count', 'content' => $count3 ,'percent' => '100'],
            ['title' => 'Default Objectif Type', 'content' => $countTypeDefault ,'percent' => '100'],

        ];


        
        $gainWeight = 0;
        $loseWeight = 0;
        $maintainWeight = 0;

        foreach ($objNotFinished as $obj) {
            if ($obj->getPoidsobj() > $obj->getPoidsact()) {
                $gainWeight++;
            }
            if ($obj->getPoidsobj() < $obj->getPoidsact()) {
                $loseWeight++;
            }
            if ($obj->getPoidsobj() == $obj->getPoidsact()) {
                $maintainWeight++;
            }
        }

        $chart = new ChartView(ChartView::BAR);
        $values = [  $gainWeight,$loseWeight, $maintainWeight];
        $labels = ["Gain Weight", "Lose Weight", "Maintain Weig"];
        $chart->setLabels($labels);
        $dataset = [
            'label' => 'Unfinished Plans',
            'data' => $values, 
            'backgroundColor' => 'rgba(255, 99, 132, 0.2)',
            'borderColor' => 'rgba(255, 99, 132, 1)',
            'borderWidth' => 1,
        ];
        $chart->setDatasets([$dataset]);
        $chart->pushOptions([
        'scales' => [
            'y' => [
                'beginAtZero' => true,
            ],
        ],
    ]);

    $gainWeight2 = 0;
        $loseWeight2 = 0;
        $maintainWeight2 = 0;
    foreach ($objectifs as $obj2) {
        if ($obj2->getPoidsobj() > $obj2->getPoidsact()) {
            $gainWeight2++;
        }
        if ($obj2->getPoidsobj() < $obj2->getPoidsact()) {
            $loseWeight2++;
        }
        if ($obj2->getPoidsobj() == $obj2->getPoidsact()) {
            $maintainWeight2++;
        }
    }

    $chart2 = new ChartView(ChartView::BAR);
    $values2 = [  $gainWeight2, $loseWeight2, $maintainWeight2];
    $labels2 = ["Gain Weight", "Lose Weight", "Maintain Weight"];
    $chart2->setLabels($labels2);
    $dataset2 = [
        'label' => 'All The Plans',
        'data' => $values2, 
        'backgroundColor' => 'rgba(255, 99, 132, 0.2)',
        'borderColor' => 'rgba(255, 99, 132, 1)',
        'borderWidth' => 1,
    ];
    $chart2->setDatasets([$dataset2]);
    $chart2->pushOptions([
    'scales' => [
        'y' => [
            'beginAtZero' => true,
        ],
    ],
]);


        return $this->render('dashboard\gestionSuivi/plan.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'objectifs' => $objectifs,
             'objFinished'=> $objFinished,
             'objNotFinished'=> $objNotFinished,
             'stats' => $stats,
             'chart' => $chart,
             'chart2' => $chart2,

        ]);
    }


    
    #[Route('/objectif/deletee/{id}', name: 'delete_Obj')]
    public function deleteObjective2($id, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $userId = $user->getId(); 
        $entityManager = $registry->getManager();

        $objFinished = $entityManager->getRepository(Planning::class)->createQueryBuilder('p')
        ->innerJoin('p.idobjectif', 'o')
        ->where('o.coachid = :userId')
        ->andWhere('p.idobjectif = o.idobjectif')
        ->getQuery()
        ->setParameter('userId', $userId)
        ->getResult();
       
        $notInPlanning = $entityManager->createQueryBuilder()
        ->select('o.idobjectif')
        ->from(Planning::class, 'p')
        ->innerJoin('p.idobjectif', 'o')
        ->getQuery()
        ->getResult();
    
    $objNotFinished = $entityManager->createQueryBuilder()
        ->select('o')
        ->from(Objectif::class, 'o')
        ->where('o.idobjectif NOT IN (:notInPlanning)')
        ->andWhere('o.coachid = :userId')
        ->setParameter('notInPlanning', $notInPlanning)
        ->setParameter('userId', $userId)
        ->getQuery()
        ->getResult();
        $objectifs = $entityManager->getRepository(Objectif::class)->findBy(['coachid' => $userId]);
       // $objectifs = $registry->getRepository(Objectif::class)->find($id);
        if (!$objectifs) {
            throw $this->createNotFoundException('Objective not found.');
        }
        $entityManager->remove($objectifs);
        $entityManager->flush();


        $html = $this->renderView('dashboard\gestionSuivi/liste_obj.html.twig', [
            'objectifs' => $objectifs,
            'objFinished'=> $objFinished,
             'objNotFinished'=> $objNotFinished,
        ]);

        return new Response($html);
    }
    

    #[Route('/test', name: 'test')]
    public function test(): Response
    {
        return $this->render('main\email.html.twig', [
        
    
        ]);
    }



    #[Route('/make-planing/{objectiveId}', name: 'MakePlaning')]
    public function MakePlaning($objectiveId, ManagerRegistry $registry, Request $request,MailerInterface $mailer): Response
    {  
        $entityManager = $registry->getManager();

        $objFinished = $entityManager->getRepository(Planning::class)->createQueryBuilder('p')
        ->innerJoin('p.idobjectif', 'o')
        ->andWhere('p.idobjectif = :objectifId')
        ->setParameter('objectifId', $objectiveId)
        ->getQuery()
        ->getResult();

        $isVar1InList = 0;
if ($objFinished !== null) {
    $isVar1InList = 1;

}



        $plan = new Planning();
        $form = $this->createForm(PlanningType::class, $plan);
        $user = $this->getUser();
        $obj = $registry->getRepository(Objectif::class)->find($objectiveId);    
        $plan->setIdobjectif($obj);
        $form->handleRequest($request);

        $planningList = $registry->getRepository(Planning::class)->findBy(['idobjectif' => $obj]);


        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                

             $photo1 = $form['foodprog']->getData();    
           // $filename = 'PlanIMG' . $plan->getFoodprog() . '.jpg'; 
            $filename = 'FoodDiet.jpg'; 
        
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/capturesExercices/';
            $file = new \Symfony\Component\HttpFoundation\File\File($photo1);
            $file->move($targetdir, $filename);
            

            $photo2 = $form['trainingprog']->getData();
            $filename2 = 'ExercicesProg.jpg'; 
            $targetdir2 = $this->getParameter('kernel.project_dir') . '/public/capturesPlaning/';
            $file2 = new \Symfony\Component\HttpFoundation\File\File($photo2);
            $file2->move($targetdir2, $filename2);



            $user1 = $obj->getUserid();   
            $useremail=$user1->getEmail();
        
                $entityManager = $registry->getManager();
                $entityManager->persist($plan);
                $entityManager->flush();
                
                $email = (new TemplatedEmail())
                ->from('gymplus-noreply@grandelation.com')
                ->to($useremail)
                ->subject('Your Plans Are Ready!')
                ->text('Stay Consistent!')
              
                ->attachFromPath($this->getParameter('kernel.project_dir') . '/public/capturesExercices/FoodDiet.jpg', 'Exercices.jpg', 'image/jpg')
                ->attachFromPath($this->getParameter('kernel.project_dir') . '/public/capturesPlaning/ExercicesProg.jpg', 'Diet.jpg', 'image/jpg')
                //send the two pics.jpg too as attachement
            
                
                ->htmlTemplate('main/email.html')
                ->context([
                    'Diet.jpg' => 'cid:Diet.jpg',
                    'Exercice.jpg' => 'cid:Exercices.jpg',
                ]);
                $mailer->send($email);

                //->html('<p>Stay Consistent!</p><img src="cid:Diet" alt="Plan Image"><img src="cid:Exercices" alt="Exercices Image">')


                if ($request->isXmlHttpRequest()) {
                    return new JsonResponse(['success' => 'Success! Ajax kae3d yet3ada.']);
                } else {
                    $this->addFlash(
                        'nope',
                        'nope! AJAX mech ka3ed yet3ada .'
                    );
                    return $this->redirectToRoute('Dashbord_Objective');
                }
            } else {
                $errors = $form->getErrors(true);
                $errorsArray = [];
                foreach ($errors as $error) {
                    $errorsArray[$error->getOrigin()->getName()] = $error->getMessage();
                }
                return new JsonResponse($errorsArray, 400);
            }
        }

        return $this->render('dashboard\gestionSuivi/MakingPlaning.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'obj' => $obj,
            'form' => $form->createView(),
            'planningList' => $planningList,
            'isVar1InList' => $isVar1InList
        ]);
    }

   
    #[Route('/plan/updatePlan/{idPlanning}/{objectiveId}', name: 'updatePlan')]
    public function updatePlan($idPlanning, $objectiveId, Request $request, ManagerRegistry $registry): Response
    {  
       $user = $this->getUser();
       $plan = $registry->getRepository(Planning::class)->find($idPlanning);
       $form = $this->createForm(PlanningType::class, $plan);
        $form->handleRequest($request);
        $obj = $registry->getRepository(Objectif::class)->find($objectiveId);    

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                $photo1 = $form['foodprog']->getData();
            $file = new \Symfony\Component\HttpFoundation\File\File($photo1);
            $filename = 'PlanIMG' . $plan->getTrainingprog() . '.' . $file->guessExtension();
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/capturesPlansUpdated/';
            $file->move($targetdir, $filename);
            $user->setPhoto($filename);
    
    
                $entityManager = $registry->getManager();
                $entityManager->flush();
    
                if ($request->isXmlHttpRequest()) {
                    return new JsonResponse(['success' => 'Success! Adding successfully submitted.']);
                } else {
                    $this->addFlash(
                        'success',
                        'Success! Adding successfully submitted.'
                    );
                    return $this->redirectToRoute('Dashbord_Objective');
                }
            } else {
                $errors = $form->getErrors(true);
                $errorsArray = [];
                foreach ($errors as $error) {
                    $errorsArray[$error->getOrigin()->getName()] = $error->getMessage();
                }
                return new JsonResponse($errorsArray, 400);
            }
        }

        return $this->render('dashboard\gestionSuivi/MakingPlaning.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'obj' => $obj,
            'form' => $form->createView(),
            'planningList' => $plan,
        ]);
    }
    
  
    

    #[Route('/objectif/deletePlan/{idPlanning}/{objectiveId}', name: 'deletePlan')]
    public function deletePlan($idPlanning, $objectiveId, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $userId = $user->getId(); 
        $entityManager = $registry->getManager();

        $obj = $registry->getRepository(Objectif::class)->find($objectiveId);    
        $planningList = $registry->getRepository(Planning::class)->findBy(['idobjectif' => $obj]);

        $Planning = $registry->getRepository(Planning::class)->find($idPlanning);
        if (!$Planning) {
            throw $this->createNotFoundException('Planning not found.');
        }
        $entityManager->remove($Planning);
        $entityManager->flush();


        $html = $this->renderView('dashboard\gestionSuivi/listeAfterDelteDash.html.twig', [
            'planningList'=> $planningList,
            'obj'=>$obj,
        ]);

        return new Response($html);
    }
}


