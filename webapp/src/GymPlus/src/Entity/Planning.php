<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\PlanningRepository;

/**
 * Planning
 *
 * @ORM\Table(name="planning", indexes={@ORM\Index(name="idObjectif", columns={"idObjectif"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: PlanningRepository::class)]
class Planning
{
    /**
     * @var int
     *
     * @ORM\Column(name="id_Planning", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer", nullable: false)]
    private $idPlanning;

    /**
     * @var string|null
     *
     * @ORM\Column(name="TrainingProg", type="text", length=65535, nullable=true)
     */
    #[ORM\Column(type: "text", length: 65535, nullable: true)]
    private $trainingprog;

    /**
     * @var string|null
     *
     * @ORM\Column(name="FoodProg", type="text", length=65535, nullable=true)
     */
    #[ORM\Column(type: "text", length: 65535, nullable: true)]
    private $foodprog;

    /**
     * @var \Objectif
     *
     * @ORM\ManyToOne(targetEntity="Objectif")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idObjectif", referencedColumnName="idObjectif")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "plannings")]
    private $idobjectif;

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
