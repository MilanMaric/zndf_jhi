(function () {
    'use strict';

    angular
            .module('zndfApp')
            .factory('StatisticsService', StatisticsService);

    StatisticsService.$inject = ['$resource'];

    function StatisticsService($resource) {
        var service = $resource('api/statistics/', {}, {
            'byRate': {method: 'GET', isArray: true, url: '/api/statistics/films/favorites'},
            'byPopularity': {
                method: 'GET',
                isArray: true,
                url: '/api/statistics/films/rated'
            }
        });

        return service;
    }
})();
