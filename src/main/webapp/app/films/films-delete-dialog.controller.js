(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('FilmsDeleteController', FilmsDeleteController);

    FilmsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Film'];

    function FilmsDeleteController ($uibModalInstance, entity, Film) {
        var vm = this;

        vm.film = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Film.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
