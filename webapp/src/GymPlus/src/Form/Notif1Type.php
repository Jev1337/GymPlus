<?php

namespace App\Form;

use App\Entity\Notif;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\DateType;

class Notif1Type extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('titre' , TextType::class, [
                'label' => 'Title',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('datevente', DateType::class, [
                'label' => 'Date',
                'widget' => 'single_text', // Afficher le champ en tant que champ de texte simple
                'format' => 'yyyy-MM-dd', // Format de la date
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'], 
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('etat', ChoiceType::class, [
                'choices' => [
                    'Unread' => 'Unread',
                    'Completed' => 'Completed',
                ],
                'label' => 'Status',
                'label_attr' => [
                    'style' => 'color: blue; width: 100px; display: inline-block; text-align: right; margin-right: 10px;',
                ], 
                'attr' => [
                    'style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;',
                ],
            ])            
            ->add('Description' , TextType::class, [
                'label' => 'Description',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('idProduit', TextType::class, [
                'label' => 'Product ID',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            ->add('nomUser' , TextType::class, [
                'label' => 'User Name',
                'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
                'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] ,
                'disabled' => true, // Rend le champ non modifiable
            ])
            // ->add('etatCommentaire')  
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Notif::class,
        ]);
    }
}
