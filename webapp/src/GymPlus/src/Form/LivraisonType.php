<?php

namespace App\Form;

use App\Entity\Livraison;
// use Doctrine\DBAL\Types\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextType;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class LivraisonType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // ->add('idfacture')
            // ->add('idclient')
            // ->add('lieu')
            // ->add('etat')
            ->add('idfacture', TextType::class, [
                'label' => 'Invoice ID',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
           
            ->add('idclient', TextType::class, [
                'label' => 'Client ID',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('lieu', TextType::class, [
                'label' => 'Location',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('etat', ChoiceType::class, [
                'choices' => [
                    'Delivered' => 'Delivered',
                    'En cours' => strtolower('En cours'),
                ],
                'label' => 'Status',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
            ]);
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Livraison::class,
        ]);
    }
}
