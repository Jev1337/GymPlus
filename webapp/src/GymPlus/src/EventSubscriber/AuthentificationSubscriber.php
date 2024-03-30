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

        /** These arrays represent the routes that require authentication and the routes that each role can access
        * The routes that require authentication are the routes that are protected and can only be accessed by authenticated users
        * The routes that each role can access are the routes that are accessible by the users with the corresponding role
        * If a route is not in any of the arrays, it means that it is accessible by everyone
        */

        // Routes that require authentication
        $protectedRoutes = ['app_subs', 'app_buy', 'app_profile', 'app_photo', 'app_logout', 'app_dashboard', 'app_objectif'
        , 'app_Schedule_objectif', 'app_events', 'eventb', 'event_delete', 'event_edit', 'app_eventsf', 'event_join', 'event_leave'];

        // Routes that ONLY clients can access
        $clientRoutes = ['app_home', 'app_subs', 'app_buy', 'app_profile', 'app_photo', 'app_objectif', 'app_Schedule_objectif', 'event_join', 'event_leave','app_eventsf'];
        // Routes that staff can access
        $staffRoutes = ['app_events', 'eventb', 'event_delete', 'event_edit','eventParticipant_kick','eventParticipant'];
        // Routes that ONLY admin can access
        $adminRoutes = $staffRoutes;
        $adminRoutes = array_merge($adminRoutes, 
            []
        );


        if ($user && $user->getRole() == 'client' && (in_array($currentRoute, $staffRoutes) || in_array($currentRoute, $adminRoutes))) {
            $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_home')));
            return;
        }
        if ($user && ($user->getRole() == 'staff') && (in_array($currentRoute, $clientRoutes) || in_array($currentRoute, $adminRoutes))) {
            $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_dashboard')));
            return;
        }
        
        if ($user && ($user->getRole() == 'admin') && (in_array($currentRoute, $clientRoutes))) {
            $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_dashboard')));
            return;
        }

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