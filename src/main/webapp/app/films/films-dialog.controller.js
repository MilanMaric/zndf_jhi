(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('FilmsDialogController', FilmsDialogController);

    FilmsDialogController.$inject = ['$stateParams', 'entity', 'Film', 'JhiLanguageService', 'Genre'];

    function FilmsDialogController($stateParams, entity, Film, JhiLanguageService, Genre) {
        var vm = this;

        vm.languages = null;
        vm.save = save;
        vm.film = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        loadGenres();

        function onSaveSuccess(result) {
            vm.isSaving = false;

        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
            vm.film.actorRoles=[];
            console.log(vm.film);
            if (vm.film.id !== null) {
                Film.update(vm.film, onSaveSuccess, onSaveError);
            } else {
                Film.save(vm.film, onSaveSuccess, onSaveError);
            }
        }

        function loadGenres() {
            vm.genres = Genre.query();
        }


    }
})();
