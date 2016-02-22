/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("GroupsController", ["GroupService",
    function (GroupService) {
        "use strict";
        var vm = this;
        GroupService.fetchAll().then(function (response) {
            vm.groupList = response.data;
        });
        vm.partials = {
            groupModalView: "/app/group/group.modal.view.html"
        };
        //vm.group - it's transfer object to pass data to modal and back
        vm.edit = function (group) {
            vm.group = {};
            angular.copy(group, vm.group);
            vm.viewTitle = "Editing Group";
            vm.actionTitle = "Update";
            vm.action = update;
        };
        vm.create = function () {
            vm.group = {};
            vm.viewTitle = "Create a New Group";
            vm.actionTitle = "Add";
            vm.action = add;
        };

        function add(group) {
            GroupService.create(group, function (response) {
                group.id = response.data;
                vm.groupList.push(group);
            });
            closeModalView();
        }

        function update(group) {
            var originGroup = vm.groupList.find(function (g) {
                return g.id === group.id
            });
            angular.copy(group, originGroup);
            GroupService.update(group, function () {
                
            });
            closeModalView();
        }

        function closeModalView() {
            $("#modal-close-button").trigger("click");//todo: need improve(find other solution)
        }
    }]);