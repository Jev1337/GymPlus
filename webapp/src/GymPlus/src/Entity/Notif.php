<?php

namespace App\Entity;

use App\Repository\NotifRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: NotifRepository::class)]
class Notif
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: "idProduit", nullable: true)]
    private ?int $idProduit = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $Description = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $etat = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $etatCommentaire = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $titre = null;

    #[ORM\Column(name: "nomUser" , length: 255, nullable: true)]
    private ?string $nomUser = null;

    #[ORM\Column]
    private ?\DateTime $datevente;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getIdProduit(): ?int
    {
        return $this->idProduit;
    }

    public function setIdProduit(?int $idProduit): static
    {
        $this->idProduit = $idProduit;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->Description;
    }

    public function setDescription(?string $Description): static
    {
        $this->Description = $Description;

        return $this;
    }

    public function getEtat(): ?string
    {
        return $this->etat;
    }

    public function setEtat(?string $etat): static
    {
        $this->etat = $etat;

        return $this;
    }

    public function getEtatCommentaire(): ?string
    {
        return $this->etatCommentaire;
    }

    public function setEtatCommentaire(?string $etatCommentaire): static
    {
        $this->etatCommentaire = $etatCommentaire;

        return $this;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(?string $titre): static
    {
        $this->titre = $titre;

        return $this;
    }

    public function getNomUser(): ?string
    {
        return $this->nomUser;
    }

    public function setNomUser(?string $nomUser): static
    {
        $this->nomUser = $nomUser;

        return $this;
    }

    public function getDatevente(): ?\DateTime
    {
        return $this->datevente;
    }

    public function setDatevente(\DateTime $datevente): static
    {
        $this->datevente = $datevente;

        return $this;
    }

   
}
