<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\MaintenancesRepository;

/**
 * Maintenances
 *
 * @ORM\Table(name="maintenances", indexes={@ORM\Index(name="equipements_details_id", columns={"equipements_details_id"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: MaintenancesRepository::class)]
class Maintenances
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
     * @ORM\Column(name="date_maintenance", type="date", nullable=true)
     */
    #[ORM\Column(type: "date", nullable: true)]
    private $dateMaintenance;

    /**
     * @var string|null
     *
     * @ORM\Column(name="status", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private $status;

    /**
     * @var \EquipementsDetails
     *
     * @ORM\ManyToOne(targetEntity="EquipementsDetails")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="equipements_details_id", referencedColumnName="id")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "maintenances")]
    private $equipementsDetails;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDateMaintenance(): ?\DateTimeInterface
    {
        return $this->dateMaintenance;
    }

    public function setDateMaintenance(?\DateTimeInterface $dateMaintenance): static
    {
        $this->dateMaintenance = $dateMaintenance;

        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->status;
    }

    public function setStatus(?string $status): static
    {
        $this->status = $status;

        return $this;
    }

    public function getEquipementsDetails(): ?EquipementsDetails
    {
        return $this->equipementsDetails;
    }

    public function setEquipementsDetails(?EquipementsDetails $equipementsDetails): static
    {
        $this->equipementsDetails = $equipementsDetails;

        return $this;
    }


}
