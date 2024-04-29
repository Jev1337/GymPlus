<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\PlanningRepository;

use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: PlanningRepository::class)]
class Planning
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $idPlanning;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field is required")]

    private ?string $trainingprog;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field is required")]
    private ?string $foodprog;


    #[ORM\ManyToOne(targetEntity: Objectif::class)]
    #[ORM\JoinColumn(name: "idobjectif", referencedColumnName: "idobjectif")]
    private ?Objectif $idobjectif;

    public function getIdPlanning(): ?int
    {
        return $this->idPlanning;
    }

    public function getTrainingprog(): ?string
    {
        return $this->trainingprog;
    }

    public function setTrainingprog(?string $trainingprog): static
    {
        $this->trainingprog = $trainingprog;

        return $this;
    }

    public function getFoodprog(): ?string
    {
        return $this->foodprog;
    }

    public function setFoodprog(?string $foodprog): static
    {
        $this->foodprog = $foodprog;

        return $this;
    }

    public function getIdobjectif(): ?Objectif
    {
        return $this->idobjectif;
    }

    public function setIdobjectif(?Objectif $idobjectif): static
    {
        $this->idobjectif = $idobjectif;

        return $this;
    }


}
