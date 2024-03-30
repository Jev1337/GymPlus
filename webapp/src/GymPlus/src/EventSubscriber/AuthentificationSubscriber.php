<?php

namespace App\EventSubscriber;

use Symfony\Component\EventDispatcher\EventSubscriberInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpKernel\Event\RequestEvent;
use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

class AuthentificationSubscriber implements EventSubscriberInterface
{
    private $urlGenerator;
    private $session;

    public function __construct(UrlGeneratorInterface $urlGenerator, SessionInterface $session)
    {
        $this->urlGenerator = $urlGenerator;
        $this->session = $session;
    }

    public function onKernelRequest(RequestEvent $event): void
    {
        $user = $this->session->get('user');

        $currentRoute = $event->getRequest()->attributes->get('_route');

        $protectedRoutes = ['app_subs', 'app_buy', 'app_profile', 'app_photo', 'app_logout', 'app_dashboard'];
        if (!in_array($currentRoute, $protectedRoutes)) {
            return;
        }
        if (!$user) {
            $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_login')));
            return;
        }
    }

    public static function getSubscribedEvents(): array
    {
        return [
            KernelEvents::REQUEST => 'onKernelRequest',
        ];
    }
}