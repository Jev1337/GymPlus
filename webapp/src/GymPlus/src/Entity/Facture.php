<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\FactureRepository;

#[ORM\Entity(repositoryClass: FactureRepository::class)]
class Facture
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $idfacture;

    #[ORM\Column]
    private ?\DateTime $datevente;

    #[ORM\Column]
    private ?float $prixtatalpaye;

    #[ORM\Column]
    private ?string $methodedepaiement;

   #[ORM\ManyToOne(targetEntity: User::class)]
    private $id;

    public function getIdfacture(): ?int
    {
        return $this->idfacture;
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

    public function getPrixtatalpaye(): ?float
    {
        return $this->prixtatalpaye;
    }

    public function setPrixtatalpaye(?float $prixtatalpaye): static
    {
        $this->prixtatalpaye = $prixtatalpaye;

        return $this;
    }

    public function getMethodedepaiement(): ?string
    {
        return $this->methodedepaiement;
    }

    public function setMethodedepaiement(?string $methodedepaiement): static
    {
        $this->methodedepaiement = $methodedepaiement;

        return $this;
    }

    public function getId(): ?User
    {
        return $this->id;
    }

    public function setId(?User $id): static
    {
        $this->id = $id;

        return $this;
    }


}
