<?php

namespace App\Repository;

use App\Entity\EquipementsDetails;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<EquipementsDetails>
 *
 * @method EquipementsDetails|null find($id, $lockMode = null, $lockVersion = null)
 * @method EquipementsDetails|null findOneBy(array $criteria, array $orderBy = null)
 * @method EquipementsDetails[]    findAll()
 * @method EquipementsDetails[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class EquipementsDetailsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, EquipementsDetails::class);
    }

    public function getEquipementsDetails(): array
    {
        return $this->createQueryBuilder('e')
            ->getQuery()
            ->getResult();
    }

    public function getEquipementsDetailsById(int $id): ?EquipementsDetails
    {
        return $this->createQueryBuilder('e')
            ->andWhere('e.id = :id')
            ->setParameter('id', $id)
            ->getQuery()
            ->getOneOrNullResult();
    }

//    /**
//     * @return EquipementsDetails[] Returns an array of EquipementsDetails objects
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

//    public function findOneBySomeField($value): ?EquipementsDetails
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
