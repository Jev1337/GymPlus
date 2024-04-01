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
use Karser\Recaptcha3Bundle\Form\Recaptcha3Type;
use Karser\Recaptcha3Bundle\Validator\Constraints\Recaptcha3;

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
                'pattern' => '[a-zA-Z ]*',
                'minLength' => 3,
                'maxLength' => 20,
            ]
        ])
        ->add('lastname', TextType::class, [
            'required' => true,
            'attr' =>[
                'pattern' => '[a-zA-Z ]*',
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
                'pattern' => '^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$'
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
                'pattern' => '[a-zA-Z0-9 ]*',
                'minLength' => 3,
                'maxLength' => 50,
            ]
        ])
        ->add('photo', FileType::class, [
            'required' => true,
        ])
        ->add('captcha', Recaptcha3Type::class, [
            'constraints' => new Recaptcha3(),
            'action_name' => 'login',
            'locale' => 'en',
        ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => User::class,
            'validation_groups' => ['create'],
        ]);
    }
}
