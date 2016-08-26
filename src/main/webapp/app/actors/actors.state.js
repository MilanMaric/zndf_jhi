(function () {
    'use strict';

    angular
            .module('zndfApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('actors', {
                    parent: 'admin',
                    url: '/actors?page&sort',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'userManagement.home.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/actors/actors.html',
                            controller: 'ActorsController',
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
                                $translatePartialLoader.addPart('actor');
                                return $translate.refresh();
                            }]

                    }})
                .state('actors-detail', {
                    parent: 'admin',
                    url: '/actors/:id',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'actor.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/actors/actors-detail.html',
                            controller: 'ActorsDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('actor');
                                return $translate.refresh();
                            }]
                    }
                })
                .state('actors.new', {
                    parent: 'actors',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/actors/actors-dialog.html',
                                controller: 'ActorsDialogController',
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
                                $state.go('actors', null, {reload: true});
                            }, function () {
                                $state.go('actors');
                            });
                        }]
                })
                .state('actors.edit', {
                    parent: 'actors',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/actors/actors-dialog.html',
                                controller: 'ActorsDialogController',
                                controllerAs: 'vm',
                                backdrop: 'static',
                                size: 'lg',
                                resolve: {
                                    entity: ['Actor', function (Actor) {
                                            return Actor.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('actors', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                })
                .state('actors.delete', {
                    parent: 'actors',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/actors/actors-delete-dialog-controller.js',
                                controller: 'ActorsDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['Actor', function (Actor) {
                                            return Actor.get({id: $stateParams.id});
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
