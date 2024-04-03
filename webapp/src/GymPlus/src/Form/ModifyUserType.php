<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TelType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormInterface;

class ModifyUserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('username', TextType::class, [
            'required' => true,
            'label' => 'Username',
            'attr' =>[
                'pattern' => '[a-zA-Z0-9]*',
                'minLength' => 3,
                'maxLength' => 20,
                'placeholder' => 'Username',
            ]
        ])
        ->add('firstname', TextType::class, [
            'required' => true,
            'label' => 'First Name',
            'attr' =>[
                'pattern' => '[a-zA-Z ]*',
                'minLength' => 3,
                'maxLength' => 20,
                'placeholder' => 'First Name',
            ]
        ])
        ->add('lastname', TextType::class, [
            'required' => true,
            'label' => 'Last Name',
            'attr' =>[
                'pattern' => '[a-zA-Z ]*',
                'minLength' => 3,
                'maxLength' => 20,
                'placeholder' => 'Last Name',
            ]
        ])
        ->add('dateNaiss', DateType::class, [
            'widget' => 'single_text',
            'label' => 'Date of Birth',
            'required' => true,
            'attr' =>[
                'placeholder' => 'Date of Birth',
            ]
        ])
        ->add('numTel', TelType::class, [
            'required' => true,
            'label' => 'Phone Number',
            'attr' =>[
                'pattern' => '[0-9]*',
                'minLength' => 8,
                'maxLength' => 8,
                'placeholder' => 'Phone Number',
            ]
        ])
        ->add('adresse', TextType::class, [
            'required' => true,
            'label' => 'Address',
            'attr' =>[
                'pattern' => '[a-zA-Z0-9 ]*',
                'minLength' => 3,
                'maxLength' => 50,
                'placeholder' => 'Address',
            ]
        ])
        ->add('submit', SubmitType::class, [
            'label' => 'Save'
        ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => User::class,
            'validation_groups' => ['update' , 'Default'],
        ]);
    }
}
