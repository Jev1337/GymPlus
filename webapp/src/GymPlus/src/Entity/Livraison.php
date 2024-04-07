<?php

namespace App\Entity;

use App\Repository\LivraisonRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LivraisonRepository::class)]
class Livraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private $idlivraison;

    #[ORM\Column]

    private ?int $idfacture;

    #[ORM\Column]
    private ?int $idclient;

    #[ORM\Column]
    private ?string $lieu;

    #[ORM\Column]
    private ?string $etat;

    public function getIdlivraison(): ?int
    {
        return $this->idlivraison;
    }

    public function getIdfacture(): ?int
    {
        return $this->idfacture;
    }

    public function setIdfacture(?int $idfacture): static
    {
        $this->idfacture = $idfacture;

        return $this;
    }

    public function getIdclient(): ?int
    {
        return $this->idclient;
    }

    public function setIdclient(?int $idclient): static
    {
        $this->idclient = $idclient;

        return $this;
    }

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(?string $lieu): static
    {
        $this->lieu = $lieu;

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


}
