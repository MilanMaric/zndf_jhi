(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate', 'Account'];

    function SettingsController(Principal, Auth, JhiLanguageService, $translate, Account) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.saveImage = saveImage;
        Account.getImage(function (data) {
            vm.image = data.image;
        });
        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function (account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save() {
            Auth.updateAccount(vm.settingsAccount).then(function () {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function (account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function (current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        function saveImage() {
            if (vm.newImage) {
                Account.saveImage(vm.newImage, function (data) {
                    vm.image = data.image;
                    vm.newImage={};
                }, function (err) {
                    console.log(err);
                });
            }
        }
    }
})();
