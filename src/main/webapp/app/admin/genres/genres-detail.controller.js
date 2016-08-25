(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('GenresDetailController', GenresDetailController);

    GenresDetailController.$inject = ['$stateParams', 'Genre'];

    function GenresDetailController ($stateParams, Genre) {
        var vm = this;

        vm.load = load;
        vm.genre = {};

        vm.load($stateParams.login);

        function load (login) {
            Genre.get({login: login}, function(result) {
                vm.genre = result;
            });
        }
    }
})();
