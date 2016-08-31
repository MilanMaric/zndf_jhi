(function () {
    'use strict';

    angular
            .module('zndfApp', [
                'ngStorage',
                'tmh.dynamicLocale',
                'pascalprecht.translate',
                'ngResource',
                'ngCookies',
                'ngAria',
                'ngCacheBuster',
                'ngFileUpload',
                'ui.bootstrap',
                'ui.bootstrap.datetimepicker',
                'ui.router',
                'infinite-scroll',
                'angular-loading-bar',
                'jkAngularRatingStars'
            ])
            .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
