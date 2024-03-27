<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\ObjectifRepository;
/**
 * Objectif
 *
 * @ORM\Table(name="objectif", indexes={@ORM\Index(name="userId", columns={"userId"}), @ORM\Index(name="coachId", columns={"CoachId"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: ObjectifRepository::class)]
class Objectif
{
    /**
     * @var int
     *
     * @ORM\Column(name="idObjectif", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer", nullable: false)]
    private $idobjectif;

    /**
     * @var float|null
     *
     * @ORM\Column(name="poidsObj", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: "float", precision: 10, scale: 0, nullable: true)]
    private $poidsobj;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="dateD", type="date", nullable=true)
     */
    #[ORM\Column(type: "date", nullable: true)]
    private $dated;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="dateF", type="date", nullable=true)
     */
    #[ORM\Column(type: "date", nullable: true)]
    private $datef;

    /**
     * @var float|null
     *
     * @ORM\Column(name="PoidsAct", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: "float", precision: 10, scale: 0, nullable: true)]
    private $poidsact;

    /**
     * @var float|null
     *
     * @ORM\Column(name="Taille", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: "float", precision: 10, scale: 0, nullable: true)]
    private $taille;

    /**
     * @var string|null
     *
     * @ORM\Column(name="Alergie", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $alergie;

    /**
     * @var string|null
     *
     * @ORM\Column(name="TypeObj", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $typeobj;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="CoachId", referencedColumnName="id")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "objectifs")]
    private $coachid;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="userId", referencedColumnName="id")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "objectifs")]
    private $userid;

    public function getIdobjectif(): ?int
    {
        return $this->idobjectif;
    }

    public function getPoidsobj(): ?float
    {
        return $this->poidsobj;
    }

    public function setPoidsobj(?float $poidsobj): static
    {
        $this->poidsobj = $poidsobj;

        return $this;
    }

    public function getDated(): ?\DateTimeInterface
    {
        return $this->dated;
    }

    public function setDated(?\DateTimeInterface $dated): static
    {
        $this->dated = $dated;

        return $this;
    }

    public function getDatef(): ?\DateTimeInterface
    {
        return $this->datef;
    }

    public function setDatef(?\DateTimeInterface $datef): static
    {
        $this->datef = $datef;

        return $this;
    }

    public function getPoidsact(): ?float
    {
        return $this->poidsact;
    }

    public function setPoidsact(?float $poidsact): static
    {
        $this->poidsact = $poidsact;

        return $this;
    }

    public function getTaille(): ?float
    {
        return $this->taille;
    }

    public function setTaille(?float $taille): static
    {
        $this->taille = $taille;

        return $this;
    }

    public function getAlergie(): ?string
    {
        return $this->alergie;
    }

    public function setAlergie(?string $alergie): static
    {
        $this->alergie = $alergie;

        return $this;
    }

    public function getTypeobj(): ?string
    {
        return $this->typeobj;
    }

    public function setTypeobj(?string $typeobj): static
    {
        $this->typeobj = $typeobj;

        return $this;
    }

    public function getCoachid(): ?User
    {
        return $this->coachid;
    }

    public function setCoachid(?User $coachid): static
    {
        $this->coachid = $coachid;

        return $this;
    }

    public function getUserid(): ?User
    {
        return $this->userid;
    }

    public function setUserid(?User $userid): static
    {
        $this->userid = $userid;

        return $this;
    }


}
