<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * BlackListed
 *
 * @ORM\Table(name="black_listed", indexes={@ORM\Index(name="id_user", columns={"id_user"})})
 * @ORM\Entity
 */
class BlackListed
{
    /**
     * @var \DateTime
     *
     * @ORM\Column(name="start_ban", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    private $startBan = 'CURRENT_TIMESTAMP';

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="end_ban", type="date", nullable=false, options={"default"="CURRENT_TIMESTAMP"})
     */
    private $endBan = 'CURRENT_TIMESTAMP';

    /**
     * @var \User
     *
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="NONE")
     * @ORM\OneToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="id_user", referencedColumnName="id")
     * })
     */
    private $idUser;


}
