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
use Symfony\Component\Form\FormInterface;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('id', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[0-9]*',
                'minLength' => 8,
                'maxLength' => 8,
            ]
        ])
        ->add('username', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z0-9]*',
                'minLength' => 3,
                'maxLength' => 20,
            ]
        ])
        ->add('firstname', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z]*',
                'minLength' => 3,
                'maxLength' => 20,
            ]
        ])
        ->add('lastname', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z]*',
                'minLength' => 3,
                'maxLength' => 20,
            ]
        ])
        ->add('dateNaiss', DateType::class, [
            'widget' => 'single_text',
            'required' => true,
        ])
        ->add('password', PasswordType::class, [
            'required' => true,
            'attr' =>[
                'minLength' => 8,
                'maxLength' => 20,
            ],
        ])
        ->add('email', EmailType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z0-9]*',
                'minLength' => 3,
                'maxLength' => 50,
            ]
        ])
        ->add('numTel', TelType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[0-9]*',
                'minLength' => 8,
                'maxLength' => 8,
            ]
        ])
        ->add('adresse', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z0-9]*',
                'minLength' => 3,
                'maxLength' => 50,
            ]
        ])
        ->add('photo', FileType::class, [
            'required' => true,
        ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => User::class,
            'validation_groups' => function (FormInterface $form) {
                $data = $form->getData();
                return null === $data->getId() ? ['Default', 'create'] : ['Default', 'update'];
            },
        ]);
    }
}
