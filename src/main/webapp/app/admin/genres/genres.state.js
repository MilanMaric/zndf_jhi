(function () {
    'use strict';

    angular
            .module('zndfApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('genres', {
                    parent: 'admin',
                    url: '/genres?page&sort',
                    data: {
                        authorities: ['ROLE_ADMIN'],
                        pageTitle: 'userManagement.home.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/admin/genres/genres.html',
                            controller: 'GenresController',
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
                                $translatePartialLoader.addPart('genre');
                                return $translate.refresh();
                            }]

                    }})
                .state('genre-detail', {
                    parent: 'admin',
                    url: '/genres/:id',
                    data: {
                        authorities: ['ROLE_ADMIN'],
                        pageTitle: 'genre.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/admin/genres/genres-detail.html',
                            controller: 'GenresDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('genre');
                                return $translate.refresh();
                            }]
                    }
                })
                .state('genres.new', {
                    parent: 'genres',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_ADMIN']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/admin/genres/genres-dialog.html',
                                controller: 'GenresDialogController',
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
                                $state.go('genres', null, {reload: true});
                            }, function () {
                                $state.go('genres');
                            });
                        }]
                })
                .state('genres.edit', {
                    parent: 'genres',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_ADMIN']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/admin/genres/genres-dialog.html',
                                controller: 'GenresDialogController',
                                controllerAs: 'vm',
                                backdrop: 'static',
                                size: 'lg',
                                resolve: {
                                    entity: ['Genre', function (Genre) {
                                            return Genre.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('genres', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                })
                .state('genres.delete', {
                    parent: 'genres',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_ADMIN']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/admin/genres/genres-delete-dialog-controller.js',
                                controller: 'GenresDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['Genre', function (Genre) {
                                            return Genre.get({id: $stateParams.id});
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
