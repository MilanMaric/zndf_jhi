(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('StatisticsController', StatisticsController);

    StatisticsController.$inject = ['StatisticsService'];

    function StatisticsController(StatisticsService) {
        var vm = this;
        vm.getFilms = getFilms;
        vm.k = 1;
        vm.loadNextPage = loadNextPage;

        function getFilms(k) {
            vm.page = 0;
            vm.more = false;
            vm.k = k;
            if (k == 1) {
                StatisticsService.byRate({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    g: vm.mGenre
                }, onSuccess, onError);
            } else {
                StatisticsService.byPopularity({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    g: vm.mGenre
                }, onSuccess, onError);
            }
        }

        function onError() {
            vm.page = 0;
        }

        function onSuccess(data, headers) {
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.films = data;
            vm.more = true;
        }

        function loadNextPage() {
            vm.page++;
            vm.more = false;
            if (vm.k == 1) {
                StatisticsService.byRate({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    g: vm.mGenre
                }, nextPageSuccess, nextPageError);
            } else {
                StatisticsService.byPopularity({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    g: vm.mGenre
                }, nextPageSuccess, nextPageError);
            }
        }

        function nextPageSuccess(data) {
            if (data && data.length > 0) {
                if (vm.films)
                    vm.films = vm.films.concat(data);
                else
                    vm.films = data;
                vm.more = true;
            }
        }

        function nextPageError() {
            vm.more = false;
            vm.page--;
        }

    }
})();
