(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('ActorsDialogController', ActorsDialogController);

    ActorsDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Actor', 'JhiLanguageService'];

    function ActorsDialogController($stateParams, $uibModalInstance, entity, Actor, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.actor = entity;


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
            if (vm.actor.id !== null) {
                Actor.update(vm.actor, onSaveSuccess, onSaveError);
            } else {
                Actor.save(vm.actor, onSaveSuccess, onSaveError);
            }
        }
    }
})();
