<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\FactureRepository;

#[ORM\Entity(repositoryClass: FactureRepository::class)]
class Facture
{
    // #[ORM\Id]
    // #[ORM\GeneratedValue]
    // #[ORM\Column]
    #[ORM\Id]
    #[ORM\Column(name: 'idFacture', type: 'integer')]
    #[ORM\GeneratedValue(strategy: 'AUTO')]
    private ?int $idfacture;

    #[ORM\Column]
    private ?\DateTime $datevente;

    #[ORM\Column]
    private ?float $prixtatalpaye;

    #[ORM\Column]
    private ?string $methodedepaiement;

  
//    #[ORM\ManyToOne(targetEntity: User::class)]
//    #[ORM\JoinColumn(name: 'user_id', referencedColumnName: 'id')]
   #[ORM\ManyToOne(targetEntity: User::class, inversedBy: 'factures')]
   #[ORM\JoinColumn(name: 'id', referencedColumnName: 'id', nullable: false)]

    private $user;//id;

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
        return $this->user;//id;
    }

    public function setId(?User $user): static
    {
        $this->user = $user;

        return $this;
    }

    public function __construct()
    {
        $this->datevente = new \DateTime();

        //$this->ListeDetails = new ArrayCollection();

        
    }

    public function __toString(): string
    {
        return $this->getIdfacture();
    }

}
