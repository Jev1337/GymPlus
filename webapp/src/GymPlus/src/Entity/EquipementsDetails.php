<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * EquipementsDetails
 *
 * @ORM\Table(name="equipements_details")
 * @ORM\Entity
 */
class EquipementsDetails
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
     * @var string|null
     *
     * @ORM\Column(name="name", type="string", length=255, nullable=true)
     */
    private $name;

    /**
     * @var string|null
     *
     * @ORM\Column(name="description", type="string", length=255, nullable=true)
     */
    private $description;

    /**
     * @var string|null
     *
     * @ORM\Column(name="duree_de_vie", type="string", length=255, nullable=true)
     */
    private $dureeDeVie;

    /**
     * @var string|null
     *
     * @ORM\Column(name="etat", type="string", length=255, nullable=true)
     */
    private $etat;


}
