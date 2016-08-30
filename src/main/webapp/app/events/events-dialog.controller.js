(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('EventsDialogController', EventsDialogController);

    EventsDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Event', 'JhiLanguageService'];

    function EventsDialogController($stateParams, $uibModalInstance, entity, Event, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.event = entity;


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
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }
    }
})();
