<?php

namespace App\Entity;

use App\Repository\PostRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\HttpFoundation\File\File;
use Vich\UploaderBundle\Mapping\Annotation as Vich;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: PostRepository::class)]
#[Vich\Uploadable]
class Post
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_post ")]
    private ?int $id = null;

    #[ORM\Column(name: "user_id", nullable: true)]
    private ?int $idUser = null;

    #[ORM\Column(name: "mode", length: 255, nullable: true)]
    private ?string $mode = null;

    #[ORM\Column(name: "content", length: 255, nullable: true)]
    #[Assert\NotBlank(message: 'Please enter your lastname.')]
    private ?string $content = null;

    #[ORM\Column(name: "date", type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date = null;

    #[ORM\Column(name: "photo", length: 255, nullable: true)]
    private ?string $photo = null;

    #[ORM\Column(name: "likes", nullable: true)]
    private ?int $likes = null;

    #[ORM\Column(name: "nbComnts", nullable: true)]
    private ?int $nbComnts = null;

    #[Vich\UploadableField(mapping: 'img_post', fileNameProperty: 'photo')]
    private ?File $imageFile = null;

    // #[ORM\Column(nullable: true)]
    // private ?string $imageName = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getIdUser(): ?int
    {
        return $this->idUser;
    }

    public function setIdUser(?int $idUser): static
    {
        $this->idUser = $idUser;

        return $this;
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

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(?\DateTimeInterface $date): static
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
     /**
     * If manually uploading a file (i.e. not using Symfony Form) ensure an instance
     * of 'UploadedFile' is injected into this setter to trigger the update. If this
     * bundle's configuration parameter 'inject_on_load' is set to 'true' this setter
     * must be able to accept an instance of 'File' as the bundle will inject one here
     * during Doctrine hydration.
     *
     * @param File|\Symfony\Component\HttpFoundation\File\UploadedFile|null $imageFile
     */
    public function setImageFile(?File $imageFile = null): void
    {
        $this->imageFile = $imageFile;

    }

    public function getImageFile(): ?File
    {
        return $this->imageFile;
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

    public function getNbComnts(): ?int
    {
        return $this->nbComnts;
    }

    public function setNbComnts(?int $nbComnts): static
    {
        $this->nbComnts = $nbComnts;

        return $this;
    }
}
