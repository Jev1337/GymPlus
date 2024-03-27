<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\BlackListedRepository;

#[ORM\Entity (repositoryClass: BlackListedRepository::class)]
class BlackListed
{
    #[ORM\Column (options: ["default" => "CURRENT_TIMESTAMP"])]
    private ?\DateTime $startBan;

    #[ORM\Column (options: ["default" => "CURRENT_TIMESTAMP"])]
    private ?\DateTime $endBan;

    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(nullable: false)]
    private $idUser;

    public function getStartBan(): ?\DateTimeInterface
    {
        return $this->startBan;
    }

    public function setStartBan(\DateTimeInterface $startBan): static
    {
        $this->startBan = $startBan;

        return $this;
    }

    public function getEndBan(): ?\DateTimeInterface
    {
        return $this->endBan;
    }

    public function setEndBan(\DateTimeInterface $endBan): static
    {
        $this->endBan = $endBan;

        return $this;
    }

    public function getIdUser(): ?User
    {
        return $this->idUser;
    }

    public function setIdUser(?User $idUser): static
    {
        $this->idUser = $idUser;

        return $this;
    }


}
