<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Abonnement
 *
 * @ORM\Table(name="abonnement", indexes={@ORM\Index(name="user_id", columns={"user_id"}), @ORM\Index(name="type", columns={"type"})})
 * @ORM\Entity
 */
class Abonnement
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
     * @ORM\Column(name="dateFinAb", type="date", nullable=true)
     */
    private $datefinab;

    /**
     * @var \AbonnementDetails
     *
     * @ORM\ManyToOne(targetEntity="AbonnementDetails")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="type", referencedColumnName="name")
     * })
     */
    private $type;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="user_id", referencedColumnName="id")
     * })
     */
    private $user;


}
