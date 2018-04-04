/**
 * 编辑框控制
 * 
 * @author wengwh
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('EditModalController',
		function($scope, $uibModalInstance, id, service, complete, title, url, data) {
			$scope.formUrl = url;
			$scope.formData = data || {};
			if (id) {
				$scope.modalTitle = "修改" + title;

				service.get({
					urlPath : '/' + id
				}, function(data, status) {
					$scope.formData = angular.extend($scope.formData, data);
				});

				$scope.ok = function() {
					service.put({
						urlPath : '/' + id,
						data : $scope.formData
					}, function(data, status) {
						$uibModalInstance.close();
						$scope.showSuccessMsg('修改' + title + '成功');
						complete();
					});
				};

			} else {
				$scope.modalTitle = '添加' + title;
				$scope.ok = function() {
					service.post({
						data : $scope.formData
					}, function(data, status) {
						$uibModalInstance.close();
						$scope.showSuccessMsg('添加' + title + '成功');
						complete();
					});
				};
			}

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});

})();
