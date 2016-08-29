(function () {
    'use strict';

    angular
            .module('zndfApp')
            .factory('Gallery', Gallery);

    Gallery.$inject = ['$resource'];

    function Gallery($resource) {
        var service = $resource('api/gallery/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'save': {
                method: 'POST',
                headers: {'Content-Type': undefined},
                transformRequest: function (data) {
                    var fd = new FormData();
                    fd.append("image", data);
                    return fd;
                }},
            'delete': {method: 'DELETE'}
        });

        return service;
    }
})();
