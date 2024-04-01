<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\MaintenancesRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: MaintenancesRepository::class)]
class Maintenances
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the maintenance date.')]
    #[Assert\LessThan('today', message: 'Date of maintenance must be in the past.')]
    #[Assert\GreaterThanOrEqual('-100 years', message: 'Date of maintenance must be at most 100 years ago.')]
    private ?\DateTime $dateMaintenance;


    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the maintenance status.')]
    #[Assert\Length(min: 3, max: 255, minMessage: 'Status must be at least 3 characters long.', maxMessage: 'Status must be at most 255 characters long.')]
    #[Assert\Type(type: "string", message: 'Status must be a string.')]
    #[Assert\Regex(pattern: "/[a-zA-Z0-9]*/", message: 'Status must contain only letters and numbers.')]
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
