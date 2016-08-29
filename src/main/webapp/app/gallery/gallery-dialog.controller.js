(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('GalleryDialogController', GalleryDialogController);

    GalleryDialogController.$inject = ['$stateParams', '$uibModalInstance',  'Gallery', 'JhiLanguageService'];

    function GalleryDialogController($stateParams, $uibModalInstance, Gallery, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;


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
            Gallery.save(vm.newPicture, onSaveSuccess, onSaveError);
        }
    }
})();
