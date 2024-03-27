<?php

namespace App\Repository;

use App\Entity\AbonnementDetails;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<AbonnementDetails>
 *
 * @method AbonnementDetails|null find($id, $lockMode = null, $lockVersion = null)
 * @method AbonnementDetails|null findOneBy(array $criteria, array $orderBy = null)
 * @method AbonnementDetails[]    findAll()
 * @method AbonnementDetails[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class AbonnementDetailsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, AbonnementDetails::class);
    }

//    /**
//     * @return AbonnementDetails[] Returns an array of AbonnementDetails objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('a')
//            ->andWhere('a.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('a.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?AbonnementDetails
//    {
//        return $this->createQueryBuilder('a')
//            ->andWhere('a.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
