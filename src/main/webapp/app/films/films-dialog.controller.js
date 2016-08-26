(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('FilmsDialogController', FilmsDialogController);

    FilmsDialogController.$inject = ['$stateParams',  'entity', 'Film', 'JhiLanguageService'];

    function FilmsDialogController($stateParams,  entity, Film, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.film = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear() {
           
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
           
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
            if (vm.film.id !== null) {
                Film.update(vm.film, onSaveSuccess, onSaveError);
            } else {
                Film.save(vm.film, onSaveSuccess, onSaveError);
            }
        }
    }
})();
