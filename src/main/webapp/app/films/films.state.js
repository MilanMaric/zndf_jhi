(function () {
    'use strict';

    angular
            .module('zndfApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('films', {
                    parent: 'app',
                    url: '/films?page&sort',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'userManagement.home.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/films/films.html',
                            controller: 'FilmsController',
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
                                $translatePartialLoader.addPart('film');
                                return $translate.refresh();
                            }]

                    }})
                .state('films-detail', {
                    parent: 'app',
                    url: '/films/:id',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'film.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/films/films-detail.html',
                            controller: 'FilmsDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('film');
                                return $translate.refresh();
                            }]
                    }
                })
                .state('films.new', {
                    parent: 'films',
                    url: '/film/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/films/films-dialog.html',
                            controller: 'FilmsDialogController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                })
                .state('films.edit', {
                    parent: 'films',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/films/films-dialog.html',
                                controller: 'FilmsDialogController',
                                controllerAs: 'vm',
                                backdrop: 'static',
                                size: 'lg',
                                resolve: {
                                    entity: ['Film', function (Film) {
                                            return Film.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('films', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                })
                .state('films.delete', {
                    parent: 'films',
                    url: '/:id/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/films/films-delete-dialog-controller.js',
                                controller: 'FilmsDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['Film', function (Film) {
                                            return Film.get({id: $stateParams.id});
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
