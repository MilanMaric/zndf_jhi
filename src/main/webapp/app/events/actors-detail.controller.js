(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('ActorsDetailController', ActorsDetailController);

    ActorsDetailController.$inject = ['$stateParams', 'Actor'];

    function ActorsDetailController ($stateParams, Actor) {
        var vm = this;

        vm.load = load;
        vm.actor = {};

        vm.load($stateParams.id);

        function load (id) {
            Actor.get({id: id}, function(result) {
                vm.actor = result;
            });
        }
    }
})();
