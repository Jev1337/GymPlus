<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\EventDetailsRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EventDetailsRepository::class)]
class EventDetails
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    /**
     * @Assert\Length(
     *      min = 4,
     *      minMessage = "The name must be at least {{ limit }} characters long"
     * )
     */
    #[ORM\Column]
    private ?string $name;

    /**
     * @Assert\Length(
     *      min = 4,
     *      minMessage = "The type must be at least {{ limit }} characters long"
     * )
     * @Assert\Regex(
     *     pattern="/^[a-zA-Z]*$/",
     *     message="The type should contain only letters"
     * )
     */
    #[ORM\Column]
    private ?string $type;

    /**
     * @Assert\GreaterThan(
     *     "now",
     *     message="The event date should be at least 1 hour from now"
     * )
     */
    #[ORM\Column]
    private ?\DateTime $eventDate;

    /**
     * @Assert\Range(
     *      min = 20,
     *      max = 120,
     *      notInRangeMessage = "The duration must be between {{ min }} and {{ max }}",
     * )
     */
    #[ORM\Column]
    private ?string $duree;

    /**
     * @Assert\Range(
     *      min = 10,
     *      max = 50,
     *      notInRangeMessage = "The number of places must be between {{ min }} and {{ max }}",
     * )
     */
    #[ORM\Column]
    private ?int $nbPlaces;

    #[ORM\Column]
    private ?int $nbTotal;

    public function __construct()
    {
        $this->user = new ArrayCollection();
    }

    


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(?string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getType(): ?string
    {
        return $this->type;
    }

    public function setType(?string $type): static
    {
        $this->type = $type;

        return $this;
    }

    public function getEventDate(): ?\DateTime
    {
        return $this->eventDate;
    }

    public function setEventDate(?\DateTime $eventDate): static
    {
        $this->eventDate = $eventDate;

        return $this;
    }

    public function getDuree(): ?string
    {
        return $this->duree;
    }

    public function setDuree(?string $duree): static
    {
        $this->duree = $duree;

        return $this;
    }

    public function getNbPlaces(): ?int
    {
        return $this->nbPlaces;
    }

    public function setNbPlaces(?int $nbPlaces): static
    {
        $this->nbPlaces = $nbPlaces;

        return $this;
    }

    public function getNbTotal(): ?int
    {
        return $this->nbTotal;
    }

    public function setNbTotal(?int $nbTotal): static
    {
        $this->nbTotal = $nbTotal;

        return $this;
    }

    public function getUser(): Collection
    {
        return $this->user;
    }

    public function addUser(User $user): static
    {
        if (!$this->user->contains($user)) {
            $this->user->add($user);
        }

        return $this;
    }

    public function removeUser(User $user): static
    {
        $this->user->removeElement($user);

        return $this;
    }

}
