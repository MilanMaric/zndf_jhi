(function () {
    'use strict';

    angular
            .module('zndfApp')
            .factory('Comment', Comment);

    Comment.$inject = ['$resource'];

    function Comment($resource) {
        var service = $resource('/api/', {}, {
            'query': {method: 'GET', isArray: true, url: '/api/films/:id/comments'},
            'save': {method: 'POST', url: '/api/films/:id/comments'}
        });

        return service;
    }
})();
