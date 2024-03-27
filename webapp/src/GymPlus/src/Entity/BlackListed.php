<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\BlackListedRepository;

/**
 * BlackListed
 *
 * @ORM\Table(name="black_listed", indexes={@ORM\Index(name="id_user", columns={"id_user"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: BlackListedRepository::class)]
class BlackListed
{
    /**
     * @var \DateTime
     *
     * @ORM\Column(name="start_ban", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    #[ORM\Column(type: 'date', nullable: false, options: ['default' => 'CURRENT_TIMESTAMP'])]
    private $startBan = 'CURRENT_TIMESTAMP';

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="end_ban", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    #[ORM\Column(type: 'date', nullable: false, options: ['default' => 'CURRENT_TIMESTAMP'])]
    private $endBan = 'CURRENT_TIMESTAMP';

    /**
     * @var \User
     *
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="NONE")
     * @ORM\OneToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="id_user", referencedColumnName="id")
     * })
     */
    #[ORM\Id]
    #[ORM\OneToOne(targetEntity: "User")]
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
