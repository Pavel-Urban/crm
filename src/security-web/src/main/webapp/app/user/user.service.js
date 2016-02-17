(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];

    function UserService($http) {
        console.log('UserService');

        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.CreateUser = CreateUser;
        service.UpdateUser = UpdateUser;
        service.DeleteUser = DeleteUser;

        return service;


        function GetAll(handleSuccess) {
            return $http.get('rest/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function GetById(id, handleSuccess) {
            return $http.get('rest/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function GetByUsername(name, handleSuccess) {
            return $http.get('rest/users/' + name).then(handleSuccess, handleError('Error getting user by username'));
        }

        function CreateUser(user, handleSuccess) {
            return $http.post('rest/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function UpdateUser(user, handleSuccess) {
            return $http.put('rest/users', user).then(handleSuccess, handleError('Error updating user'));
        }

        function DeleteUser(id) {
            return $http.delete('api/users' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return {
                    success: false,
                    message: error
                };
            };
        }
    }
})();