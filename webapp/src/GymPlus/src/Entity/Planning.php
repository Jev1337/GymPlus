<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Planning
 *
 * @ORM\Table(name="planning", indexes={@ORM\Index(name="idObjectif", columns={"idObjectif"})})
 * @ORM\Entity
 */
class Planning
{
    /**
     * @var int
     *
     * @ORM\Column(name="id_Planning", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idPlanning;

    /**
     * @var string|null
     *
     * @ORM\Column(name="TrainingProg", type="text", length=65535, nullable=true)
     */
    private $trainingprog;

    /**
     * @var string|null
     *
     * @ORM\Column(name="FoodProg", type="text", length=65535, nullable=true)
     */
    private $foodprog;

    /**
     * @var \Objectif
     *
     * @ORM\ManyToOne(targetEntity="Objectif")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idObjectif", referencedColumnName="idObjectif")
     * })
     */
    private $idobjectif;


}
