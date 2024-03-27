<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Facture
 *
 * @ORM\Table(name="facture", indexes={@ORM\Index(name="FK_id", columns={"id"})})
 * @ORM\Entity
 */
class Facture
{
    /**
     * @var int
     *
     * @ORM\Column(name="idFacture", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idfacture;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateVente", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    private $datevente = 'CURRENT_TIMESTAMP';

    /**
     * @var float|null
     *
     * @ORM\Column(name="prixTatalPaye", type="float", precision=10, scale=0, nullable=true)
     */
    private $prixtatalpaye;

    /**
     * @var string|null
     *
     * @ORM\Column(name="methodeDePaiement", type="string", length=255, nullable=true)
     */
    private $methodedepaiement;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="id", referencedColumnName="id")
     * })
     */
    private $id;


}
