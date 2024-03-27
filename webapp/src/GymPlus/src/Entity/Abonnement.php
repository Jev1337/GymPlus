<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\AbonnementRepository;

/**
 * Abonnement
 *
 * @ORM\Table(name="abonnement", indexes={@ORM\Index(name="user_id", columns={"user_id"}), @ORM\Index(name="type", columns={"type"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: AbonnementRepository::class)]
class Abonnement
{
    /**
     * @var int
     *
     * @ORM\Column(name="id", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer", nullable: false)]
    private $id;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="dateFinAb", type="date", nullable=true)
     */
    #[ORM\Column(type: "date", nullable: true)]
    private $datefinab;

    /**
     * @var \AbonnementDetails
     *
     * @ORM\ManyToOne(targetEntity="AbonnementDetails")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="type", referencedColumnName="name")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "abonnements")]
    private $type;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="user_id", referencedColumnName="id")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "abonnements")]
    private $user;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDatefinab(): ?\DateTimeInterface
    {
        return $this->datefinab;
    }

    public function setDatefinab(?\DateTimeInterface $datefinab): static
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
