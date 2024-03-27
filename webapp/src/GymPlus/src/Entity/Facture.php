<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\FactureRepository;

/**
 * Facture
 *
 * @ORM\Table(name="facture", indexes={@ORM\Index(name="FK_id", columns={"id"})})
 * @ORM\Entity
 */
#[ORM\Entity(repositoryClass: FactureRepository::class)]
class Facture
{
    /**
     * @var int
     *
     * @ORM\Column(name="idFacture", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer", nullable: false)]
    private $idfacture;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateVente", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    #[ORM\Column(type: 'date', nullable: false, options: ['default' => 'CURRENT_TIMESTAMP'])]
    private $datevente = 'CURRENT_TIMESTAMP';

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixTatalPaye", type="float", precision=10, scale=0, nullable=true)
     */
    #[ORM\Column(type: 'float', precision: 10, scale: 0, nullable: true)]
    private $prixtatalpaye;

    /**
     * @var string|null
     *
     * @ORM\Column(name="methodeDePaiement", type="string", length=255, nullable=true)
     */
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private $methodedepaiement;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="id", referencedColumnName="id")
     * })
     */
    #[ORM\ManyToOne(inversedBy: "factures")]
    private $id;

    public function getIdfacture(): ?int
    {
        return $this->idfacture;
    }

    public function getDatevente(): ?\DateTimeInterface
    {
        return $this->datevente;
    }

    public function setDatevente(\DateTimeInterface $datevente): static
    {
        $this->datevente = $datevente;

        return $this;
    }

    public function getPrixtatalpaye(): ?float
    {
        return $this->prixtatalpaye;
    }

    public function setPrixtatalpaye(?float $prixtatalpaye): static
    {
        $this->prixtatalpaye = $prixtatalpaye;

        return $this;
    }

    public function getMethodedepaiement(): ?string
    {
        return $this->methodedepaiement;
    }

    public function setMethodedepaiement(?string $methodedepaiement): static
    {
        $this->methodedepaiement = $methodedepaiement;

        return $this;
    }

    public function getId(): ?User
    {
        return $this->id;
    }

    public function setId(?User $id): static
    {
        $this->id = $id;

        return $this;
    }


}
