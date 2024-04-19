<?php

namespace App\Repository;

use App\Entity\ParticipantMessanger;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<ParticipantMessanger>
 *
 * @method ParticipantMessanger|null find($id, $lockMode = null, $lockVersion = null)
 * @method ParticipantMessanger|null findOneBy(array $criteria, array $orderBy = null)
 * @method ParticipantMessanger[]    findAll()
 * @method ParticipantMessanger[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ParticipantRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, ParticipantMessanger::class);
    }

//    /**
//     * @return Participant[] Returns an array of Participant objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('p.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Participant
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
    public function findParticipantByConversationIdAndUserId ( int $convoId, int $userId){
        $qb = $this->createQueryBuilder('p');

        $qb->
            where(
                $qb->expr()->andX(
                    $qb->expr()->eq('p.conversation', ':$convoId'),
                    $qb->expr()->neq('p.user', ':$userId'),
                )
            )
            ->setParameters(
                [
                    'userId'=> $userId, 
                    'convvId'=> $convoId
                ]
            )
        ;
            
        return $qb->getQuery()->getOneOrNullResult();
    }

}
