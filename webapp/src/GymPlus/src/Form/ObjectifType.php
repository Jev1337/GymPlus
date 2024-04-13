<?php

namespace App\Form;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;

use App\Entity\Objectif;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use App\Entity\User;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Doctrine\ORM\EntityRepository;
use Symfony\Component\Form\FormInterface;

class ObjectifType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('poidsobj', TextType::class, [
            ])
            ->add('poidsact', TextType::class, [
            ])    
            ->add('taille', TextType::class, [
            ])     
            ->add('alergie', TextareaType::class, [
            ])        
            ->add('typeobj', ChoiceType::class, [
                'choices' => [
                    'Default 1' => 'Default',
                    'Version ++' => 'Version ++',
                ],
                'label' => 'Type',
            ])
            ->add('datef', DateType::class, [
                'widget' => 'single_text',
            ])           
            ->add('coachid', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'username',
                'query_builder' => function (EntityRepository $er) {
                    return $er->createQueryBuilder('u')
                        ->where('u.role LIKE :role')
                        ->setParameter('role', 'staff');
                },
            ])
            ->add('submit', SubmitType::class)
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Objectif::class,
            
        ]);
    }
}
