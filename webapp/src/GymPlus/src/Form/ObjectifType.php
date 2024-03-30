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

class ObjectifType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('poidsobj')
            ->add('poidsact')
            ->add('taille')
            ->add('alergie')
            ->add('typeobj', ChoiceType::class, [
                'choices' => [
                    'Default 1' => 'Default',
                    'Version ++' => 'Version ++',
                ],
                'label' => 'Type',
                'placeholder' => 'Select an option',
            ])
            ->add('datef', DateType::class, [
                'widget' => 'single_text',
                'attr' => ['placeholder' => 'Date ']
            ])           
                ->add('CoachNames', EntityType::class, [
                'class' => User::class,
                'query_builder' => function ($userRepository) {
                    return $userRepository->createQueryBuilder('u')
                        ->where('u.role LIKE :role')
                        ->setParameter('role', '%staff%');
                },
                'choice_label' => 'username',
                'placeholder' => 'Select your Coach',

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
