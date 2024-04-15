<?php

namespace App\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\NumberType;

class GPPricesType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('gpprice1', NumberType::class, [
                'label' => 'Price 1',
                'required' => true,
            ])
            ->add('gpprice2', NumberType::class, [
                'label' => 'Price 2',
                'required' => true,
            ])
            ->add('gpprice3', NumberType::class, [
                'label' => 'Price 3',
                'required' => true,
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            // Configure your form options here
        ]);
    }
}
