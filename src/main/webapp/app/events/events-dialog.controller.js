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
            var k=vm.event.time.time.split(":");
            var timeMillis=0;
            if(k.length==2){
                timeMillis=(k[0]*60+k[1])*60*1000;
            }
            var dateTimeMilis=vm.event.time.date.getTime()+timeMillis;
            var dateTimeObject=new Date(dateTimeMilis);
            vm.event.eventTime=dateTimeObject.toISOString();
            console.log("EVENT DATE: "+vm.event.eventDate);
            vm.event.time=undefined;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }
    }
})();
