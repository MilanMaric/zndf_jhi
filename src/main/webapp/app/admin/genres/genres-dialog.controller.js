(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('GenresDialogController', GenresDialogController);

    GenresDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Genre', 'JhiLanguageService'];

    function GenresDialogController($stateParams, $uibModalInstance, entity, Genre, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.genre = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
            if (vm.genre.id !== null) {
                Genre.update(vm.genre, onSaveSuccess, onSaveError);
            } else {
                Genre.save(vm.genre, onSaveSuccess, onSaveError);
            }
        }
    }
})();
