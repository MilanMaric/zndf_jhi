(function () {
    'use strict';

    angular
        .module('zndfApp')
        .factory('Actor', Actor);

    Actor.$inject = ['$resource'];

    function Actor ($resource) {
        var service = $resource('api/actors/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
