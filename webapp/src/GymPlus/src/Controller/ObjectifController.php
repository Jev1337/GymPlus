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
class ObjectifController extends AbstractController
{


    #[Route('/RedirectionToChatGpt', name: 'app_ChatGpt')]
    public function chatGpt( ? string $question, ? string $response): Response
    {
        return $this->render('main/test.html.twig', [
            'question' => $question,
            'response' => $response
        ]);
    }

   /* #[Route('/RedirectionToChatGpt', name: 'app_ChatGpt')]
    public function chatGpt( ? string $question, ? string $response): Response
    {
        $history = [];

        if ($question && $response) {
            $history = $this->get('session')->get('chat_history', []);
            
            $history[] = ['question' => $question, 'response' => $response];
            
            $this->get('session')->set('chat_history', $history);
        }
    
        return $this->render('main/OpenAi.html.twig', [
            'history' => $history
        ]);
    }*/
    
    #[Route('/chat', name: 'send_chat', methods:"POST")]
    public function chat(Request $request): Response
    {
        $question=$request->request->get('text');
        //Implémentation du chat gpt
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
        $filePath = 'C:/Users/MSI/IdeaProjects/GymPlus/webapp/src/GymPlus/src/arduino_Coming_Data.txt'; 
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
    



    #[Route('/RealTimeTracker', name: 'app_Tracker')]
    public function StatRedirectionTwig(Request $request): Response
    {
        $this->getArduinoData();
        $filePath = 'C:/Users/MSI/IdeaProjects/GymPlus/webapp/src/GymPlus/src/arduino_Coming_Data.txt';
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

// Set the ID of the chart element
$chart->setId('myChart');
        return $this->render('main/realtime.html.twig', [
            'chart' => $chart,
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

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $registry->getManager();
            $entityManager->flush();
            return $this->redirectToRoute('app_objectif');
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

        return $this->render('dashboard\gestionSuivi/plan.html.twig', [
            'controller_name' => 'ObjectifController',
            'user' => $user,
            'objectifs' => $objectifs,

        ]);
    }


    
    #[Route('/objectif/deletee/{id}', name: 'delete_Obj')]
    public function deleteObjective2($id, ManagerRegistry $registry): Response
    {
        $user = $this->getUser();
        $userId = $user->getId(); 
        $entityManager = $registry->getManager();
       
        $obj = $registry->getRepository(Objectif::class)->find($id);
        if (!$obj) {
            throw $this->createNotFoundException('Objective not found.');
        }
        $entityManager->remove($obj);
        $entityManager->flush();

        $objectifs = $entityManager->getRepository(Objectif::class)->findBy(['userid' => $userId]);

        $html = $this->renderView('dashboard\gestionSuivi/liste_obj.html.twig', [
            'objectifs' => $objectifs
        ]);

        return new Response($html);
    }
    



    #[Route('/make-planing/{objectiveId}', name: 'MakePlaning')]
    public function MakePlaning($objectiveId, ManagerRegistry $registry, Request $request): Response
    {  
        $form = $this->createForm(PlanningType::class);
        $user = $this->getUser();
       // $obj = $registry->getRepository(Objectif::class)->find($objectiveId);    
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                $plan=$form->getData();
              //  dd($request->files->get);
               dd($request);
                $entityManager = $registry->getManager();
                $entityManager->persist($plan);
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
           // 'obj' => $obj,
            'form' => $form->createView(),
        ]);
    }

   

  
}