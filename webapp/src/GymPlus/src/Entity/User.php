<?php

namespace App\Entity;

use App\Repository\UserRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;



#[ORM\Entity(repositoryClass: UserRepository::class)]
class User
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
    #[ORM\Column]
    private ?int $id;

    /**
     * @var string|null
     *
     * @ORM\Column(name="username", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $username;

    /**
     * @var string|null
     *
     * @ORM\Column(name="firstname", type="string", length=255, nullable=true)
     */
    #[ORM\Column( length: 255)]
    private ?string $firstname;

    /**
     * @var string|null
     *
     * @ORM\Column(name="lastname", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $lastname;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(name="date_naiss", type="date", nullable=true)
     */
    #[ORM\Column]
    private ?date $dateNaiss;

    /**
     * @var string|null
     *
     * @ORM\Column(name="password", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $password;

    /**
     * @var string|null
     *
     * @ORM\Column(name="email", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $email;

    /**
     * @var string|null
     *
     * @ORM\Column(name="role", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $role;

    /**
     * @var string|null
     *
     * @ORM\Column(name="num_tel", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $numTel;

    /**
     * @var string|null
     *
     * @ORM\Column(name="adresse", type="text", length=65535, nullable=true)
     */
    #[ORM\Column]
    private ?string $adresse;

    /**
     * @var string|null
     *
     * @ORM\Column(name="photo", type="text", length=65535, nullable=true)
     */
    #[ORM\Column(length: 65535)]
    private ?string $photo;

    /**
     * @var int|null
     *
     * @ORM\Column(name="event_points", type="integer", nullable=true)
     */
    #[ORM\Column]
    private ?int $eventPoints;

    /**
     * @var string|null
     *
     * @ORM\Column(name="faceid", type="string", length=255, nullable=true)
     */
    #[ORM\Column(length: 255)]
    private ?string $faceid;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="faceid_ts", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    #[ORM\Column(options: ["default" => "CURRENT_TIMESTAMP"])]
    private ?date $faceidTs;

    /**
     * @var \Doctrine\Common\Collections\Collection
     *
     * @ORM\ManyToMany(targetEntity="EventDetails", mappedBy="user")
     */
    #[ORM\ManyToMany(mappedBy: "user")]
    private $eventDetails = array();

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->eventDetails = new \Doctrine\Common\Collections\ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getUsername(): ?string
    {
        return $this->username;
    }

    public function setUsername(?string $username): static
    {
        $this->username = $username;

        return $this;
    }

    public function getFirstname(): ?string
    {
        return $this->firstname;
    }

    public function setFirstname(?string $firstname): static
    {
        $this->firstname = $firstname;

        return $this;
    }

    public function getLastname(): ?string
    {
        return $this->lastname;
    }

    public function setLastname(?string $lastname): static
    {
        $this->lastname = $lastname;

        return $this;
    }

    public function getDateNaiss(): ?\DateTimeInterface
    {
        return $this->dateNaiss;
    }

    public function setDateNaiss(?\DateTimeInterface $dateNaiss): static
    {
        $this->dateNaiss = $dateNaiss;

        return $this;
    }

    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(?string $password): static
    {
        $this->password = $password;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(?string $email): static
    {
        $this->email = $email;

        return $this;
    }

    public function getRole(): ?string
    {
        return $this->role;
    }

    public function setRole(?string $role): static
    {
        $this->role = $role;

        return $this;
    }

    public function getNumTel(): ?string
    {
        return $this->numTel;
    }

    public function setNumTel(?string $numTel): static
    {
        $this->numTel = $numTel;

        return $this;
    }

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(?string $adresse): static
    {
        $this->adresse = $adresse;

        return $this;
    }

    public function getPhoto(): ?string
    {
        return $this->photo;
    }

    public function setPhoto(?string $photo): static
    {
        $this->photo = $photo;

        return $this;
    }

    public function getEventPoints(): ?int
    {
        return $this->eventPoints;
    }

    public function setEventPoints(?int $eventPoints): static
    {
        $this->eventPoints = $eventPoints;

        return $this;
    }

    public function getFaceid(): ?string
    {
        return $this->faceid;
    }

    public function setFaceid(?string $faceid): static
    {
        $this->faceid = $faceid;

        return $this;
    }

    public function getFaceidTs(): ?\DateTimeInterface
    {
        return $this->faceidTs;
    }

    public function setFaceidTs(\DateTimeInterface $faceidTs): static
    {
        $this->faceidTs = $faceidTs;

        return $this;
    }

    /**
     * @return Collection<int, EventDetails>
     */
    public function getEventDetails(): Collection
    {
        return $this->eventDetails;
    }

    public function addEventDetail(EventDetails $eventDetail): static
    {
        if (!$this->eventDetails->contains($eventDetail)) {
            $this->eventDetails->add($eventDetail);
            $eventDetail->addUser($this);
        }

        return $this;
    }

    public function removeEventDetail(EventDetails $eventDetail): static
    {
        if ($this->eventDetails->removeElement($eventDetail)) {
            $eventDetail->removeUser($this);
        }

        return $this;
    }

}
