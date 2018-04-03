/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('MainController', [ '$scope','$timeout','$window', function($scope,$timeout,$window) {
    $scope.authService = $scope.IdmService($scope.restUrl.auths);
    $scope.userName = $window.localStorage.userName;
    $scope.userAvatar = $window.localStorage.userAvatar;
    
    $scope.sidebarBackground='black';
    $scope.switchSidebarBackground = function(color) {
      $scope.sidebarBackground = color;
    };
    
    $scope.sidebarActiveColor='primary';
    $scope.switchSidebarActiveColor = function(color) {
      $scope.sidebarActiveColor = color;
    };
    
    $scope.changeNotMobileMenu = function(){
      $timeout(function() {
        if($(window).width() > 991){
          $scope.isNotMobileMenu = false; 
        }else{
          $scope.isNotMobileMenu = true; 
        }
        jQuery('.scroller').css('max-height',$(window).height()-75);
      });
    };
    
    angular.element(window).bind('resize', function() {
      $scope.changeNotMobileMenu();
    });
    
    $scope.$on('$viewContentLoaded', function() {  
      $scope.changeNotMobileMenu();
    });
    
    $scope.changeNotMobileMenu();
    
    $scope.sidebarVisible = false;
    $scope.sidebarToggle= function(){
      var toggleButton = document.getElementsByClassName('navbar-toggle')[0]
      var body = document.getElementsByTagName('body')[0];
      if($scope.sidebarVisible == false){
        $timeout(function(){
            toggleButton.classList.add('toggled');
        },500);
        body.classList.add('nav-open');
        $scope.sidebarVisible = true;
      } else {
        toggleButton.classList.remove('toggled');
        $scope.sidebarVisible = false;
        body.classList.remove('nav-open');
      }
    };
    
    $scope.signOut= function(){
    	$window.localStorage.token = null;
    	$scope.$state.go('login');
    };

    $scope.authService.get({
      urlPath : '/menus'
    }, function(response) {
      $scope.menuItems = response;
    });
    
    /*$scope.menuItems=[
      { path: 'home', title: '系统首页',  icon: 'ti-panel'},
      { path: 'work', title: '我的工作台',  icon:'ti-bell',
        children:[
          { path: 'work.process-start', title: '发起新流程',  icon: 'ti-user'},
          { path: 'work.process-run', title: '运行的流程',  icon: 'ti-microsoft-alt'},
          { path: 'work.process-finish', title: '办结的流程',  icon: 'ti-menu'},
          { path: 'work.process-involve', title: '参与的流程',  icon: 'ti-ruler-pencil'},
          { path: 'work.task-todo', title: '待办任务',  icon: 'ti-pencil-alt2'},
          { path: 'work.task-finish', title: '已办任务',  icon: 'ti-map'},
        ]
      },
      { path: 'modeler', title: '设计器管理',  icon:'ti-pencil-alt2', 
        children:[
          { path: 'modeler.form', title: '表单设计器',  icon: 'ti-layout-grid4'},
          { path: 'modeler.button', title: '按钮设计器',  icon: 'ti-credit-card'},
          { path: 'modeler.flow', title: '流程设计器',  icon: 'ti-layout-media-overlay-alt-2'},
          { path: 'modeler.report', title: '报表设计器',  icon: 'ti-layout-cta-left'},
          { path: 'modeler.app', title: '模块设计器',  icon: 'ti-view-list-alt'}
        ]
      },
      { path: 'flow', title: '流程后台管理',  icon:'ti-blackboard', 
        children:[
          { path: 'flow.definition', title: '流程定义管理',  icon: 'ti-package'},
          { path: 'flow.instance', title: '流程实例管理',  icon: 'ti-menu'},
          { path: 'flow.task', title: '流程任务管理',  icon: 'ti-server'},
          { path: 'flow.form', title: '表单定义管理',  icon: 'ti-text'}
        ]
      },
      { path: 'main.idm', id:'1', title: '人员权限管理',  icon:'ti-agenda',
        children:[
          { path: 'main.idm.user', title: '人员管理',  icon: 'ti-user'},
          { path: 'main.idm.group', title: '部门管理',  icon: 'ti-microsoft-alt'},
          { path: 'main.idm.menu', title: '菜单管理',  icon: 'ti-menu'},
          { path: 'main.idm.role', title: '角色管理',  icon: 'ti-ruler-pencil'}
        ]
      }
    ]*/

    $scope.menuTitle = null;
    $scope.$on('$stateChangeSuccess', function(toState, toParams) {
      var statePath = toParams.name;
      var pathArray = statePath.split('.');
      for (var index in $scope.menuItems) {
        var item = $scope.menuItems[index];
        if (item.path === pathArray[0]+'.'+pathArray[1]) {
          $scope.menuTitle = item.name;
          if(item.children && item.children.length>0){
            for (var child in item.children) {
              var childItem = item.children[child];
              if (childItem.path === statePath) {
                $scope.menuTitle = childItem.name;
                break;
              }
            }
          }
          break;
        }
      }
    });
  }]);
})();
