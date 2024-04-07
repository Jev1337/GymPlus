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


class ProduitType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name')
            ->add('prix')
            ->add('stock')
            ->add('description')
            //->add('categorie')
            ->add('categorie' , ChoiceType::class , 
            [
                'choices'=>
                [   'food'=>'food',
                    'equipement'=>'equipement',
                    'vetement'=>'vetement',
                ],
            ])
            ->add('photo', FileType::class,[
                    'required' => false,
                    'mapped' => true, 
            ])
            ->add('seuil')
            ->add('promo')
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
