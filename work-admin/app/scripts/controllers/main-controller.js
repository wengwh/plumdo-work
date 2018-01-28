/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('MainController', [ '$scope','$timeout', function($scope,$timeout) {
    $scope.sidebarBackground='white';
    $scope.switchSidebarBackground = function(color) {
      $scope.sidebarBackground = color;
    };
    
    $scope.sidebarActiveColor='danger';
    $scope.switchSidebarActiveColor = function(color) {
      $scope.sidebarActiveColor = color;
    };
    
    $scope.menuItems=[
      { path: 'home.stock', title: 'Dashboard',  icon: 'ti-panel', class: 'active' },
      { path: 'home.stock2', title: 'User Profile',  icon:'ti-user', class: '' },
      { path: 'table', title: 'Table List',  icon:'ti-view-list-alt', class: '' },
      { path: 'typography', title: 'Typography',  icon:'ti-text', class: '' },
      { path: 'icons', title: 'Icons',  icon:'ti-pencil-alt2', class: '' },
      { path: 'maps', title: 'Maps',  icon:'ti-map', class: '' },
      { path: 'notifications', title: 'Notifications',  icon:'ti-bell', class: '' }
    ]
    
   
    $scope.changeNotMobileMenu = function(){
      if($(window).width() > 991){
        $scope.isNotMobileMenu = false; 
      }else{
        $scope.isNotMobileMenu = true; 
      }
    };
    
    angular.element(window).bind('resize', function() {
      $timeout(function() {
        $scope.changeNotMobileMenu();
      });
    });
   
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
  }]);
})();
