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
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
class EventDetailsType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    { $event = $builder->getData();
        $builder
            ->add('name')
            ->add('type', ChoiceType::class, [
                'choices'  => [
                    'Swimming' => 'swimming',
                    'Boxing' => 'boxing',
                    'Gymnastic' => 'gymnastic',
                    'Powerlifting' => 'powerlifting',
                    'Bodybuilding' => 'bodybuilding',
                    'Spinning' => 'spinning',
                    'Crossfit' => 'crossfit',
                ],
                'placeholder' => 'Choose an event type',
            ])
            ->add('eventDate', DateTimeType::class, [
                'widget' => 'single_text',
                'required' => true,
            ]
            )
            ->add('duree')
            ->add('nbPlaces')
            ->add('Add', SubmitType::class);
    }
    

}