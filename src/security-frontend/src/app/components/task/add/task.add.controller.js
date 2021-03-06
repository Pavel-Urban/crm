/**
 * @author yauheni.putsykovich
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskAddController', TaskAddController);

    /** @ngInject */
    function TaskAddController(taskCommonService, authService, util) {
        var vm = this;

        vm.canEdit = true;
        vm.timeless = false;
        vm.title = 'Add Task';
        vm.submitText = 'Add';

        (function () {
            taskCommonService.initContext(vm).then(function () {
                var currentUsername = authService.getUserName();
                var currentUser = vm.assigns.find(function (item) {
                    return currentUsername === item.userName;
                });

                var startDate = util.getDateTrimMinutes();
                var endDate = new Date(startDate);
                endDate.setHours(endDate.getHours() + 1);

                vm.task = {
                    startDate: startDate,
                    endDate: endDate,
                    status: vm.statuses[0],// default is 'New', TODO: need to add some resolver
                    priority: vm.priorities[1],// default is 'Normal', TODO: need to add some resolver
                    assignee: currentUser
                };

                vm.aclHandler = taskCommonService.createAclHandler(function () {
                    return vm.task.id;
                });
                vm.aclHandler.canEdit = vm.canEdit;

                vm.canEditDateTime = vm.canEdit;
            });
        })();
    }
})();
