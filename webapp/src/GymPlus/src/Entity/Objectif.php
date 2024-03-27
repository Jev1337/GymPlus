<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Objectif
 *
 * @ORM\Table(name="objectif", indexes={@ORM\Index(name="userId", columns={"userId"}), @ORM\Index(name="coachId", columns={"CoachId"})})
 * @ORM\Entity
 */
class Objectif
{
    /**
     * @var int
     *
     * @ORM\Column(name="idObjectif", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idobjectif;

    /**
     * @var float|null
     *
     * @ORM\Column(name="poidsObj", type="float", precision=10, scale=0, nullable=true)
     */
    private $poidsobj;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="dateD", type="date", nullable=true)
     */
    private $dated;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="dateF", type="date", nullable=true)
     */
    private $datef;

    /**
     * @var float|null
     *
     * @ORM\Column(name="PoidsAct", type="float", precision=10, scale=0, nullable=true)
     */
    private $poidsact;

    /**
     * @var float|null
     *
     * @ORM\Column(name="Taille", type="float", precision=10, scale=0, nullable=true)
     */
    private $taille;

    /**
     * @var string|null
     *
     * @ORM\Column(name="Alergie", type="string", length=255, nullable=true)
     */
    private $alergie;

    /**
     * @var string|null
     *
     * @ORM\Column(name="TypeObj", type="string", length=255, nullable=true)
     */
    private $typeobj;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="CoachId", referencedColumnName="id")
     * })
     */
    private $coachid;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="userId", referencedColumnName="id")
     * })
     */
    private $userid;


}
