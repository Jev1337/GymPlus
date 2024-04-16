<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\EquipementsDetailsRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity (repositoryClass: EquipementsDetailsRepository::class)]
class EquipementsDetails
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the equipment name.')]
    #[Assert\Length(min: 3, max: 255, minMessage: 'Name must be at least 3 characters long.', maxMessage: 'Name must be at most 255 characters long.')]
    #[Assert\Type(type: "string", message: 'Name must be a string.')]
    #[Assert\Regex(pattern: "/[a-zA-Z0-9]*/", message: 'Name must contain only letters and numbers.')]
    private ?string $name;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the equipment description.')]
    #[Assert\Length(min: 3, max: 255, minMessage: 'Description must be at least 3 characters long.', maxMessage: 'Description must be at most 255 characters long.')]
    #[Assert\Type(type: "string", message: 'Description must be a string.')]
    #[Assert\Regex(pattern: "/[a-zA-Z0-9]*/", message: 'Description must contain only letters and numbers.')]
    private ?string $description;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the equipment duree de vie.')]
    #[Assert\Length(min: 1, max: 255, minMessage: 'Duree de vie must be at least 1 character long.', maxMessage: 'Duree de vie must be at most 255 characters long.')]
    #[Assert\Type(type: "string", message: 'Duree de vie must be a string.')]
    #[Assert\Regex(pattern: "/[0-9]*/", message: 'Duree de vie must contain only numbers.')]
    private ?string $dureeDeVie;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter the equipment etat.')]
    #[Assert\Length(min: 3, max: 255, minMessage: 'Etat must be at least 3 characters long.', maxMessage: 'Etat must be at most 255 characters long.')]
    #[Assert\Type(type: "string", message: 'Etat must be a string.')]
    #[Assert\Regex(pattern: "/[a-zA-Z0-9]*/", message: 'Etat must contain only letters and numbers.')]
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
