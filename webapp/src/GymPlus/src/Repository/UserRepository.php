<?php

namespace App\Repository;

use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<User>
 *
 * @method User|null find($id, $lockMode = null, $lockVersion = null)
 * @method User|null findOneBy(array $criteria, array $orderBy = null)
 * @method User[]    findAll()
 * @method User[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class UserRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, User::class);
    }
    public function showAllUsers()
    {
        return $this->createQueryBuilder('u')
            ->getQuery()
            ->getResult();
    }
    public function findUserById($id)
    {
        return $this->createQueryBuilder('u')
            ->where('u.id = :id')
            ->setParameter('id', $id)
            ->getQuery()
            ->getOneOrNullResult();
    }
    public function findUserByEmail($email)
    {
        return $this->createQueryBuilder('u')
            ->where('u.email = :email')
            ->setParameter('email', $email)
            ->getQuery()
            ->getOneOrNullResult();
    }
    public function findUserByRole($role)
    {
        return $this->createQueryBuilder('u')
            ->where('u.roles LIKE :role')
            ->setParameter('role', '%'.$role.'%')
            ->getQuery()
            ->getResult();
    }
    public function findUserByUsername($username)
    {
        return $this->createQueryBuilder('u')
            ->where('u.username = :username')
            ->setParameter('username', $username)
            ->getQuery()
            ->getOneOrNullResult();
    }
    public function findUserByPhone($num_tel)
    {
        return $this->createQueryBuilder('u')
            ->where('u.numTel = :numTel')
            ->setParameter('numTel', $num_tel)
            ->getQuery()
            ->getOneOrNullResult();
    }
    public function getClientList(){
        return $this->createQueryBuilder('u')
            ->where('u.role LIKE :role')
            ->setParameter('role', 'client')
            ->getQuery()
            ->getResult();
    
    }
    public function getAdminList(){
        return $this->createQueryBuilder('u')
            ->where('u.role LIKE :role')
            ->setParameter('role', 'admin')
            ->getQuery()
            ->getResult();
    
    
    }

    public function getUserCount(){
        return $this->createQueryBuilder('u')
            ->select('count(u.id)')
            ->getQuery()
            ->getSingleScalarResult();
    }

    public function getUserList($filter){
        $qb = $this->createQueryBuilder('u');
        if ($filter != null) {
            $qb->where('u.username LIKE :filter')
                ->orWhere('u.email LIKE :filter')
                ->orWhere('u.numTel LIKE :filter')
                ->orWhere('u.role LIKE :filter')
                ->orWhere('u.id LIKE :filter')
                ->setParameter('filter', '%'.$filter.'%');
        }
        return $qb->getQuery()->getResult();
    }
    public function getClientListFiltered($filter){
        $qb = $this->createQueryBuilder('u');
        $qb->where('u.role LIKE :role')
            ->setParameter('role', 'client');
        if ($filter != null) {
            $qb->andWhere('u.username LIKE :filter')
                ->orWhere('u.email LIKE :filter')
                ->orWhere('u.numTel LIKE :filter')
                ->orWhere('u.id LIKE :filter')
                ->setParameter('filter', '%'.$filter.'%');
        }
        return $qb->getQuery()->getResult();
    }



//    /**
//     * @return User[] Returns an array of User objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('u')
//            ->andWhere('u.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('u.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?User
//    {
//        return $this->createQueryBuilder('u')
//            ->andWhere('u.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
