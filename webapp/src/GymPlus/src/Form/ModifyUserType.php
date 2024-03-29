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

class ModifyUserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('username', TextType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'Username']
        ])
        ->add('firstname', TextType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'First Name']
        ])
        ->add('lastname', TextType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'Last Name']
        ])
        ->add('dateNaiss', DateType::class, [
            'widget' => 'single_text',
            'attr' => ['placeholder' => 'Date of Birth']
        ])
        ->add('numTel', TelType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'Phone Number']
        ])
        ->add('adresse', TextType::class, [
            'required' => false,
            'attr' => ['placeholder' => 'Address']
        ])
        ->add('submit', SubmitType::class, [
            'label' => 'Save'
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
