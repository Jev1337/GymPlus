<?php

namespace App\Form;

use App\Entity\EquipementsDetails;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;


class EquipementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name', TextType::class, [
                'required' => true
            ])
            ->add('description', TextType::class, [
                'required' => true
            ])
            ->add('dureeDeVie', IntegerType::class, [
                'required' => true
            ])
            ->add('etat', ChoiceType::class, [
                'choices' => [
                    'Good' => 'Good',
                    'Bad' => 'Bad',
                ],
                'required' => true,
            ])
            ->add('submit', SubmitType::class, [
                'label' => 'Confirm',
                'attr' => [
                    'class' => 'btn btn-primary'
                ]
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => EquipementsDetails::class,
        ]);
    }
}
