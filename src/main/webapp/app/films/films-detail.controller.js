(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('FilmsDetailController', FilmsDetailController);

    FilmsDetailController.$inject = ['$stateParams', 'Film'];

    function FilmsDetailController ($stateParams, Film) {
        var vm = this;

        vm.load = load;
        vm.film = {};

        vm.load($stateParams.id);

        function load (id) {
            Film.get({id: id}, function(result) {
                vm.film = result;
            });
        }
    }
})();
