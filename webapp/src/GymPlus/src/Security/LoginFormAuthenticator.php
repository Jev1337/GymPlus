<?php

namespace App\Security;

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\UserProviderInterface;
use Symfony\Component\Security\Guard\Authenticator\AbstractFormLoginAuthenticator;
use Symfony\Component\Security\Guard\PasswordAuthenticatedInterface;
use Symfony\Component\Security\Http\Util\TargetPathTrait;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use App\Repository\UserRepository;
use Symfony\Component\Security\Core\Exception\CustomUserMessageAuthenticationException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpClient\HttpClient;


class LoginFormAuthenticator extends AbstractFormLoginAuthenticator implements PasswordAuthenticatedInterface
{
    use TargetPathTrait;

    private $urlGenerator;
    private $repo;

    public function __construct(UrlGeneratorInterface $urlGenerator, UserRepository $repo)
    {
        $this->urlGenerator = $urlGenerator;
        $this->repo = $repo;
    }

    public function supports(Request $request)
    {
        return 'app_login' === $request->attributes->get('_route')
            && $request->isMethod('POST');
    }

    public function getCredentials(Request $request)
    {
        $credentials = [
            'email' => $request->request->get('login')['email'],
            'password' => $request->request->get('login')['password'],
            'captcha' => $request->request->get('login')['captcha'],
        ];
        $request->getSession()->set(
            Security::LAST_USERNAME,
            $credentials['email']
        );

        return $credentials;
    }

    public function getUser($credentials, UserProviderInterface $userProvider)
    {
        return $this->repo->findUserByEmail($credentials['email']);
    }

    public function checkCredentials($credentials, UserInterface $user)
    {
        //verify captcha with google api
        $client = HttpClient::create();
        $response = $client->request('POST', 'https://www.google.com/recaptcha/api/siteverify', [
            'body' => [
                'secret' => '6LdlPKkpAAAAAJt_IYp6Nk2pIimy6h4UEJTyk9tQ',
                'response' => $credentials['captcha']
            ]
        ]);
        $json = $response->getContent();
        $data = json_decode($json, true);
    
        if (!$data['success']) {
            throw new CustomUserMessageAuthenticationException('Invalid captcha!');
        }

        if ($data['score'] < 0.5) {
            throw new CustomUserMessageAuthenticationException('Invalid captcha!');
        }

        if (!password_verify($credentials['password'], $user->getPassword())) {
            throw new CustomUserMessageAuthenticationException('Invalid credentials!');
        }

        return true;
        

    }

    public function onAuthenticationSuccess(Request $request, TokenInterface $token, string $providerKey)
    {
        if ($targetPath = $this->getTargetPath($request->getSession(), $providerKey)) {
            return new RedirectResponse($targetPath);
        }

        // redirect to some "app_home" route - of wherever you want
        return new RedirectResponse($this->urlGenerator->generate('app_home'));
    }

    protected function getLoginUrl()
    {
        return $this->urlGenerator->generate('app_login');
    }

    public function getPassword($credentials): ?string
    {
        return $credentials['password'];
    }
}

?>