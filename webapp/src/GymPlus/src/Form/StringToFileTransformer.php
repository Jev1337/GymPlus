<?php 
namespace App\Form;
use Symfony\Component\Form\DataTransformerInterface;
use Symfony\Component\HttpFoundation\File\File;

class StringToFileTransformer implements DataTransformerInterface
{
    public function transform($string)
    {
        return file_exists($string) ? new File($string) : null;
    }

    public function reverseTransform($file)
    {
        return $file ? $file->getPathname() : null;
    }
}
