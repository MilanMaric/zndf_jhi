(function () {
    'use strict';

    angular
            .module('zndfApp')
            .controller('FilmsDetailController', FilmsDetailController);

    FilmsDetailController.$inject = ['$stateParams', 'Film','Comment'];

    function FilmsDetailController($stateParams, Film,Comment) {
        var vm = this;

        vm.load = load;
        vm.film = {};
        vm.newComment = {};
        vm.load($stateParams.id);
        vm.loadComments = loadComments;
        vm.saveComment = saveComment;
        
        function load(id) {
            Film.get({id: id}, function (result) {
                vm.film = result;
                vm.loadComments(id);
            });
        }

        function loadComments(id) {
            vm.comments = Film.getComments({id: id});
        }

        function saveComment() {
            console.log($stateParams.id);
            Comment.save({id: $stateParams.id},vm.newComment, onCommentSuccess);
        }

        function onCommentSuccess(data) {
            vm.comments.push(data);
            vm.newComment = {};
        }
    }
})();
