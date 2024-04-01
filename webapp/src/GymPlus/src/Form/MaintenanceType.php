<?php

namespace App\Form;

use App\Entity\Maintenances;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Doctrine\ORM\EntityRepository;
use App\Entity\EquipementsDetails;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;

class MaintenanceType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('dateMaintenance', DateTimeType::class, [
                'widget' => 'single_text',
                'required' => true,
            ]
            )
            ->add('status', TextType::class, [
                'required' => true,
            
            ])
            ->add('equipementsDetails', EntityType::class, [
                'required' => true,
                'class' => EquipementsDetails::class,
                'choice_label' => 'id',
                'query_builder' => function (EntityRepository $er) {
                    return $er->createQueryBuilder('ed')
                        ->where('ed.etat = :etat1 or ed.etat = :etat2')
                        ->setParameter('etat1', 'Good')
                        ->setParameter('etat2', 'Bad');
                },
            ])
            ->add('submit', SubmitType::class, [
                'label' => 'Submit',
                'attr' => ['class' => 'btn btn-primary'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Maintenances::class,
        ]);
    }
}
