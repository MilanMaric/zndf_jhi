(function () {
    'use strict';

    angular
            .module('zndfApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('gallery', {
                    parent: 'app',
                    url: '/gallery?page&sort',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'userManagement.home.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/gallery/gallery.html',
                            controller: 'GalleryController',
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
                                $translatePartialLoader.addPart('gallery');
                                return $translate.refresh();
                            }]

                    }})
                .state('gallery.new', {
                    parent: 'gallery',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_ADMIN']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/gallery/gallery-dialog.html',
                                controller: 'GalleryDialogController',
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
                                $state.go('gallery', null, {reload: true});
                            }, function () {
                                $state.go('gallery');
                            });
                        }]
                })

                .state('gallery.delete', {
                    parent: 'gallery',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/gallery/gallery-delete-dialog-controller.js',
                                controller: 'GalleryDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['Gallery', function (Gallery) {
                                            return Gallery.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function () {
                                $state.go('user-management', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                })
                .state('gallery.detail', {
                    parent: 'gallery',
                    url: '/{id}',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'app/gallery/gallery-detail-dialog.html',
                                controller: 'GalleryDetailDialogController',
                                controllerAs: 'vm',
                                size: 'lg'
                            }).result.then(function () {
                                $state.go('user-management', null, {reload: true});
                            }, function () {
                                $state.go('^');
                            });
                        }]
                });
        ;
    }
})();
