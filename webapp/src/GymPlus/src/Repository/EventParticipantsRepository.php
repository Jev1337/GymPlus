<?php

namespace App\Repository;

use App\Entity\EventParticipants;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<EventParticipants>
 *
 * @method EventParticipants|null find($id, $lockMode = null, $lockVersion = null)
 * @method EventParticipants|null findOneBy(array $criteria, array $orderBy = null)
 * @method EventParticipants[]    findAll()
 * @method EventParticipants[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class EventParticipantsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, EventParticipants::class);
    }
    public function isUserParticipant(int $userId, int $eventDetailsId): bool
    {
        $participant = $this->findOneBy([
            'user_id' => $userId,
            'event_details_id' => $eventDetailsId,
        ]);

        return $participant !== null;
    }
    
    
    
   
public function getNextEventDate($userId)
{
    $conn = $this->getEntityManager()->getConnection();

    $sql = '
        SELECT ed.event_date as date 
        FROM event_participants ep
        INNER JOIN event_details ed ON ep.event_details_id = ed.id
        WHERE ep.user_id = :userId
        ORDER BY ed.event_date ASC
        LIMIT 1
    ';

    $stmt = $conn->executeQuery($sql, ['userId' => $userId]);

    // Fetch the result as an associative array
    $result = $stmt->fetchAssociative();

    return $result ? new \DateTime($result['date']) : null;
}


public function findPastEventsByUser($userId)
{
    $conn = $this->getEntityManager()->getConnection();

    $sql = '
        SELECT ed.* 
        FROM event_participants ep
        INNER JOIN event_details ed ON ep.event_details_id = ed.id
        WHERE ep.user_id = :userId AND ed.event_date < :currentDate
    ';

    $stmt = $conn->executeQuery($sql, ['userId' => $userId, 'currentDate' => (new \DateTime())->format('Y-m-d H:i:s')]);

    // Fetch the results as an associative array
    $results = $stmt->fetchAllAssociative();

    return $results;
}




    
    
    


//    /**
//     * @return EventParticipants[] Returns an array of EventParticipants objects
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

//    public function findOneBySomeField($value): ?EventParticipants
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
