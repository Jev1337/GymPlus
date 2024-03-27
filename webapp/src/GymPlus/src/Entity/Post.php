<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\PostRepository;

#[ORM\Entity(repositoryClass: PostRepository::class)]
class Post
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $idPost;

    #[ORM\Column]
    private ?string $mode;

    #[ORM\Column]
    private ?string $content;

    #[ORM\Column]
    private ?\DateTime $date;

    #[ORM\Column]
    private ?string $photo;

    #[ORM\Column]
    private ?int $likes;

    #[ORM\Column]
    private ?int $nbcomnts;

    #[ORM\ManyToOne(targetEntity: User::class)]
    private ?user $user;

    public function getIdPost(): ?int
    {
        return $this->idPost;
    }

    public function getMode(): ?string
    {
        return $this->mode;
    }

    public function setMode(?string $mode): static
    {
        $this->mode = $mode;

        return $this;
    }

    public function getContent(): ?string
    {
        return $this->content;
    }

    public function setContent(?string $content): static
    {
        $this->content = $content;

        return $this;
    }

    public function getDate(): ?\DateTime
    {
        return $this->date;
    }

    public function setDate(?\DateTime $date): static
    {
        $this->date = $date;

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

    public function getLikes(): ?int
    {
        return $this->likes;
    }

    public function setLikes(?int $likes): static
    {
        $this->likes = $likes;

        return $this;
    }

    public function getNbcomnts(): ?int
    {
        return $this->nbcomnts;
    }

    public function setNbcomnts(?int $nbcomnts): static
    {
        $this->nbcomnts = $nbcomnts;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }


}
