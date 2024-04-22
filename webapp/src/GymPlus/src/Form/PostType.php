<?php

namespace App\Form;

use App\Entity\Post;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Vich\UploaderBundle\Form\Type\VichImageType;

class PostType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // ->add('idUser')
            // ->add('mode')
            ->add('content', TextareaType::class, [
                'required' => false
            ])
            // ->add('imageFile', VichImageType::class, [
            //     'required' => false,
            //     'attr' => ["class" => 'text-center btn btn-outline-primary rounded', "style" => 'height:2.5rem;width: 5rem;']
            // ])
            ->add('photo', FileType::class, [
                'required' => false,
                'attr' => ["class" => 'form-control btn btn-outline-primary rounded' , "style" => 'height:2.5rem;width: 5rem;']
            ])  
            ->add('post', SubmitType::class);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Post::class,
        ]);
    }
}
