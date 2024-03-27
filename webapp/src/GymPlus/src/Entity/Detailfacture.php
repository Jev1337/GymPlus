<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Detailfacture
 *
 * @ORM\Table(name="detailfacture", indexes={@ORM\Index(name="FK_idProduit", columns={"idProduit"}), @ORM\Index(name="Fk_idFacture", columns={"idFacture"})})
 * @ORM\Entity
 */
class Detailfacture
{
    /**
     * @var int
     *
     * @ORM\Column(name="idDetailFacture", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="NONE")
     */
    private $iddetailfacture;

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixVenteUnitaire", type="float", precision=10, scale=0, nullable=true)
     */
    private $prixventeunitaire;

    /**
     * @var int|null
     *
     * @ORM\Column(name="quantite", type="integer", nullable=true)
     */
    private $quantite;

    /**
     * @var float|null
     *
     * @ORM\Column(name="tauxRemise", type="float", precision=10, scale=0, nullable=true)
     */
    private $tauxremise;

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixTotalArticle", type="float", precision=10, scale=0, nullable=true)
     */
    private $prixtotalarticle;

    /**
     * @var \Facture
     *
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="NONE")
     * @ORM\OneToOne(targetEntity="Facture")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idFacture", referencedColumnName="idFacture")
     * })
     */
    private $idfacture;

    /**
     * @var \Produit
     *
     * @ORM\ManyToOne(targetEntity="Produit")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idProduit", referencedColumnName="idProduit")
     * })
     */
    private $idproduit;


}
