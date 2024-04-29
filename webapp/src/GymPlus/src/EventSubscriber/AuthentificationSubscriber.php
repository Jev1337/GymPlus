<?php

namespace App\EventSubscriber;

use Symfony\Component\EventDispatcher\EventSubscriberInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpKernel\Event\RequestEvent;
use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Repository\UserRepository;
use Symfony\Component\Security\Core\Security;

class AuthentificationSubscriber implements EventSubscriberInterface
{
    private $urlGenerator;

    public function __construct(UrlGeneratorInterface $urlGenerator, UserRepository $userRepository, Security $security)
    {
        $this->urlGenerator = $urlGenerator;
        $this->userRepository = $userRepository;
        $this->security = $security;
    }

    public function onKernelRequest(RequestEvent $event): void
    {   
        
        $currentRoute = $event->getRequest()->attributes->get('_route');

        /** These arrays represent the routes that require authentication and the routes that each role can access
        * The routes that require authentication are the routes that are protected and can only be accessed by authenticated users
        * The routes that each role can access are the routes that are accessible by the users with the corresponding role
        * If a route is not in any of the arrays, it means that it is accessible by everyone
        */

        // Routes that require authentication
        $protectedRoutes = ['app_subs', 'app_buy', 'app_profile', 'app_photo', 'app_logout', 'app_dashboard', 'app_objectif'
        , 'app_Schedule_objectif', 'app_events', 'eventb', 'event_delete', 'event_edit', 'app_eventsf', 'event_join', 'event_leave', 'getAll_post'
        , 'get_conversation', 'app_equipments', 'app_equipments_edit', 'app_equipments_delete', 'app_maintenances', 'app_maintenances_edit', 'app_maintenances_delete'
        , 'app_usermgmt', 'app_user_delete', 'app_user_edit', 'app_photo_admin', 'app_submgmt', 'app_removesub', 'app_subuser', 'app_user_delete_current' , 'app_updatefaceid','eventParticipant
        ','eventParticipant_kick','blacklised','ban','unban','past_events_history','rewards','points','belt','bag','whey'];

        // Routes that require client role
        $clientRoutes = ['app_home', 'app_subs', 'app_buy', 'app_profile', 'app_objectif', 'app_Schedule_objectif', 'event_join', 'event_leave','app_eventsf','getAll_post','get_conversation','rewards','points','belt','bag','whey','past_events',
        'event_vote'];
        
        // Routes that require staff role
        $staffRoutes = ['app_dashboard', 'app_events', 'eventb', 'event_delete', 'event_edit','eventParticipant_kick','eventParticipant', 'app_equipments', 'app_equipments_edit'
        , 'app_equipments_delete', 'app_maintenances', 'app_maintenances_edit', 'app_maintenances_delete','blacklised','ban','unban', 'app_dashboard_profile', 'app_submgmt', 'getAll_post', 'get_conversation', 'app_getsubdetails','past_events_history'];
        
        // Routes that require admin role (inherits staff routes)
        $adminRoutes = $staffRoutes;
        $adminRoutes = array_merge($adminRoutes, 
            ['app_usermgmt', 'app_user_delete', 'app_user_edit', 'app_photo_admin', 'app_updatefaceidmg']
        );
        
        // Get the current user
        $user = $this->security->getUser();

        if ($user) {

            if (in_array($currentRoute, $clientRoutes) || in_array($currentRoute, $staffRoutes) || in_array($currentRoute, $adminRoutes)){
                if ($user->getRole() == 'client' && !in_array($currentRoute, $clientRoutes)) {
                    $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_home')));
                    return;
                }
        
                if ($user->getRole() == 'staff' && !in_array($currentRoute, $staffRoutes)) {
                    $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_dashboard')));
                    return;
                }
        
                if ($user->getRole() == 'admin' && !in_array($currentRoute, $adminRoutes)) {
                    $event->setResponse(new RedirectResponse($this->urlGenerator->generate('app_dashboard')));
                    return;
                }
            }

            
        }

        // If the current route is not protected, do nothing to allow access to everyone
        if (!in_array($currentRoute, $protectedRoutes)) {
            return;
        }

        // Else if there is no user connected, redirect to the login page
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


?>