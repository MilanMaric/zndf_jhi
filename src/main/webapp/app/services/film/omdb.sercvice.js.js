(function () {
    'use strict';

    angular
            .module('zndfApp')
            .factory('OMDB', OMDB);

    OMDB.$inject = ['$resource'];

    function OMDB($resource) {
        var service = $resource('api/omdb', {}, {
            'query': {method: 'GET'}
        });

        return service;
    }
})();
