<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * User
 *
 * @ORM\Table(name="user")
 * @ORM\Entity
 */
class User
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
     * @ORM\Column(name="username", type="string", length=255, nullable=true)
     */
    private $username;

    /**
     * @var string|null
     *
     * @ORM\Column(name="firstname", type="string", length=255, nullable=true)
     */
    private $firstname;

    /**
     * @var string|null
     *
     * @ORM\Column(name="lastname", type="string", length=255, nullable=true)
     */
    private $lastname;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="date_naiss", type="date", nullable=true)
     */
    private $dateNaiss;

    /**
     * @var string|null
     *
     * @ORM\Column(name="password", type="string", length=255, nullable=true)
     */
    private $password;

    /**
     * @var string|null
     *
     * @ORM\Column(name="email", type="string", length=255, nullable=true)
     */
    private $email;

    /**
     * @var string|null
     *
     * @ORM\Column(name="role", type="string", length=255, nullable=true)
     */
    private $role;

    /**
     * @var string|null
     *
     * @ORM\Column(name="num_tel", type="string", length=255, nullable=true)
     */
    private $numTel;

    /**
     * @var string|null
     *
     * @ORM\Column(name="adresse", type="text", length=65535, nullable=true)
     */
    private $adresse;

    /**
     * @var string|null
     *
     * @ORM\Column(name="photo", type="text", length=65535, nullable=true)
     */
    private $photo;

    /**
     * @var int|null
     *
     * @ORM\Column(name="event_points", type="integer", nullable=true)
     */
    private $eventPoints;

    /**
     * @var string|null
     *
     * @ORM\Column(name="faceid", type="string", length=255, nullable=true)
     */
    private $faceid;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="faceid_ts", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    private $faceidTs = 'CURRENT_TIMESTAMP';

    /**
     * @var \Doctrine\Common\Collections\Collection
     *
     * @ORM\ManyToMany(targetEntity="EventDetails", mappedBy="user")
     */
    private $eventDetails = array();

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->eventDetails = new \Doctrine\Common\Collections\ArrayCollection();
    }

}
