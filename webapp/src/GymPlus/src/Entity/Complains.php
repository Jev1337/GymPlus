<?php

namespace App\Entity;

use App\Repository\ComplainsRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ComplainsRepository::class)]
class Complains
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id')]
    private ?int $id = null;

    #[ORM\Column(name:"user_id")]
    private ?int $userId = null;

    #[ORM\Column(name:"post_id")]
    private ?int $postId = null;

    #[ORM\Column(length: 255, name:"feedback")]
    #[Assert\NotBlank(message: 'you can not add an empty feedback.')]
    private ?string $feedback = null;

    #[ORM\ManyToOne(inversedBy: 'complains')]
    private ?User $user = null;


    // #[ORM\ManyToOne(inversedBy: 'complains')]
    // private ?Post $post = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getUserId(): ?int
    {
        return $this->userId;
    }

    public function setUserId(): static
    {
        $this->userId = $this->user->getId();

        return $this;
    }

    public function getPostId(): ?int
    {
        return $this->postId;
    }

    public function setPostId(int $idpost): static
    {
        $this->postId = $idpost;

        return $this;
    }

    public function getFeedback(): ?string
    {
        return $this->feedback;
    }

    public function setFeedback(string $feedback): static
    {
        $this->feedback = $feedback;

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
