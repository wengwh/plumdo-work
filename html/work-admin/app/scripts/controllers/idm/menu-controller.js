/**
 * 菜单控制器
 * 
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('MenuController',
    function($scope,$stateParams,$q) {
      $scope.menuService = $scope.IdmService($scope.restUrl.idmMenus);
      $scope.parentId = parseInt($stateParams.id || 0);
      $scope.queryParams = $scope.parentId===0 ? $scope.getCacheParams():{};
      $scope.queryParams.parentId = $scope.parentId;
      $scope.parentMenu = {id:$scope.parentId};
      $scope.queryResult = {};
      
      var queryPromise = null;
      if($scope.parentId !== 0){
        queryPromise = $scope.menuService.get({
          urlPath : '/'+$scope.parentId
        }, function(response) {
            $scope.parentMenu = response;
        });
      }
      
      $scope.queryMenu = function() {
        $scope.menuService.get({
          params : $scope.queryParams
        }, function(response) {
            $scope.queryResult = response;
        });
      };

      $scope.deleteMenu = function(id) {
        $scope.confirmModal({
          title : '确认删除菜单',
          confirm : function() {
            $scope.menuService.delete({
              urlPath : '/' + id
            }, function() {
              $scope.showSuccessMsg('删除菜单成功');
              $scope.queryMenu();
            });
          }
        });
      };

      $scope.editMenu = function(id) {
        $scope.editModal({
            id : id,
            formUrl : 'menu-edit.html',
            title : '菜单',
            formData : (function() {
              if($scope.parentMenu.id!==0){
                return {type:1, parentId:$scope.parentMenu.id, parentName:$scope.parentMenu.name};
              }else{
                return {type:0, parentId:0};
              }
            })(),
            service : $scope.menuService,
            complete : $scope.queryMenu
        });
      };
      
      $scope.switchStaus = function(id){
        $scope.menuService.put({
          urlPath : '/' + id +'/switch'
        }, function() {
          $scope.showSuccessMsg('修改状态成功');
          $scope.queryMenu();
        });
      };
      
      $scope.queryMenuRole = function(id) {
        $scope.tableModal({
          service : $scope.IdmService($scope.restUrl.idmMenus+'/'+id+'/roles'),
          colModels : [
            {name:'名称',index:'name'},
            {name:'状态',index:'status',
              formatter:function(){
                return '<span class="label label-success" ng-if="row.status==0">启用</span>'+
                   '<span class="label label-danger" ng-if="row.status==1">停用</span>';
              }
            }
          ]
        });
      };
      
      $scope.tableOptions = {
        id : 'menu',
        data : 'queryResult',
        colModels : [
          {name:'名称',index:'name',sortable:true,width:'10%'},
          {name:'路径',index:'route',sortable:true,width:'9%'},
          {name:'图标',index:'icon',width:'5%',
            formatter:function(){
              return '<div class="th-icon"><span class="{{\'fa \'+row.icon}}"></span></div>';
            }
          },
          {name:'状态',index:'status',sortable:true,width:'10%',
            formatter:function(){
              return '<toggle-switch ng-init="switch=(row.status==0)" ng-model="switch" class="switch-small switch-info" '+
                'on-label="启用" off-label="停用" on-change="switchStaus(row.id)"></toggle-switch>';
            }
          },
          {name:'排序',index:'order',sortable:true,width:'5%'},
          {name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
          {name:'操作',index:'',width:'15%',
            formatter:function(){
              return '<div class="th-btn-group">'+
                '<button type="button" class="btn btn-xs btn-info" ng-click=editMenu(row.id)>'+
                '<i class="fa fa-pencil"></i>&nbsp;编辑</button>'+
                '<button type="button" class="btn btn-xs btn-success" ng-if="row.type==0" ng-click=gotoDetail(row.id)>'+
                '<i class="fa fa-list"></i>&nbsp;下级</button>'+
                '<button type="button" class="btn btn-xs btn-success" ng-if="row.type!=0" ng-click=queryMenuRole(row.id)>'+
                '<i class="fa fa-list"></i>&nbsp;关联角色</button>'+
                '<button type="button" class="btn btn-xs btn-danger" ng-click=deleteMenu(row.id)>'+
                '<i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
                '</div>';
            }
          }
        ],
        loadFunction : $scope.queryMenu,
        queryParams : $scope.queryParams,
        sortName : 'order',
        sortOrder : 'asc'
      };

      $q.all([queryPromise]).then(function() {  
        $scope.queryMenu();
     });
  });

})();
