<?php

namespace App\Repository;

use App\Entity\Commentaire;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Commentaire>
 *
 * @method Commentaire|null find($id, $lockMode = null, $lockVersion = null)
 * @method Commentaire|null findOneBy(array $criteria, array $orderBy = null)
 * @method Commentaire[]    findAll()
 * @method Commentaire[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class CommentaireRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commentaire::class);
    }

    public function getNbComnts($value) // khedma bl createQuerry fi 3ou4 createQuerryBuilder
    {
        $em = $this->getEntityManager();
        return $em->createQuery('select count(b) from App\Entity\Commentaire b where b.postId = :val')
        ->setParameter('val', $value)

        ->getSingleScalarResult(); //khater ma aandich list
    }

    // public function showCommentsByPost()
    // {
    //     return $this->createQueryBuilder('b')
    //     ->join('b.idPost', 'a')
    //     ->addSelect('a')
    //     // ->where('a.idPost = :val') 
    //     // ->andWhere('a.idPost = :val')
    //     // ->setParameter('val', $value)
    //     // ->from('YourMappingSpace:Campsite', 's')
    //     ->orderBy('a.idPost', 'DESC')
    //     ->getQuery()
    //     ->getResult();
    // }
//    /**
//     * @return Commentaire[] Returns an array of Commentaire objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('c.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Commentaire
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
