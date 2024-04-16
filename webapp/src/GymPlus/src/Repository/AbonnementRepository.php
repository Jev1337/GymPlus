<?php

namespace App\Repository;

use App\Entity\Abonnement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Abonnement>
 *
 * @method Abonnement|null find($id, $lockMode = null, $lockVersion = null)
 * @method Abonnement|null findOneBy(array $criteria, array $orderBy = null)
 * @method Abonnement[]    findAll()
 * @method Abonnement[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class AbonnementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Abonnement::class);
    }

    public function isUserSubscribed($userId): bool
    {
        $qb = $this->createQueryBuilder('a');
        $qb->select('COUNT(a.id)');
        $qb->where('a.user = :userId');
        $qb->setParameter('userId', $userId);
        $qb->andWhere('a.datefinab > :now');
        $qb->setParameter('now', new \DateTime());
        $count = $qb->getQuery()->getSingleScalarResult();
        return $count > 0;
    }

    public function getOldSubscriptionsByUserId($userId): array
    {
        $qb = $this->createQueryBuilder('a');
        $qb->where('a.user = :userId');
        $qb->setParameter('userId', $userId);
        $qb->andWhere('a.datefinab < :now');
        $qb->setParameter('now', new \DateTime());
        return $qb->getQuery()->getResult();
    }

    public function getCurrentSubByUserId($userId): ?Abonnement
    {
        $qb = $this->createQueryBuilder('a');
        $qb->where('a.user = :userId');
        $qb->setParameter('userId', $userId);
        $qb->andWhere('a.datefinab > :now');
        $qb->setParameter('now', new \DateTime());
        return $qb->getQuery()->getOneOrNullResult();
    }


//    /**
//     * @return Abonnement[] Returns an array of Abonnement objects
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

//    public function findOneBySomeField($value): ?Abonnement
//    {
//        return $this->createQueryBuilder('a')
//            ->andWhere('a.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
