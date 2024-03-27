<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\UserRepository;

#[ORM\Entity(repositoryClass: UserRepository::class)]
class User
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;


    #[ORM\Column]
    private ?string $username;

    #[ORM\Column]
    private ?string $firstname;

    #[ORM\Column]
    private ?string $lastname;

    #[ORM\Column]
    private ?\DateTimeInterface $dateNaiss;

    #[ORM\Column]
    private ?string $password;

    #[ORM\Column]
    private ?string $email;

    #[ORM\Column]
    private ?string $role;

    #[ORM\Column]
    private ?string $numTel;

    #[ORM\Column]
    private ?string $adresse;

    #[ORM\Column]
    private ?string $photo;

    #[ORM\Column]
    private ?int $eventPoints;

    #[ORM\Column]
    private ?string $faceid;

    #[ORM\Column]
    private ?\DateTimeInterface $faceidTs;


    #[ORM\ManyToMany(targetEntity: EventDetails::class, inversedBy: "user")]
    private Collection $eventDetails;

    
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
