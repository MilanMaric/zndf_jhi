(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('ActorsDeleteController', ActorsDeleteController);

    ActorsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Actor'];

    function ActorsDeleteController ($uibModalInstance, entity, Actor) {
        var vm = this;

        vm.actor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Actor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
