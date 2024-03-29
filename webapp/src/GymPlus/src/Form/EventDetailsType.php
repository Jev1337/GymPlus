<?php

namespace App\Form;

use App\Entity\EventDetails;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\NumberType;

class EventDetailsType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name')
            ->add('type')
            ->add('eventDate')
            ->add('duree')
            ->add('nbPlaces', null, ['disabled' => $options['isEditAction'] ?? false])  // Use the option here
            ->add('save', SubmitType::class);
    }
    
    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => EventDetails::class,
            'isEditAction' => false,  // Add this line to define the new option
        ]);
    }
}