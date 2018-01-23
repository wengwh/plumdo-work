/**
 * 时间指令，由于第三方的时间使用默认按钮，input不会触发，自定义通过事件触发
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp').directive('viewLoad', function () {
    return {
      restrict: 'A',
      templateUrl: 'views/common/view-load.html',
      link: function (scope, element) {
        $(element).fadeIn(300);
      }
    };
  }).directive('fixedPlugin', function () {
    return {
      restrict: 'A',
      templateUrl: 'views/common/fixedplugin.component.html',
      link: function (scope, element) {
      	var $sidebar = $('.sidebar');
        var $off_canvas_sidebar = $('.off-canvas-sidebar');
        var window_width = $(window).width();

        if(window_width > 767){
            if($('.fixed-plugin .dropdown').hasClass('show-dropdown')){
                $('.fixed-plugin .dropdown').addClass('open');
            }

        }

        $('.fixed-plugin a').click(function(event){
          // Alex if we click on switch, stop propagation of the event, so the dropdown will not be hide, otherwise we set the  section active
            if($(this).hasClass('switch-trigger')){
                if(event.stopPropagation){
                    event.stopPropagation();
                }
                else if(window.event){
                   window.event.cancelBubble = true;
                }
            }
        });

        $('.fixed-plugin .background-color span').click(function(){
            $(this).siblings().removeClass('active');
            $(this).addClass('active');

            var new_color = $(this).data('color');

            if($sidebar.length != 0){
                $sidebar.attr('data-background-color',new_color);
            }

            if($off_canvas_sidebar.length != 0){
                $off_canvas_sidebar.attr('data-background-color',new_color);
            }
        });

        $('.fixed-plugin .active-color span').click(function(){
            $(this).siblings().removeClass('active');
            $(this).addClass('active');

            var new_color = $(this).data('color');

            if($sidebar.length != 0){
                $sidebar.attr('data-active-color',new_color);
            }

            if($off_canvas_sidebar.length != 0){
                $off_canvas_sidebar.attr('data-active-color',new_color);
            }
        });
      }
    };
  });

})();
