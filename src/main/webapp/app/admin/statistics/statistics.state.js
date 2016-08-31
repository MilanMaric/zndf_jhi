(function() {
    'use strict';

    angular
        .module('zndfApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('jhi-statistics', {
            parent: 'admin',
            url: '/statistics',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'statistics.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/statistics/statistics.html',
                    controller: 'StatisticsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statistics');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
