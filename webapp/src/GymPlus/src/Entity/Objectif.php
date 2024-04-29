<?php
namespace App\Entity;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\ObjectifRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ObjectifRepository::class)]
class Objectif
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $idobjectif;
    #[ORM\Column]
    #[Assert\NotBlank(message: "Field is required")]
    #[Assert\Type(type: 'float', message: "Only numeric values are allowed for Weight.")]
    #[Assert\Regex(
        pattern: '/^\d+(\.\d+)?$/',
        message: "Only numeric values are allowed for poidsobj."
    )]
    #[Assert\Range(min: 30, max: 150, minMessage: "Field must be at least 30", maxMessage: "Field must be at most 150")]
    private ?float $poidsobj;
    #[ORM\Column]
    private ?\DateTime $dated;

    #[Assert\NotBlank(message: "Field required")]
    #[ORM\Column(type: "datetime", nullable: true)]
    #[Assert\GreaterThanOrEqual(value: "+1 month", message: "Date field must be greater than or equal to one month from today")]
    private ?\DateTime $datef;

    #[ORM\Column(type: "float", nullable: true)]
    #[Assert\NotBlank(message: "Field required")]
    #[Assert\Type(type: 'float', message: "Only numeric values are allowed for Weight.")]
    #[Assert\Range(min: 30, max: 150, minMessage: "Field must be at least 30", maxMessage: "Field must be at most 150")]
    private ?float $poidsact;

    #[ORM\Column(type: "float", nullable: true)]
    #[Assert\NotBlank(message: "Field required")]
    #[Assert\Type(type: 'float', message: "Only numeric values are allowed for Weight.")]
    #[Assert\Range(min: 140, max: 220, minMessage: "Field must be at least 140 cm", maxMessage: "Field must be at most 220cm")]
    private ?float $taille;

    #[ORM\Column]
    #[Assert\Length(max: 20)]
/**
     * @Assert\Regex(
     *     pattern="/^[a-zA-Z\s', ]*$/",
     *     message="Invalid characters detected."
     * )
     */    private ?string $alergie;

    #[ORM\Column]  
    #[Assert\NotBlank(message: "Select an option")]
     private ?string $typeobj;


    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "userid", referencedColumnName: "id")]
    private ?User $userid;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "coachid", referencedColumnName: "id")]
    #[Assert\NotBlank(message: "Select an option")]
    private ?User $coachid;

 

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

    public function getDated(): ?\DateTime
    {
        return $this->dated;
    }

    public function setDated(?\DateTime $dated): static
    {
        $this->dated = $dated;

        return $this;
    }

    public function getDatef(): ?\DateTime
    {
        return $this->datef;
    }

    public function setDatef(?\DateTime $datef): static
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
