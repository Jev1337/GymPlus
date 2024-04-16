<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\AbonnementDetailsRepository;

#[ORM\Entity (repositoryClass: AbonnementDetailsRepository::class)]
class AbonnementDetails
{

    #[ORM\Id]
    #[ORM\Column]
    private ?string $name;

    #[ORM\Column]
    private ?float $prix;

    public function getName(): ?string
    {
        return $this->name;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): static
    {
        $this->prix = $prix;

        return $this;
    }


}
