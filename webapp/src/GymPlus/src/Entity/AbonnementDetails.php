<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * AbonnementDetails
 *
 * @ORM\Table(name="abonnement_details")
 * @ORM\Entity
 */
class AbonnementDetails
{
    /**
     * @var string
     *
     * @ORM\Column(name="name", type="string", length=255, nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $name;

    /**
     * @var float
     *
     * @ORM\Column(name="prix", type="float", precision=10, scale=0, nullable=false)
     */
    private $prix;


}
