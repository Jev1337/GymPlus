<?php

namespace App\Entity;

use App\Repository\CommentaireRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommentaireRepository::class)]
class Commentaire
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_comment")]
    private ?int $id = null;

    #[ORM\Column(name: "user_id", nullable: true)]
    private ?int $userId = null;

    #[ORM\Column(name: "id_post", nullable: true)]
    private ?int $postId = null;

    #[ORM\Column(name: "content", length: 255, nullable: true)]
    private ?string $content = null;

    #[ORM\Column(name: "date", type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date = null;

    #[ORM\Column(name: "likes", nullable: true)]
    private ?int $likes = null;

    // #[ORM\ManyToOne(inversedBy: 'comnts')]
    // private ?Post $post = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getUserId(): ?int
    {
        return $this->userId;
    }

    public function setUserId(?int $userId): static
    {
        $this->userId = $userId;

        return $this;
    }

    public function getPostId(): ?int
    {
        return $this->postId;
    }

    public function setPostId(?int $postId): static
    {
        $this->postId = $postId;

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

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(?\DateTimeInterface $date): static
    {
        $this->date = $date;

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

    // public function getPost(): ?Post
    // {
    //     return $this->post;
    // }

    // public function setPost(?Post $post): static
    // {
    //     $this->post = $post;

    //     return $this;
    // }
}
