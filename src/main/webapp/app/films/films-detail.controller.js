(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('FilmsDetailController', FilmsDetailController);

    FilmsDetailController.$inject = ['$stateParams', 'Film', 'Comment', '$sce'];

    function FilmsDetailController($stateParams, Film, Comment, $sce) {
        var vm = this;

        vm.load = load;
        vm.film = {};
        vm.newComment = {};
        vm.load($stateParams.id);
        vm.loadComments = loadComments;
        vm.saveComment = saveComment;
        vm.saveTrailer = saveTrailer;
        vm.loadTrailers = loadTrailers;
        vm.getTrailerEmbeddedLink = getTrailerEmbeddedLink;
        vm.checkFavorite = checkFavorite;
        vm.setFavorite = setFavorite;
        vm.getRate = getRate;
        vm.setRate = setRate;

        checkFavorite();
        getRate();

        function load(id) {
            Film.get({id: id}, function (result) {
                vm.film = result;
                vm.loadComments(id);
                vm.loadTrailers(id);
            });
        }

        function loadComments(id) {
            vm.comments = Film.getComments({id: id});
        }

        function loadTrailers(id) {
            vm.trailers = Film.getTrailers({id: id});
        }

        function saveComment() {
            Comment.save({id: $stateParams.id}, vm.newComment, onCommentSuccess);
        }

        function onCommentSuccess(data) {
            vm.comments.push(data);
            vm.newComment = {};
        }

        function saveTrailer() {
            Film.saveTrailer({id: $stateParams.id}, vm.newTrailer, onTrailerSuccess);
        }

        function onTrailerSuccess(data) {
            vm.trailers.push(data);
            vm.newTrailer = {};
        }


        function getTrailerEmbeddedLink(trailer)
        {
            if (trailer && trailer.uri) {
                var url = trailer.uri;
                var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=|\?v=)([^#\&\?]*).*/;
                var match = url.match(regExp);
                if (match && match[2].length == 11) {
                    return $sce.trustAsResourceUrl('https://www.youtube.com/embed/' + match[2] + '?autoplay=0');
                } else {
                    return null;
                }
            }
        }

        function checkFavorite() {
            Film.checkFavorite({id: $stateParams.id}, function (data) {
                console.log(data);
                vm.favorite = data;
            });
        }

        function setFavorite() {
            if (!vm.favorite) {
                vm.favorite = Film.setFavorite({id: $stateParams.id});
            }
        }

        function getRate() {
            vm.rate = Film.checkRate({id: $stateParams.id});
        }

        function setRate() {
            if(vm.rate.rate==0)
                vm.rate.rate=1;
            vm.rate = Film.setRate({id: $stateParams.id}, vm.rate);
        }
    }
})();
