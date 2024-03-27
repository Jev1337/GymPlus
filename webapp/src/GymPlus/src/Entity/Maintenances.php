<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Maintenances
 *
 * @ORM\Table(name="maintenances", indexes={@ORM\Index(name="equipements_details_id", columns={"equipements_details_id"})})
 * @ORM\Entity
 */
class Maintenances
{
    /**
     * @var int
     *
     * @ORM\Column(name="id", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $id;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="date_maintenance", type="date", nullable=true)
     */
    private $dateMaintenance;

    /**
     * @var string|null
     *
     * @ORM\Column(name="status", type="string", length=255, nullable=true)
     */
    private $status;

    /**
     * @var \EquipementsDetails
     *
     * @ORM\ManyToOne(targetEntity="EquipementsDetails")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="equipements_details_id", referencedColumnName="id")
     * })
     */
    private $equipementsDetails;


}
