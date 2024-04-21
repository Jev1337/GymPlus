<?php

namespace App\Form;

use App\Entity\Detailfacture;
use App\Entity\Facture;
use App\Entity\Produit;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;

class DetailfactureType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('iddetailfacture')
            ->add('prixventeunitaire')
            ->add('quantite')
            ->add('tauxremise')
            // ->add('prixtotalarticle')
            ->add('idproduit' , EntityType::class , 
            [
                'class' => Produit::class,
            ]) 
            ->add('idfacture' , EntityType::class , 
            [
                'class' => Facture::class,
            ]) 
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Detailfacture::class,
        ]);
    }
}
