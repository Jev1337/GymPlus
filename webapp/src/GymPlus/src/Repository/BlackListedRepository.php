<?php

namespace App\Repository;

use App\Entity\BlackListed;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<BlackListed>
 *
 * @method BlackListed|null find($id, $lockMode = null, $lockVersion = null)
 * @method BlackListed|null findOneBy(array $criteria, array $orderBy = null)
 * @method BlackListed[]    findAll()
 * @method BlackListed[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class BlackListedRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, BlackListed::class);
    }

//    /**
//     * @return BlackListed[] Returns an array of BlackListed objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('b')
//            ->andWhere('b.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('b.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?BlackListed
//    {
//        return $this->createQueryBuilder('b')
//            ->andWhere('b.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
