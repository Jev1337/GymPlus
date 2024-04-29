<?php

namespace App\Repository;

use App\Entity\Complains;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Complains>
 *
 * @method Complains|null find($id, $lockMode = null, $lockVersion = null)
 * @method Complains|null findOneBy(array $criteria, array $orderBy = null)
 * @method Complains[]    findAll()
 * @method Complains[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ComplainsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Complains::class);
    }

//    /**
//     * @return Complains[] Returns an array of Complains objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('c.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Complains
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
