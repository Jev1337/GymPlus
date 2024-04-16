<?php

namespace App\Entity;

use App\Repository\EventParticipantsRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: EventParticipantsRepository::class)]
class EventParticipants
{
    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private ?int $event_details_id = null;

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private ?int $user_id = null;

    #[ORM\Column(type: "integer", nullable: true)]
    private ?int $rate = null;

    public function getEventDetailsId(): ?int
    {
        return $this->event_details_id;
    }

    public function setEventDetailsId(int $event_details_id): static
    {
        $this->event_details_id = $event_details_id;

        return $this;
    }

    public function getUserId(): ?int
    {
        return $this->user_id;
    }

    public function setUserId(int $user_id): static
    {
        $this->user_id = $user_id;

        return $this;
    }

    public function getRate(): ?int
    {
        return $this->rate;
    }

    public function setRate(?int $rate): static
    {
        $this->rate = $rate;

        return $this;
    }
 }