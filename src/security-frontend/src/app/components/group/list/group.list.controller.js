(function () {
	'use strict';

	angular
			.module('crm.group')
			.controller('GroupsListController', GroupsListController);

	function GroupsListController($q, groupService, groupSearch, dialogService, $state) {
		var vm = this;

		vm.bundle = groupSearch.securedMode();
		vm.add = add;
		vm.edit = edit;
		vm.remove = remove;

		init();

		function init() {
			vm.bundle.find();
		}

		function add() {
			$state.go('groups.add');
		}

		function edit(group) {
			$state.go('groups.edit', {id: group.id});
		}

    vm.remove = function () {
      dialogService.confirm('You sure want to delete this group?').result.then(function (answer) {
        var checkedGroups = vm.bundle.pageGroups.filter(function (group) {
          return group.checked;
        });
        if (answer) {
          $q.all(
            checkedGroups.map(function (group) {
              return groupService.remove(group.id);
            })
          ).then(vm.bundle.find, function () {
              dialogService.error('Occurred an error during removing groups');
            });
        }
      });
    };
  }
})();
