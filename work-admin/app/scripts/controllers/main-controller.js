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
    
  }]);
})();
