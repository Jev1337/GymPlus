<?php

namespace App\Repository;

use App\Entity\EventDetails;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<EventDetails>
 *
 * @method EventDetails|null find($id, $lockMode = null, $lockVersion = null)
 * @method EventDetails|null findOneBy(array $criteria, array $orderBy = null)
 * @method EventDetails[]    findAll()
 * @method EventDetails[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class EventDetailsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, EventDetails::class);
    }

    public function findFutureEvents(): array
{
    $currentDate = new \DateTime();

    return $this->createQueryBuilder('e')
        ->where('e.event_date > :currentDate')
        ->setParameter('currentDate', $currentDate)
        ->getQuery()
        ->getResult();
}
    

//    /**
//     * @return EventDetails[] Returns an array of EventDetails objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('e.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?EventDetails
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
