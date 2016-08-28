(function () {
    'use strict';

    angular
            .module('zndfApp')
            .factory('Account', Account);

    Account.$inject = ['$resource'];

    function Account($resource) {
        var service = $resource('api/account', {}, {
            'get': {method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function (response) {
                        // expose response
                        return response;
                    }
                }
            },
            'saveImage': {
                method: 'POST',
                url: 'api/account/image',
                headers: {'Content-Type': undefined},
                transformRequest: function (data) {
                    var fd = new FormData();
                    fd.append("image", data);
                    return fd;
                }
            },
            'getImage': {
                method: 'GET',
                url: 'api/account/image'
            }
        });
        return service;
    }
})();
