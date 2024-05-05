<?php

namespace App\Form;
use App\Entity\Planning;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Form\Extension\Core\Type\TextType;

class PlanningType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('trainingprog', FileType::class, [

        ])  
        
           ->add('foodprog', FileType::class, [

            
        ]) 
            ->add('submit', SubmitType::class);
            $builder->get('trainingprog')->addModelTransformer(new StringToFileTransformer());
            $builder->get('foodprog')->addModelTransformer(new StringToFileTransformer());
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Planning::class,
        ]);
    }
   
}
