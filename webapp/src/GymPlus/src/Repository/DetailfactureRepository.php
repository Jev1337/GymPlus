<?php

namespace App\Repository;

use App\Entity\Detailfacture;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Detailfacture>
 *
 * @method Detailfacture|null find($id, $lockMode = null, $lockVersion = null)
 * @method Detailfacture|null findOneBy(array $criteria, array $orderBy = null)
 * @method Detailfacture[]    findAll()
 * @method Detailfacture[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class DetailfactureRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Detailfacture::class);
    }

    public function findDetailsByFactureId($factureId)
    {
        return $this->createQueryBuilder('d')
            ->andWhere('d.idfacture = :factureId')
            ->setParameter('factureId', $factureId)
            ->getQuery()
            ->getResult();
    }

    
    public function findMostSoldProducts(int $limit = 3): array
    {
        return $this->createQueryBuilder('d')
            ->select('p.idproduit AS idproduit', 'SUM(d.quantite) AS totalQuantite', 'p.photo' , 'p.name' , 'p.prix' ) // Sélectionnez également la colonne photo
            ->join('d.idproduit', 'p') // Joignez l'entité Produit avec l'entité Detailfacture
            ->groupBy('p.idproduit') // Groupez par l'identifiant du produit
            ->orderBy('totalQuantite', 'DESC') // Triez par la somme des quantités vendues
            ->setMaxResults($limit) // Limitez le nombre de résultats
            ->getQuery()
            ->getResult();
    }
   


//    /**
//     * @return Detailfacture[] Returns an array of Detailfacture objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('d')
//            ->andWhere('d.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('d.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Detailfacture
//    {
//        return $this->createQueryBuilder('d')
//            ->andWhere('d.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
