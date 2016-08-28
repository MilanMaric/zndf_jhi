(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Film', 'ParseLinks', 'AlertService', 'OMDB'];

    function HomeController($scope, Principal, LoginService, $state, Film, ParseLinks, AlertService, OMDB) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.feedsPage = 1;
        vm.itemsPerPage = 5;
        vm.films = [];
        vm.feed = {};
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        vm.onSearchChange = onSearchChange;
        vm.getFeeds = getFeeds;
        vm.getFeeds(0);
        vm.transition = transition;
        getAccount();
        getFilms();
        getFavorites();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register() {
            $state.go('register');
        }

        function getFilms() {
            Film.query({
                page: vm.page,
                size: vm.itemsPerPage
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.films = data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }


        function transition() {
            vm.page = vm.page++;
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

        function onSearchChange() {
            vm.searchResults = OMDB.get({
                s: vm.search,
                page: 1
            });
        }
        
        function getFavorites(){
            vm.favorites=Film.getFavorites();
        }
    }
})();
