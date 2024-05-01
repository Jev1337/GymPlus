<?php

namespace App\Controller;

use App\Entity\Detailfacture;
use App\Entity\Facture;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Dompdf\Dompdf;
use Dompdf\Options;

class CustomPdfController extends AbstractController
{
    public function generatePdf(Request $request, $idFacture)
    {
        // Récupérer la facture
        $facture = $this->getDoctrine()->getRepository(Facture::class)->find($idFacture);

        // Récupérer les détails de la facture associés à cette facture
        $detailsFacture = $this->getDoctrine()->getRepository(Detailfacture::class)->findBy(['idfacture' => $facture]);

        // Récupérer le contenu HTML de la vue Twig
        $html = $this->renderView('panier/facturePDF.html.twig', [
            'facture' => $facture,
            'detailsFacture' => $detailsFacture,
        ]);

        // Configure Dompdf
        $options = new Options();
        $options->set('isHtml5ParserEnabled', true);

        // Instancier Dompdf
        $dompdf = new Dompdf($options);

        // Charger le contenu HTML dans Dompdf
        $dompdf->loadHtml($html);

        // Définir la taille et l'orientation du document
        $dompdf->setPaper('A4', 'portrait');

        // Rendre le PDF
        $dompdf->render();

        // Retourner le PDF en réponse HTTP
        return new Response(
            $dompdf->output(),
            Response::HTTP_OK,
            [
                'Content-Type' => 'application/pdf',
                'Content-Disposition' => 'inline; filename="facture.pdf"'
            ]
        );
    }
}

