<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * EventDetails
 *
 * @ORM\Table(name="event_details")
 * @ORM\Entity
 */
class EventDetails
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
     * @ORM\Column(name="type", type="string", length=255, nullable=true)
     */
    private $type;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="event_date", type="datetime", nullable=true)
     */
    private $eventDate;

    /**
     * @var string|null
     *
     * @ORM\Column(name="duree", type="string", length=255, nullable=true)
     */
    private $duree;

    /**
     * @var int|null
     *
     * @ORM\Column(name="nb_places", type="integer", nullable=true)
     */
    private $nbPlaces;

    /**
     * @var int|null
     *
     * @ORM\Column(name="nb_total", type="integer", nullable=true)
     */
    private $nbTotal;

    /**
     * @var \Doctrine\Common\Collections\Collection
     *
     * @ORM\ManyToMany(targetEntity="User", inversedBy="eventDetails")
     * @ORM\JoinTable(name="event_participants",
     *   joinColumns={
     *     @ORM\JoinColumn(name="event_details_id", referencedColumnName="id")
     *   },
     *   inverseJoinColumns={
     *     @ORM\JoinColumn(name="user_id", referencedColumnName="id")
     *   }
     * )
     */
    private $user = array();

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->user = new \Doctrine\Common\Collections\ArrayCollection();
    }

}
