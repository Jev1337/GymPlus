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

/* #[Route('/RealTimeTracker', name: 'app_Tracker')] 
 public function getArduinoData(Request $request): StreamedResponse
 {
        $arduinoIp = '192.168.1.162'; 
        $arduinoPort = 80; 
        $streamedResponse = new StreamedResponse();
        $streamedResponse->setCallback(function () use ($arduinoIp, $arduinoPort) {
            $fp = fsockopen($arduinoIp, $arduinoPort, $errno, $errstr, 30);
            if ($fp) {
                fwrite($fp, "GET / HTTP/1.0\r\n\r\n");
                while (!feof($fp)) {
                    echo fgets($fp);
                }
                fclose($fp);
            } else {
                echo "Failed to connect to the Arduino";
            }
        });
    
        $streamedResponse->headers->set('Content-Type', 'text/plain');
        $streamedResponse->headers->set('Cache-Control', 'no-cache');
    
        return $streamedResponse;
    }*/

  

    #[Route('/StroringData', name: 'app_StroringData')]
    public function getArduinoData(): StreamedResponse
    {
        

        $filePath = 'C:/Users/MSI/IdeaProjects/GymPlus/webapp/src/GymPlus/src/aaaa.txt'; 
        
        $streamedResponse = new StreamedResponse();

        $streamedResponse->setCallback(function () use ($filePath) {
            $fp = fsockopen('192.168.1.162', 80, $errno, $errstr, 30); 
            
            if ($fp) {

                $file = fopen($filePath, 'a'); 
                if ($file) {
                    while (!feof($fp)) {
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


  /*  #[Route('/RealTimeTracker', name: 'app_Tracker')]
    public function StatRedirectionTwig(Request $request): Response
    {
    return $this->render('main\test2.html.twig', [

    ]);
    }
*/






    
    
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

        return $this->render('dashboard\gestionSuivi/plan.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'objectifs' => $objectifs,
             'objFinished'=> $objFinished,
             'objNotFinished'=> $objNotFinished,

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
    



    #[Route('/make-planing/{objectiveId}', name: 'MakePlaning')]
    public function MakePlaning($objectiveId, ManagerRegistry $registry, Request $request): Response
    {  
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
            $file = new \Symfony\Component\HttpFoundation\File\File($photo1);
            $filename = 'PlanIMG' . $plan->getTrainingprog() . '.' . $file->guessExtension();
            $targetdir = $this->getParameter('kernel.project_dir') . '/public/capturesPlaning/';
            $file->move($targetdir, $filename);
            $user->setPhoto($filename);


                $entityManager = $registry->getManager();
                $entityManager->persist($plan);
                $entityManager->flush();

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
