(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('util', util);

	/** @ngInject */
	function util($timeout) {
		return {
			createDelayTypingListener: createDelayTypingListener
		};

		function createDelayTypingListener(action, delay) {
			return (function () {
				var timer;

				return {
					keyDown: function () {
						$timeout.cancel(timer);
					},
					keyUp: function () {
						$timeout.cancel(timer);
						timer = $timeout(action, delay);
					}
				}
			})();
		}
	}
})();
