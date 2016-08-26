(function() {
    'use strict';

    angular
        .module('zndfApp')
        .controller('GenresDetailController', GenresDetailController);

    GenresDetailController.$inject = ['$stateParams', 'Genre'];

    function GenresDetailController ($stateParams, Genre) {
        var vm = this;

        vm.load = load;
        vm.genre = {};

        vm.load($stateParams.id);

        function load (id) {
            Genre.get({id: id}, function(result) {
                vm.genre = result;
            });
        }
    }
})();
