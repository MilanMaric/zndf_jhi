(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('EventsDeleteController', EventsDeleteController);

    EventsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Event'];

    function EventsDeleteController ($uibModalInstance, entity, Event) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (login) {
            Event.delete({login: login},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
