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

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('username', TextType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Username']
        ])
        ->add('firstname', TextType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'First Name']
        ])
        ->add('lastname', TextType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Last Name']
        ])
        ->add('dateNaiss', DateType::class, [
            'widget' => 'single_text',
            'attr' => ['placeholder' => 'Date of Birth']
        ])
        ->add('password', PasswordType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Password']
        ])
        ->add('email', EmailType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Email']
        ])
        ->add('numTel', TelType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Phone Number']
        ])
        ->add('adresse', TextType::class, [
            'required' => true,
            'attr' => ['placeholder' => 'Address']
        ])
        ->add('photo', FileType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'Upload Photo']
        ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
