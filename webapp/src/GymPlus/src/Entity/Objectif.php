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
    #[Assert\NotBlank(message: "Field requiredeeee")]
    #[Assert\GreaterThanOrEqual(value:0, message:"La quantité doit être un nombre positif")]
    private ?float $poidsobj;


    #[ORM\Column]
    private ?\DateTime $dated;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field required")]
    private ?\DateTime $datef;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field required")]
    private ?float $poidsact;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field required")]
    private ?float $taille;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Field required")]
    private ?string $alergie;

    #[ORM\Column]  
    #[Assert\NotBlank(message: "Field required")]
    private ?string $typeobj;


    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "userid", referencedColumnName: "id")]
    private ?User $userid;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "coachid", referencedColumnName: "id")]
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
