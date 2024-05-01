<?php

namespace App\Repository;

use App\Entity\Notif;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Notif>
 *
 * @method Notif|null find($id, $lockMode = null, $lockVersion = null)
 * @method Notif|null findOneBy(array $criteria, array $orderBy = null)
 * @method Notif[]    findAll()
 * @method Notif[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class NotifRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Notif::class);
    }

    public function findDetailsWithCondition(): array
    {
        return $this->createQueryBuilder('n')
            ->andWhere('n.etatCommentaire = :etatCommentaire')
            ->setParameter('etatCommentaire', 'yes')
            ->getQuery()
            ->getResult();
    }

//    /**
//     * @return Notif[] Returns an array of Notif objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('n')
//            ->andWhere('n.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('n.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Notif
//    {
//        return $this->createQueryBuilder('n')
//            ->andWhere('n.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
