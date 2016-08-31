(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('FilmsDialogController', FilmsDialogController);

    FilmsDialogController.$inject = ['$stateParams', 'entity', 'Film', 'JhiLanguageService', 'Genre', 'Actor', '$state', '$uibModal'];

    function FilmsDialogController($stateParams, entity, Film, JhiLanguageService, Genre, Actor, $state, $uibModal) {
        var vm = this;
        vm.actors = [];
        vm.languages = null;
        vm.save = save;
        vm.film = entity;
        vm.addActor = addActor;
        vm.removeActorRole = removeActorRole;
        loadActors();
        vm.createActor=createActor;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        loadGenres();

        function loadActors() {
            vm.actors = Actor.query();
        }

        function addActor(actor) {
            if (!vm.film.actorRoles)
                vm.film.actorRoles = [];
            vm.film.actorRoles.push({actor: actor, roleName: ""});
        }

        function removeActorRole(actorRole) {
            var index = vm.film.actorRoles.indexOf(actorRole);
            vm.film.actorRoles.splice(index, 1);
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            $state.go('films');
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
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

        function createActor() {
            $uibModal.open({
                templateUrl: 'app/actors/actors-dialog.html',
                controller: 'ActorsDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            id: null
                        };
                    }
                }
            }).result.then(function () {
                loadActors();
            }, function () {
                
            });
        }


    }
})();
