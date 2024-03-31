<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\UserRepository;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;

#[ORM\Entity(repositoryClass: UserRepository::class)]
class User Implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your CIN.')]
    #[Assert\Length(min: 8, max: 8, minMessage: 'CIN must be 8 characters long.', maxMessage: 'CIN must be 8 characters long.')]
    #[Assert\Regex(pattern: '/^[0-9]*$/', message: 'CIN must contain only numbers.')]
    private ?int $id;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your username.')]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Username must be at least 3 characters long.', maxMessage: 'Username must be at most 20 characters long.')]
    #[Assert\Regex(pattern: '/^[a-zA-Z0-9]*$/', message: 'Username must contain only letters and numbers.')]
    private ?string $username;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your firstname.')]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Firstname must be at least 3 characters long.', maxMessage: 'Firstname must be at most 20 characters long.')]
    #[Assert\Regex(pattern: '/^[a-zA-Z]*$/', message: 'Firstname must contain only letters.')]
    private ?string $firstname;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your lastname.')]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Lastname must be at least 3 characters long.', maxMessage: 'Lastname must be at most 20 characters long.')]
    #[Assert\Regex(pattern: '/^[a-zA-Z]*$/', message: 'Lastname must contain only letters.')]
    private ?string $lastname;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your date of birth.')]
    #[Assert\LessThan('today', message: 'Date of birth must be in the past.')]
    #[Assert\GreaterThanOrEqual('-100 years', message: 'Date of birth must be at most 100 years ago.')]
    private ?\DateTime $dateNaiss;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your password.')]
    #[Assert\Length(min: 8, max: 20, minMessage: 'Password must be at least 8 characters long.', maxMessage: 'Password must be at most 20 characters long.', groups: ['create'])]
    private ?string $password;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your email.')]
    #[Assert\Email(message: 'Please enter a valid email address.')]
    #[Assert\Length(min: 3, max: 50, minMessage: 'Email must be at least 3 characters long.', maxMessage: 'Email must be at most 50 characters long.')]
    private ?string $email;

    #[ORM\Column]
    private ?string $role;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your phone number.')]
    #[Assert\Length(min: 8, max: 8, minMessage: 'Phone number must be 8 characters long.', maxMessage: 'Phone number must be 8 characters long.')]
    #[Assert\Regex(pattern: '/^[0-9]*$/', message: 'Phone number must contain only numbers.')]
    private ?string $numTel;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your address.')]
    #[Assert\Length(min: 3, max: 50, minMessage: 'Address must be at least 3 characters long.', maxMessage: 'Address must be at most 50 characters long.')]
    #[Assert\Regex(pattern: '/^[a-zA-Z0-9]*$/', message: 'Address must contain only letters and numbers.')]
    private ?string $adresse;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please upload your photo.')]
    #[Assert\File(mimeTypes: ['image/jpeg', 'image/png'], mimeTypesMessage: 'Please upload a valid image file.', groups: ['create'])]
    #[Assert\Image(maxSize: '2M', maxSizeMessage: 'Please upload an image file that is less than 2MB.', groups: ['create'])]
    private ?string $photo;

    #[ORM\Column]
    private ?int $eventPoints;

    #[ORM\Column]
    private ?string $faceid;

    #[ORM\Column]
    private ?\DateTime $faceidTs;


    #[ORM\ManyToMany(targetEntity: EventDetails::class, inversedBy: "user")]
    private Collection $eventDetails;

    
    public function __construct()
    {
        $this->eventDetails = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * The public representation of the user (e.g. a username, an email address, etc.)
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        $roles = [$this->role];
        return array_unique($roles);
    }

    public function setId(int $id): static
    {
        $this->id = $id;
        return $this;
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

    public function getDateNaiss(): ?\DateTime
    {
        return $this->dateNaiss;
    }

    public function setDateNaiss(?\DateTime $dateNaiss): static
    {
        $this->dateNaiss = $dateNaiss;

        return $this;
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): ?string
    {
        return $this->password;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    public function getSalt()
    {
        // not needed when using the "bcrypt" algorithm in security.yaml
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

    public function getFaceidTs(): ?\DateTime
    {
        return $this->faceidTs;
    }

    public function setFaceidTs(\DateTime $faceidTs): static
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
