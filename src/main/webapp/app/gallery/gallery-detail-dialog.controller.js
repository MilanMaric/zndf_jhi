(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('GalleryDetailDialogController', GalleryDetailDialogController);

    GalleryDetailDialogController.$inject = ['$stateParams', '$uibModalInstance',  'JhiLanguageService'];

    function GalleryDetailDialogController($stateParams, $uibModalInstance,  JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.languages = null;
        vm.id=$stateParams.id;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
