<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\EquipementsDetailsRepository;

#[ORM\Entity (repositoryClass: EquipementsDetailsRepository::class)]
class EquipementsDetails
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\Column]
    private ?string $name;

    #[ORM\Column]
    private ?string $description;

    #[ORM\Column]
    private ?string $dureeDeVie;

    #[ORM\Column]
    private ?string $etat;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(?string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getDureeDeVie(): ?string
    {
        return $this->dureeDeVie;
    }

    public function setDureeDeVie(?string $dureeDeVie): static
    {
        $this->dureeDeVie = $dureeDeVie;

        return $this;
    }

    public function getEtat(): ?string
    {
        return $this->etat;
    }

    public function setEtat(?string $etat): static
    {
        $this->etat = $etat;

        return $this;
    }


}
