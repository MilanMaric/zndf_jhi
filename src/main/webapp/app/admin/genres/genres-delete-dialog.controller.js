(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('GenresDeleteController', GenresDeleteController);

    GenresDeleteController.$inject = ['$uibModalInstance', 'entity', 'Genre'];

    function GenresDeleteController ($uibModalInstance, entity, Genre) {
        var vm = this;

        vm.genre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (login) {
            Genre.delete({login: login},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
