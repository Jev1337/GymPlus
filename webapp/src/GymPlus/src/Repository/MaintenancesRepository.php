<?php

namespace App\Repository;

use App\Entity\Maintenances;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Maintenances>
 *
 * @method Maintenances|null find($id, $lockMode = null, $lockVersion = null)
 * @method Maintenances|null findOneBy(array $criteria, array $orderBy = null)
 * @method Maintenances[]    findAll()
 * @method Maintenances[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class MaintenancesRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Maintenances::class);
    }

    public function getMaintenances(): array
    {
        return $this->createQueryBuilder('m')
            ->getQuery()
            ->getResult();
    }

    public function getMaintenancesById(int $id): ?Maintenances
    {
        return $this->createQueryBuilder('m')
            ->andWhere('m.id = :id')
            ->setParameter('id', $id)
            ->getQuery()
            ->getOneOrNullResult();
    }

    public function getEachMonthMaintenancesCount(): array
    {
        $maintenances = $this->createQueryBuilder('m')
            ->getQuery()
            ->getResult();
        $months = [
            'Jan' => 0,
            'Feb' => 0,
            'Mar' => 0,
            'Apr' => 0,
            'May' => 0,
            'Jun' => 0,
            'Jul' => 0,
            'Aug' => 0,
            'Sep' => 0,
            'Oct' => 0,
            'Nov' => 0,
            'Dec' => 0,
        ];
        foreach ($maintenances as $maintenance) {
            $month = date('M', strtotime($maintenance->getDateMaintenance()->format('Y-m-d')));
            $months[$month] += 1;
        }
        return $months;
    }

    
//    /**
//     * @return Maintenances[] Returns an array of Maintenances objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('m')
//            ->andWhere('m.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('m.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Maintenances
//    {
//        return $this->createQueryBuilder('m')
//            ->andWhere('m.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
