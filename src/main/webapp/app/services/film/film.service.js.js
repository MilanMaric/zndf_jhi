(function () {
    'use strict';
    angular
            .module('zndfApp')
            .factory('Film', Film);
    Film.$inject = ['$resource'];
    function Film($resource) {
        var service = $resource('api/films/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {method: 'POST'},
            'update': {method: 'PUT'},
            'delete': {method: 'DELETE'},
            'getComments': {method: 'GET', url: '/api/films/:id/comments', isArray: true},
            'saveComment': {method: 'POST', url: '/api/films/:id/comments'},
            'saveTrailer': {method: 'POST', url: '/api/films/:id/trailers'},
            'getTrailers': {method: 'GET', url: '/api/films/:id/trailers', isArray: true},
            'getFavorites': {method: 'GET', url: '/api/films/favorites', isArray: true},
            'checkFavorite': {method: 'GET', url: '/api/films/:id/like'},
            'setFavorite': {method: 'POST', url: '/api/films/:id/like'},
            'checkRate': {method: 'GET', url: '/api/films/:id/rate'},
            'setRate': {method: 'POST', url: '/api/films/:id/rate'},
            'uploadVideo': {
                method: 'POST',
                url: '/api/films/:id/trailer',
                headers: {'Content-Type': undefined},
                transformRequest: function (data) {
                    var fd = new FormData();
                    fd.append("video", data);
                    return fd;
                }
            }
        }
        );
        return service;
    }
})();
