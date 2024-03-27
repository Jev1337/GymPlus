<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\DetailfactureRepository;


#[ORM\Entity(repositoryClass: DetailfactureRepository::class)]
class Detailfacture
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $iddetailfacture;

    #[ORM\Column]
    private ?float $prixventeunitaire;

    #[ORM\Column]
    private ?int $quantite;

    #[ORM\Column]
    private ?float $tauxremise;

    #[ORM\Column]
    private ?float $prixtotalarticle;

    #[ORM\ManyToOne(targetEntity: Facture::class)]
    private $idfacture;

    #[ORM\ManyToOne(targetEntity: Produit::class)]
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
