<?php

namespace App\Entity;

use App\Repository\PostRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\HttpFoundation\File\File;
use Vich\UploaderBundle\Mapping\Annotation as Vich;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: PostRepository::class)]
// #[Vich\Uploadable]
class Post
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_post")]
    private ?int $id = null;

    #[ORM\Column(name: "user_id", nullable: true)]
    private ?int $idUser = null;

    #[ORM\Column(name: "mode", length: 255, nullable: true)]
    private ?string $mode = null;

    #[ORM\Column(name: "content", length: 255, nullable: true)]
    #[Assert\NotBlank(message: 'you can not add an empty post.')]    
    private ?string $content = null;

    #[ORM\Column(name: "date", type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date = null;

    #[Assert\File(mimeTypes: ['image/jpeg', 'image/png'], mimeTypesMessage: 'Please upload a valid image file.', groups: ['create'])]
    #[Assert\Image(maxSize: '2M', maxSizeMessage: 'Please upload an image file that is less than 2MB.', groups: ['create'])]
    #[ORM\Column(name: "photo", length: 255, nullable: true)]
    private ?string $photo = null;

    #[ORM\Column(name: "likes", nullable: true)]
    private ?int $likes = null;

    #[ORM\Column(name: "nbComnts", nullable: true)]
    private ?int $nbComnts = null;
    // #[Assert\NotBlank(message: 'you can not add an empty post.')]
    // #[Vich\UploadableField(mapping: 'img_post', fileNameProperty: 'photo')]
    // private ?File $imageFile = null;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: 'posts')]
    private ?User $user = null;

    // #[ORM\OneToMany(mappedBy: 'post', targetEntity: Complains::class)]
    // private Collection $complains;

    // public function __construct()
    // {
    //     $this->complains = new ArrayCollection();
    // }

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

    public function setIdUser(): static
    {
        $this->idUser = $this->user->getId();

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
    //  /**
    //  * If manually uploading a file (i.e. not using Symfony Form) ensure an instance
    //  * of 'UploadedFile' is injected into this setter to trigger the update. If this
    //  * bundle's configuration parameter 'inject_on_load' is set to 'true' this setter
    //  * must be able to accept an instance of 'File' as the bundle will inject one here
    //  * during Doctrine hydration.
    //  *
    //  * @param File|\Symfony\Component\HttpFoundation\File\UploadedFile|null $imageFile
    //  */
    // public function setImageFile(?File $imageFile = null): void
    // {
    //     $this->imageFile = $imageFile;

    // }

    // public function getImageFile(): ?File
    // {
    //     return $this->imageFile;
    // }

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

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }

    // /**
    //  * @return Collection<int, Complains>
    //  */
    // public function getComplains(): Collection
    // {
    //     return $this->complains;
    // }

    // public function addComplain(Complains $complain): static
    // {
    //     if (!$this->complains->contains($complain)) {
    //         $this->complains->add($complain);
    //         $complain->setPost($this);
    //     }

    //     return $this;
    // }

    // public function removeComplain(Complains $complain): static
    // {
    //     if ($this->complains->removeElement($complain)) {
    //         // set the owning side to null (unless already changed)
    //         if ($complain->getPost() === $this) {
    //             $complain->setPost(null);
    //         }
    //     }

    //     return $this;
    // }
}
