(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Film', 'ParseLinks', 'AlertService', 'OMDB', 'Account'];

    function HomeController($scope, Principal, LoginService, $state, Film, ParseLinks, AlertService, OMDB, Account) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.feedsPage = 1;
        vm.itemsPerPage = 2;
        vm.films = [];
        vm.feed = {};
        vm.register = register;


        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        vm.onSearchChange = onSearchChange;
        vm.getFeeds = getFeeds;
        vm.loadNextPage = loadNextPage;
        vm.getFeeds(0);
        getAccount();
        vm.getFilms = getFilms;
        getFavorites();
        getFilms();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                Account.getImage(function (data) {
                    if (data) {
                        vm.image = data.image;
                    }
                });
            });
        }
        function register() {
            $state.go('register');
        }

        function getFilms() {
            vm.page = 0;
            vm.more = false;
            Film.query({
                page: vm.page,
                size: vm.itemsPerPage,
                s: vm.mSearch,
                g: vm.mGenre,
                sort: "rate,desc"
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.films = data;
            vm.more = true;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }



        function getFeeds() {
            var feed = new google.feeds.Feed("http://dvd.netflix.com/Top100RSS");
            feed.setNumEntries(vm.feedsPage * 20);
            console.log(vm.feedsPage);
            feed.load(function (result) {
                if (vm.feedsPage > 1) {
                    result.feed.entries.splice(0, (vm.feedsPage - 1) * 20);
                }
                vm.feed = result.feed;
            });

        }
//for OMDB api
        function onSearchChange() {
            vm.searchResults = OMDB.get({
                s: vm.search,
                page: 1
            });
        }

        function loadNextPage() {
            vm.page++;
            vm.more = false;
            Film.query({
                page: vm.page,
                size: vm.itemsPerPage,
                s: vm.mSearch,
                g: vm.mGenre,
                sort: "rate,desc"
            }, nextPageSuccess, nextPageError);
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

        function getFavorites() {
            vm.favorites = Film.getFavorites();
        }
    }
})();
