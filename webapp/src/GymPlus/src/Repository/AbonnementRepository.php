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

    public function getActiveMembershipCount(){
        $qb = $this->createQueryBuilder('a');
        $qb->select('COUNT(a.id)');
        $qb->andWhere('a.datefinab > :now');
        $qb->setParameter('now', new \DateTime());
        return $qb->getQuery()->getSingleScalarResult();
    }

    public function getActiveMembershipPercent(){
        $qb = $this->createQueryBuilder('a');
        $qb->select('COUNT(a.id)');
        $qb->andWhere('a.datefinab > :now');
        $qb->setParameter('now', new \DateTime());
        $active = $qb->getQuery()->getSingleScalarResult();
        return $active;
    
    }

    public function getArrayGPCount(){
 
        
        $qb = $this->createQueryBuilder('a');
        $qb->select('t.name, COUNT(a.id) as count') // Assuming that the related entity has a field named 'name'
           ->join('a.type', 't') // Join the related entity
           ->andWhere('a.datefinab > :now')
           ->setParameter('now', new \DateTime())
           ->andWhere('t.name IN (:types)') // Use the joined entity in the WHERE clause
           ->setParameter('types', ['GP 1', 'GP 2', 'GP 3'])
           ->groupBy('t.name'); // Group by a field of the joined entity
        
        $results = $qb->getQuery()->getResult();
        
        $arr = [
            'GP 1' => 0,
            'GP 2' => 0,
            'GP 3' => 0,
        ];
        
        foreach ($results as $result) {
            $arr[$result['name']] = $result['count']; // Use the correct field name here
        }
        
        return $arr;
        

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
