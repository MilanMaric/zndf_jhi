(function () {
    'use strict';

    angular
            .module('zndfApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('events', {
                    parent: 'app',
                    url: '/events?page&sort',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'userManagement.home.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/events/events.html',
                            controller: 'EventsController',
                            controllerAs: 'vm'
                        }
                    }, params: {
                        page: {
                            value: '1',
                            squash: true
                        },
                        sort: {
                            value: 'id,asc',
                            squash: true
                        }
                    },
                    resolve: {
                        pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                                return {
                                    page: PaginationUtil.parsePage($stateParams.page),
                                    sort: $stateParams.sort,
                                    predicate: PaginationUtil.parsePredicate($stateParams.sort),
                                    ascending: PaginationUtil.parseAscending($stateParams.sort)
                                };
                            }],
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('event');
                                return $translate.refresh();
                            }]

                    }})
                .state('events-detail', {
                    parent: 'app',
                    url: '/events/:id',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'event.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/events/events-detail.html',
                            controller: 'EventsDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('event');
                                return $translate.refresh();
                            }]
                    }
                })
                .state('events.new', {
                    parent: 'events',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/events/events-dialog.html',
                                controller: 'EventsDialogController',
                                controllerAs: 'vm',
                                backdrop: 'static',
                                size: 'lg',
                                resolve: {
                                    entity: function () {
                                        return {
                                            id: null, login: null, firstName: null, lastName: null, email: null,
                                            activated: true, langKey: null, createdBy: null, createdDate: null,
                                            lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                            resetKey: null, authorities: null
                                        };
                                    }
                                }
                            }).result.then(function () {
                                $state.go('events', null, {reload: true});
                            }, function () {
                                $state.go('events');
                            });
                        }]
                })
                .state('events.edit', {
                    parent: 'events',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/events/events-dialog.html',
                                controller: 'EventsDialogController',
                                controllerAs: 'vm',
                                backdrop: 'static',
                                size: 'lg',
                                resolve: {
                                    entity: ['Event', function (Event) {
                                            return Event.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('events', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                })
                .state('events.delete', {
                    parent: 'events',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/events/events-delete-dialog-controller.js',
                                controller: 'EventsDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['Event', function (Event) {
                                            return Event.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('user-management', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                });
    }
})();
