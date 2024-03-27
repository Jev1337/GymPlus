<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\AbonnementRepository;


#[ORM\Entity(repositoryClass: AbonnementRepository::class)]
class Abonnement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\Column]
    private ?date $datefinab;

    #[ORM\ManyToOne(targetEntity: AbonnementDetails::class)]
    private ?string $type;

    #[ORM\ManyToOne(targetEntity: User::class)]
    private ?int $user;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDatefinab(): ?\DateTime
    {
        return $this->datefinab;
    }

    public function setDatefinab(?\DateTime $datefinab): static
    {
        $this->datefinab = $datefinab;

        return $this;
    }

    public function getType(): ?AbonnementDetails
    {
        return $this->type;
    }

    public function setType(?AbonnementDetails $type): static
    {
        $this->type = $type;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }


}
