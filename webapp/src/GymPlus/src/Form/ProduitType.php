<?php

namespace App\Form;

use App\Entity\Produit;
use Symfony\Component\DomCrawler\Image;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Symfony\Component\Form\Extension\Core\Type\TextType;


class ProduitType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // ->add('name')
        //     ->add('prix' , null , ['label' => 'Price'] )
        //     ->add('stock'  , null , ['label' => 'Stock'] )
        //     ->add('description' , null , ['label' => 'Description'] )
        //     //->add('categorie')
        //     ->add('categorie' , ChoiceType::class , 
        //     [
        //         'label' => 'Category',
        //         'choices'=>
        //         [   'food'=>'food',
        //             'equipement'=>'equipement',
        //             'vetement'=>'vetement',
        //         ],
        //     ])
        //     ->add('photo', FileType::class,[
        //             'required' => false,
        //             'mapped' => true, 
        //     ])
        //     ->add('seuil' , null , ['label' => 'Limit'] )
        //     ->add('promo' , null , ['label' => 'Discount'] )
        // ;
        ->add('name', TextType::class, [
            'label' => 'Name',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('prix' , TextType::class, [
            'label' => 'Price',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('stock', TextType::class, [
            'label' => 'Stock',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('description', TextType::class, [
            'label' => 'Description',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('categorie', ChoiceType::class, [
            'label' => 'Category',
            'choices' => [
                'food' => 'food',
                'equipement' => 'equipement',
                'vetement' => 'vetement',
            ],
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('photo', FileType::class, [
            'label' => 'Photo',
            'required' => false,
            'mapped' => true,
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('seuil', TextType::class, [
            'label' => 'Limit',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ->add('promo', TextType::class, [
            'label' => 'Discount',
            'label_attr' => ['style' => 'width: 100px; display: inline-block; text-align: right; margin-right: 10px;'], 
            'attr' => ['style' => 'display: inline-block; width: calc(100% - 110px); width: 300px;'] 
        ])
        ;
   

    // Si le produit existe déjà (modification), on pré-remplit les champs pour photo et catégorie
    $builder->addEventListener(FormEvents::PRE_SET_DATA, function (FormEvent $event) {
        $produit = $event->getData();
        $form = $event->getForm();

        if ($produit && $produit->getIdproduit()) {
            // Pré-remplir le champ photo s'il existe déjà une photo
            if ($produit->getPhoto()) {
                $form->remove('photo');
                $form->add('photo', FileType::class, [
                    'required' => false,
                    'mapped' => false, 
                    'attr' => ['placeholder' => 'Votre photo actuelle']
                ]);
            }

            // Pré-remplir le champ catégorie
            if ($produit->getCategorie()) {
                $form->get('categorie')->setData($produit->getCategorie());
            }
        }
    });
}

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Produit::class,
        ]);
    }
}
