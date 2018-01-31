/**
 * 页面总入口，定义依赖的第三方库，定义共有方法，变量
 * 
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp', ['ui.router', 'ui.router.state.events','perfect_scrollbar']).run(function ($rootScope, $state, $timeout) {
    $rootScope.$state = $state;
    $rootScope.progressNum = 0;
    
    $rootScope.menuItems=[
      { path: 'home.stock', title: 'Dashboard',  icon: 'ti-panel', class: '' },
      { path: 'test', title: 'ceshi',  icon:'ti-bell', children:[
        { path: 'home.stock', title: 'Dashboard',  icon: 'ti-panel', class: '' },
        { path: 'test.stock2', title: 'Dashboard2',  icon: 'ti-panel', class: '' }
      ]},
      { path: 'test2', title: 'ceshi',  icon:'ti-bell', children:[
        { path: 'home.stock', title: 'Dashboard',  icon: 'ti-panel', class: '' },
        { path: 'test.stock2', title: 'Dashboard2',  icon: 'ti-panel', class: '' }
      ]},
      { path: 'home.stock2', title: 'User Profile',  icon:'ti-user', class: '' },
      { path: 'table', title: 'Table List',  icon:'ti-view-list-alt', class: '' },
      { path: 'typography', title: 'Typography',  icon:'ti-text', class: '' },
      { path: 'icons', title: 'Icons',  icon:'ti-pencil-alt2', class: '' },
      { path: 'maps', title: 'Maps',  icon:'ti-map', class: '' },
      { path: 'notifications', title: 'Notifications',  icon:'ti-bell', class: '' }
    ]
    
    $rootScope.menuTitle=null;
    
    $rootScope.$on('$stateChangeSuccess', function(toState,toParams,fromState){
      var statePath = toParams.name;
      for (var index in $rootScope.menuItems) { 
        if($rootScope.menuItems[index].path === statePath){
          $rootScope.menuTitle = $rootScope.menuItems[index].title;
          break;
        }
      }
    });
    
    
    
  });

})();
