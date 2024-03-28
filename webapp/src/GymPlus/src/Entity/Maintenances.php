<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\MaintenancesRepository;

#[ORM\Entity(repositoryClass: MaintenancesRepository::class)]
class Maintenances
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\Column]
    private ?\DateTime $dateMaintenance;


    #[ORM\Column]
    private ?string $status;

    #[ORM\ManyToOne(targetEntity: EquipementsDetails::class)]
    private $equipementsDetails;


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDateMaintenance(): ?\DateTime
    {
        return $this->dateMaintenance;
    }

    public function setDateMaintenance(?\DateTime $dateMaintenance): static
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
