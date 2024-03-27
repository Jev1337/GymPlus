<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\EquipementsDetailsRepository;

/**
 * EquipementsDetails
 *
 * @ORM\Table(name="equipements_details")
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: EquipementsDetailsRepository::class)]
class EquipementsDetails
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
    #[ORM\Column(type: 'integer', nullable: false)]
    private $id;

    /**
     * @var string|null
     *
     * @ORM\Column(name="name", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private $name;

    /**
     * @var string|null
     *
     * @ORM\Column(name="description", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private $description;

    /**
     * @var string|null
     *
     * @ORM\Column(name="duree_de_vie", type="string", length=255, nullable=true)
     */
    #[ORM\Column(name: 'duree_de_vie', type: 'string', length: 255, nullable: true)]
    private $dureeDeVie;

    /**
     * @var string|null
     *
     * @ORM\Column(name="etat", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private $etat;

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
