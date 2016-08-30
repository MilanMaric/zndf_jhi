(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('EventsDetailController', EventsDetailController);

    EventsDetailController.$inject = ['$stateParams', 'Event'];

    function EventsDetailController ($stateParams, Event) {
        var vm = this;

        vm.load = load;
        vm.event = {};

        vm.load($stateParams.id);

        function load (id) {
            Event.get({id: id}, function(result) {
                vm.event = result;
            });
        }
    }
})();
