<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\DetailfactureRepository;

/**
 * Detailfacture
 *
 * @ORM\Table(name="detailfacture", indexes={@ORM\Index(name="FK_idProduit", columns={"idProduit"}), @ORM\Index(name="Fk_idFacture", columns={"idFacture"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: DetailfactureRepository::class)]
class Detailfacture
{
    /**
     * @var int
     *
     * @ORM\Column(name="idDetailFacture", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="NONE")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: 'NONE')]
    #[ORM\Column(type: 'integer', nullable: false)]
    private $iddetailfacture;

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixVenteUnitaire", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: 'float', precision: 10, scale: 0, nullable: true)]
    private $prixventeunitaire;

    /**
     * @var int|null
     *
     * @ORM\Column(name="quantite", type="integer", nullable=true)
     */
    #[ORM\Column(type: 'integer', nullable: true)]
    private $quantite;

    /**
     * @var float|null
     *
     * @ORM\Column(name="tauxRemise", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: 'float', precision: 10, scale: 0, nullable: true)]
    private $tauxremise;

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixTotalArticle", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: 'float', precision: 10, scale: 0, nullable: true)]
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
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: 'NONE')]
    #[ORM\OneToOne(targetEntity: "Facture")]
    private $idfacture;

    /**
     * @var \Produit
     *
     * @ORM\ManyToOne(targetEntity="Produit")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idProduit", referencedColumnName="idProduit")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "detailfactures")]
    private $idproduit;

    public function getIddetailfacture(): ?int
    {
        return $this->iddetailfacture;
    }

    public function getPrixventeunitaire(): ?float
    {
        return $this->prixventeunitaire;
    }

    public function setPrixventeunitaire(?float $prixventeunitaire): static
    {
        $this->prixventeunitaire = $prixventeunitaire;

        return $this;
    }

    public function getQuantite(): ?int
    {
        return $this->quantite;
    }

    public function setQuantite(?int $quantite): static
    {
        $this->quantite = $quantite;

        return $this;
    }

    public function getTauxremise(): ?float
    {
        return $this->tauxremise;
    }

    public function setTauxremise(?float $tauxremise): static
    {
        $this->tauxremise = $tauxremise;

        return $this;
    }

    public function getPrixtotalarticle(): ?float
    {
        return $this->prixtotalarticle;
    }

    public function setPrixtotalarticle(?float $prixtotalarticle): static
    {
        $this->prixtotalarticle = $prixtotalarticle;

        return $this;
    }

    public function getIdfacture(): ?Facture
    {
        return $this->idfacture;
    }

    public function setIdfacture(?Facture $idfacture): static
    {
        $this->idfacture = $idfacture;

        return $this;
    }

    public function getIdproduit(): ?Produit
    {
        return $this->idproduit;
    }

    public function setIdproduit(?Produit $idproduit): static
    {
        $this->idproduit = $idproduit;

        return $this;
    }


}
