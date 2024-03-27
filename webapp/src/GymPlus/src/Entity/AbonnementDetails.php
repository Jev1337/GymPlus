<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\AbonnementDetailsRepository;

/**
 * AbonnementDetails
 *
 * @ORM\Table(name="abonnement_details")
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: AbonnementDetailsRepository::class)]
class AbonnementDetails
{
    /**
     * @var string
     *
     * @ORM\Column(name="name", type="string", length=255, nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'string', length: 255, nullable: false)]
    private $name;

    /**
     * @var float
     *
     * @ORM\Column(name="prix", type="float", precision=10, scale=0, nullable=false)
     */
    #[ORM\Column(type: 'float', precision: 10, scale: 0, nullable: false)]
    private $prix;

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
