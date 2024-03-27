<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\ProduitRepository;

/**
 * Produit
 *
 * @ORM\Table(name="produit")
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: ProduitRepository::class)]
class Produit
{
    /**
     * @var int
     *
     * @ORM\Column(name="idProduit", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer", nullable: false)]
    private $idproduit;

    /**
     * @var string|null
     *
     * @ORM\Column(name="name", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $name;

    /**
     * @var float|null
     *
     * @ORM\Column(name="prix", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: "float", precision: 10, scale: 0, nullable: true)]
    private $prix;

    /**
     * @var int|null
     *
     * @ORM\Column(name="stock", type="integer", nullable=true)
     */
    #[ORM\Column(type: "integer", nullable: true)]
    private $stock;

    /**
     * @var string|null
     *
     * @ORM\Column(name="description", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $description;

    /**
     * @var string|null
     *
     * @ORM\Column(name="categorie", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $categorie;

    /**
     * @var string|null
     *
     * @ORM\Column(name="photo", type="text", length=65535, nullable=true)
     */
    #[ORM\Column(type: "text", length: 65535, nullable: true)]
    private $photo;

    /**
     * @var int|null
     *
     * @ORM\Column(name="seuil", type="integer", nullable=true)
     */
    #[ORM\Column(type: "integer", nullable: true)]
    private $seuil;

    /**
     * @var float|null
     *
     * @ORM\Column(name="promo", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: "float", precision: 10, scale: 0, nullable: true)]
    private $promo;

    public function getIdproduit(): ?int
    {
        return $this->idproduit;
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


}
