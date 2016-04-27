/**
 * Created by anton.charnou on 11.04.2016.
 */
(function () {
  'use strict';

  angular
    .module('securityManagement')
    .controller('UsersEditController', UserEditController);


  /** @ngInject */
  function UserEditController(UserService, GroupService, RoleService, AclDialogServiceBuilder, AclServiceBuilder, $state, Collections, $stateParams, $q) {
    'use strict';
    var vm = this;

    vm.submitText = 'Save';
    vm.cancelText = 'Cancel';
    vm.title = 'Edit user';

    vm.user = {active: 'true'};

    vm.gtoups = [];
    vm.roles = [];
    vm.aclHandler = {
      canEdit: true,
      acls: [],
      actions: AclDialogServiceBuilder(AclServiceBuilder(getId, UserService))
    };

    $q.all(
      [
        GroupService.getAll().then(function (response) {
          vm.groups = response.data;
        }),
        RoleService.fetchAll().then(function (response) {
          vm.roles = response.data;
        })
      ]
    ).then(function () {
        UserService.getById($stateParams.id).then(function (response) {
          vm.user = response.data;
          checkGroupsAndRolesWhichUserHas(vm.user);
          UserService.getAcls(getId()).then(function (response) {
            vm.aclHandler.acls = response.data;
          });
        });
      });

    vm.submit = function () {
      checkGroups(vm.user);
      checkRoles(vm.user);
      vm.user.acls = vm.aclHandler.acls;
      UserService.update(vm.user).then(function () {
        $state.go('users.list');
      })
    };

    vm.cancel = function () {
      $state.go('users.list');
    };

    function getId() {
      return vm.user.id;
    }

    function checkGroups(user) {
      user.groups = vm.groups.filter(function (group) {
        return group.checked;
      });
    }

    function checkRoles(user) {
      user.roles = vm.roles.filter(function (role) {
        return role.checked;
      });
    }

    function checkGroupsAndRolesWhichUserHas(user) {
      vm.groups.forEach(function (group) {
        group.checked = !!Collections.find(group, user.groups);
      });
      vm.roles.forEach(function (role) {
        role.checked = !!Collections.find(role, user.roles);
      });
    }


  }
})();
