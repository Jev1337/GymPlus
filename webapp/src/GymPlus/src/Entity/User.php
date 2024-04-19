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
    #[Assert\NotBlank(message: 'Please enter your CIN.', groups: ['create'])]
    #[Assert\Length(min: 8, max: 8, minMessage: 'CIN must be 8 characters long.', maxMessage: 'CIN must be 8 characters long.', groups: ['create'])]
    #[Assert\Regex(pattern: '/^[0-9]*$/', message: 'CIN must contain only numbers.', groups: ['create'])]
    private ?int $id;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your username.', groups: ['create', 'update'])]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Username must be at least 3 characters long.', maxMessage: 'Username must be at most 20 characters long.', groups: ['create', 'update'])]
    #[Assert\Regex(pattern: '/^[a-zA-Z0-9]*$/', message: 'Username must contain only letters and numbers.', groups: ['create', 'update'])]
    private ?string $username;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your firstname.', groups: ['create', 'update'])]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Firstname must be at least 3 characters long.', maxMessage: 'Firstname must be at most 20 characters long.', groups: ['create', 'update'])]
    #[Assert\Regex(pattern: '/^[a-zA-Z ]*$/', message: 'Firstname must contain only letters.', groups: ['create', 'update'])]
    private ?string $firstname;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your lastname.', groups: ['create', 'update'])]
    #[Assert\Length(min: 3, max: 20, minMessage: 'Lastname must be at least 3 characters long.', maxMessage: 'Lastname must be at most 20 characters long.', groups: ['create', 'update'])]
    #[Assert\Regex(pattern: '/^[a-zA-Z ]*$/', message: 'Lastname must contain only letters.', groups: ['create', 'update'])]
    private ?string $lastname;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your date of birth.', groups: ['create', 'update'])]
    #[Assert\LessThan('today', message: 'Date of birth must be in the past.', groups: ['create', 'update'])]
    #[Assert\GreaterThanOrEqual('-100 years', message: 'Date of birth must be at most 100 years ago.', groups: ['create', 'update'])]
    private ?\DateTime $dateNaiss;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your password.', groups: ['create', 'login'])]
    #[Assert\Length(min: 8, max: 20, minMessage: 'Password must be at least 8 characters long.', maxMessage: 'Password must be at most 20 characters long.', groups: ['create'])]
    private ?string $password;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your email.', groups: ['create', 'login'])]
    #[Assert\Email(message: 'Please enter a valid email address.', groups: ['create', 'login'])]
    #[Assert\Length(min: 3, max: 50, minMessage: 'Email must be at least 3 characters long.', maxMessage: 'Email must be at most 50 characters long.', groups: ['create', 'login'])]
    private ?string $email;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your role.', groups: ['admincreate'])]
    #[Assert\Choice(choices: ['admin', 'client', 'staff'], message: 'Role must be either admin or user.', groups: ['admincreate'])]
    private ?string $role;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please enter your phone number.', groups: ['create', 'update'])]
    #[Assert\Length(min: 8, max: 8, minMessage: 'Phone number must be 8 characters long.', maxMessage: 'Phone number must be 8 characters long.', groups: ['create', 'update'])]
    #[Assert\Regex(pattern: '/^[0-9]*$/', message: 'Phone number must contain only numbers.', groups: ['create', 'update'])]
    private ?string $numTel;

    #[ORM\Column]
    private ?string $adresse;

    #[ORM\Column]
    #[Assert\NotBlank(message: 'Please upload your photo.', groups: ['create'])]
    #[Assert\File(mimeTypes: ['image/jpeg', 'image/png'], mimeTypesMessage: 'Please upload a valid image file.', groups: ['create'])]
    #[Assert\Image(maxSize: '2M', maxSizeMessage: 'Please upload an image file that is less than 2MB.', groups: ['create'])]
    private ?string $photo;

    #[ORM\Column]
    private ?int $eventPoints;

    #[ORM\Column]
    private ?string $faceid;

    #[ORM\Column]
    private ?\DateTime $faceidTs;

    #[ORM\OneToMany(mappedBy: 'user', targetEntity: Post::class, cascade:["remove", "persist", "merge"], orphanRemoval: true)]
    private Collection $posts;
    #[ORM\OneToMany(mappedBy: 'user', targetEntity: Commentaire::class, cascade:["remove", "persist", "merge"], orphanRemoval: true)]
    private Collection $comments;
    #[ORM\OneToMany(mappedBy: 'user', targetEntity: ParticipantMessanger::class, cascade:["remove", "persist", "merge"], orphanRemoval: true)]
    private Collection $participants;
    #[ORM\OneToMany(mappedBy: 'user', targetEntity: Message::class, cascade:["remove", "persist", "merge"], orphanRemoval: true)]
    private Collection $messages;

    
    public function __construct()
    {
        $this->eventDetails = new \Doctrine\Common\Collections\ArrayCollection();
        $this->posts = new ArrayCollection();
        $this->comments = new ArrayCollection();
        $this->participants = new ArrayCollection();
        $this->messages = new ArrayCollection();
    }

    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    public function getRoles(): array
    {

        $roles = ["ROLE_".strtoupper($this->role)];

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


    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    public function getSalt(): ?string
    {
        return null;
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

    /**
     * @return Collection<int, Post>
     */
    public function getPosts(): Collection
    {
        return $this->posts;
    }

    public function addPost(Post $post): static
    {
        if (!$this->posts->contains($post)) {
            $this->posts->add($post);
            $post->setUser($this);
        }

        return $this;
    }

    public function removePost(Post $post): static
    {
        if ($this->posts->removeElement($post)) {
            // set the owning side to null (unless already changed)
            if ($post->getUser() === $this) {
                $post->setUser(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Commentaire>
     */
    public function getComments(): Collection
    {
        return $this->comments;
    }

    public function addComment(Commentaire $comment): static
    {
        if (!$this->comments->contains($comment)) {
            $this->comments->add($comment);
            $comment->setUser($this);
        }

        return $this;
    }

    public function removeComment(Commentaire $comment): static
    {
        if ($this->comments->removeElement($comment)) {
            // set the owning side to null (unless already changed)
            if ($comment->getUser() === $this) {
                $comment->setUser(null);
            }
        }

        return $this;
    }
    /**
     * @return Collection<int, ParticipantMessanger>
     */
    public function getParticipants(): Collection
    {
        return $this->participants;
    }

    public function addParticipant(ParticipantMessanger $participant): static
    {
        if (!$this->participants->contains($participant)) {
            $this->participants->add($participant);
            $participant->setUser($this);
        }

        return $this;
    }

    public function removeParticipant(ParticipantMessanger $participant): static
    {
        if ($this->participants->removeElement($participant)) {
            // set the owning side to null (unless already changed)
            if ($participant->getUser() === $this) {
                $participant->setUser(null);
            }
        }

        return $this;
    }
    /**
     * @return Collection<int, Message>
     */
    public function getMessages(): Collection
    {
        return $this->messages;
    }

    public function addMessage(Message $message): static
    {
        if (!$this->messages->contains($message)) {
            $this->messages->add($message);
            $message->setUser($this);
        }

        return $this;
    }

    public function removeMessage(Message $message): static
    {
        if ($this->messages->removeElement($message)) {
            // set the owning side to null (unless already changed)
            if ($message->getUser() === $this) {
                $message->setUser(null);
            }
        }

        return $this;
    }

}