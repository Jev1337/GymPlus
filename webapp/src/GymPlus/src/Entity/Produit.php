<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\ProduitRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ProduitRepository::class)]
class Produit
{
    // #[ORM\Id]
    // #[ORM\GeneratedValue]
    // #[ORM\Column]
    #[ORM\Id]
    #[ORM\Column(name: 'idProduit', type: 'integer')]
    #[ORM\GeneratedValue(strategy: 'IDENTITY')]
    private ?int $idproduit;

    #[ORM\Column]
    #[Assert\NotBlank(message:"Name Product is required.")]
    private ?string $name;

    #[ORM\Column]
    #[Assert\Positive(message:"Positive price is required.")]
    private ?float $prix;

    #[ORM\Column]
    #[Assert\Positive(message:"Positive stock is required.")]
    private ?int $stock;

    #[ORM\Column]
    private ?string $description;

    #[ORM\Column]
    private ?string $categorie;


    #[ORM\Column]
    private ?string $photo;

    #[ORM\Column]
    #[Assert\Positive(message:"Positive limit is required.")]
    private ?int $seuil;

    #[ORM\Column]
    private ?float $promo;

    public function getIdproduit(): ?int
    {
        // return $this->idproduit;
        // Vérifie si idproduit est initialisée avant d'y accéder
    if (isset($this->idproduit)) {
        return $this->idproduit;
    } else {
        return null;
    }
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(?string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(?float $prix): static
    {
        $this->prix = $prix;

        return $this;
    }

    public function getStock(): ?int
    {
        return $this->stock;
    }

    public function setStock(?int $stock): static
    {
        $this->stock = $stock;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getCategorie(): ?string
    {
        return $this->categorie;
    }

    public function setCategorie(?string $categorie): static
    {
        $this->categorie = $categorie;

        return $this;
    }

    public function getPhoto(): ?string
    {
        return $this->photo;
    }

    public function setPhoto(?string $photo): static
    {
        $this->photo = $photo;

        return $this;
    }

    public function getSeuil(): ?int
    {
        return $this->seuil;
    }

    public function setSeuil(?int $seuil): static
    {
        $this->seuil = $seuil;

        return $this;
    }

    public function getPromo(): ?float
    {
        return $this->promo;
    }

    public function setPromo(?float $promo): static
    {
        $this->promo = $promo;

        return $this;
    }

    public function __toString()
    {
        // Retourne une représentation de l'objet Produit sous forme de chaîne
        return $this->idproduit; // Vous pouvez retourner n'importe quelle propriété qui représente le produit
    }


}
