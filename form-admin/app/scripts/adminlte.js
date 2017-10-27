/*! AdminLTE app.js
 * ================
 * Main JS application file for AdminLTE v2. This file
 * should be included in all pages. It controls some layout
 * options and implements exclusive AdminLTE plugins.
 *
 * @Author  Almsaeed Studio
 * @Support <https://www.almsaeedstudio.com>
 * @Email   <abdullah@almsaeedstudio.com>
 * @version 2.4.0
 * @repository git://github.com/almasaeed2010/AdminLTE.git
 * @license MIT <http://opensource.org/licenses/MIT>
 */

// Make sure jQuery has been loaded
if (typeof jQuery === 'undefined') {
  throw new Error('AdminLTE requires jQuery')
}

/* Layout()
 * ========
 * Implements AdminLTE layout.
 * Fixes the layout height in case min-height fails.
 *
 * @usage activated automatically upon window load.
 *        Configure any options by passing data-option="value"
 *        to the body tag.
 */
+function ($) {
  'use strict'

  var DataKey = 'lte.layout'

  var Default = {
    slimscroll: true,
    resetHeight: true
  }

  var Selector = {
    wrapper: '.wrapper',
    contentWrapper: '.content-wrapper',
    layoutBoxed: '.layout-boxed',
    mainFooter: '.main-footer',
    mainHeader: '.main-header',
    sidebar: '.sidebar',
    controlSidebar: '.control-sidebar',
    fixed: '.fixed',
    sidebarMenu: '.sidebar-menu'
  }

  var ClassName = {
    fixed: 'fixed',
    holdTransition: 'hold-transition'
  }

  var Layout = function (options) {
    this.options = options
    this.bindedResize = false
    this.activate()
  }

  Layout.prototype.activate = function () {
    this.fix()
    this.fixSidebar()

    $('body').removeClass(ClassName.holdTransition)

    if (this.options.resetHeight) {
      $('body, html, ' + Selector.wrapper).css({
        'height': 'auto',
        'min-height': '100%'
      })
    }

    if (!this.bindedResize) {
      $(window).resize(function () {
        this.fix()
        this.fixSidebar()
      }.bind(this))
      this.bindedResize = true
    }

    $(Selector.sidebarMenu).on('expanded.tree', function () {
      this.fix()
      this.fixSidebar()
    }.bind(this))

    $(Selector.sidebarMenu).on('collapsed.tree', function () {
      this.fix()
      this.fixSidebar()
    }.bind(this))

    $(Selector.controlSidebar).on('tabShown.controlsidebar', function () {
      this.fix()
      this.fixSidebar()
    }.bind(this))

    $(Selector.controlSidebar).on('changeLayout.controlsidebar', function () {
      this.fixSidebar()
    }.bind(this))

    $(Selector.controlSidebar).on('fixedLayout.controlsidebar', function () {
      this.activate()
    }.bind(this))

  }

  Layout.prototype.fix = function () {
    // Remove overflow from .wrapper if layout-boxed exists
    $(Selector.layoutBoxed + ' > ' + Selector.wrapper).css('overflow', 'hidden')

    // Get window height and the wrapper height
    var footerHeight = $(Selector.mainFooter).outerHeight() || 0
    var neg = $(Selector.mainHeader).outerHeight() + footerHeight
    var windowHeight = $(window).height()
    var sidebarHeight = $(Selector.sidebar).height() || 0

    // Set the min-height of the content and sidebar based on the
    // the height of the document.
    if ($('body').hasClass(ClassName.fixed)) {
      $(Selector.contentWrapper).css('min-height', windowHeight - footerHeight)
    } else {
      var postSetWidth

      if (windowHeight >= sidebarHeight) {
        $(Selector.contentWrapper).css('min-height', windowHeight - neg)
        postSetWidth = windowHeight - neg
      } else {
        $(Selector.contentWrapper).css('min-height', sidebarHeight)
        postSetWidth = sidebarHeight
      }

      // Fix for the control sidebar height
      var $controlSidebar = $(Selector.controlSidebar)
      if (typeof $controlSidebar !== 'undefined') {
        if ($controlSidebar.height() > postSetWidth)
          $(Selector.contentWrapper).css('min-height', $controlSidebar.height())
      }
    }
  }

  Layout.prototype.fixSidebar = function () {
    // Make sure the body tag has the .fixed class
    if (!$('body').hasClass(ClassName.fixed)) {
      if (typeof $.fn.slimScroll !== 'undefined') {
        $(Selector.sidebar).slimScroll({destroy: true}).height('auto')
      }
      return
    }

    // Enable slimscroll for fixed layout
    if (this.options.slimscroll) {
      if (typeof $.fn.slimScroll !== 'undefined') {
        // Destroy if it exists
        $(Selector.sidebar).slimScroll({destroy: true}).height('auto')

        // Add slimscroll
        $(Selector.sidebar).slimScroll({
          height: ($(window).height() - $(Selector.mainHeader).height()) + 'px',
          color: 'rgba(0,0,0,0.2)',
          size: '3px'
        })
      }
    }
  }

  // Plugin Definition
  // =================
  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data = $this.data(DataKey)

      if (!data) {
        var options = $.extend({}, Default, $this.data(), typeof option === 'object' && option)
        $this.data(DataKey, (data = new Layout(options)))
      }

      if (typeof option === 'string') {
        if (typeof data[option] === 'undefined') {
          throw new Error('No method named ' + option)
        }
        data[option]()
      }
    })
  }

  var old = $.fn.layout

  $.fn.layout = Plugin
  $.fn.layout.Constuctor = Layout

  // No conflict mode
  // ================
  $.fn.layout.noConflict = function () {
    $.fn.layout = old
    return this
  }

  // Layout DATA-API
  // ===============
  /* $(window).on('load', function () {
   Plugin.call($('body'))
   })*/
}(jQuery)


/* PushMenu()
 * ==========
 * Adds the push menu functionality to the sidebar.
 *
 * @usage: $('.btn').pushMenu(options)
 *          or add [data-toggle="push-menu"] to any toggle button
 *          Pass any option as data-option="value"
 */
+ function ($) {
  'use strict'

  var DataKey = 'lte.pushmenu'

  var Default = {
    collapseScreenSize: 767,
    expandOnHover: false,
    expandTransitionDelay: 200
  }

  var Selector = {
    collapsed: '.sidebar-collapse',
    open: '.sidebar-open',
    mainSidebar: '.main-sidebar',
    contentWrapper: '.content-wrapper',
    searchInput: '.sidebar-form .form-control',
    button: '[data-toggle="push-menu"]',
    mini: '.sidebar-mini',
    expanded: '.sidebar-expanded-on-hover',
    controlSidebar: '.control-sidebar',
    layoutFixed: '.fixed'
  }

  var ClassName = {
    collapsed: 'sidebar-collapse',
    open: 'sidebar-open',
    mini: 'sidebar-mini',
    expanded: 'sidebar-expanded-on-hover',
    expandFeature: 'sidebar-mini-expand-feature',
    layoutFixed: 'fixed'
  }

  var Event = {
    expanded: 'expanded.pushMenu',
    collapsed: 'collapsed.pushMenu'
  }

  // PushMenu Class Definition
  // =========================
  var PushMenu = function (options) {
    this.options = options
    this.init()
  }

  PushMenu.prototype.init = function () {
    if (this.options.expandOnHover
      || ($('body').is(Selector.mini + Selector.layoutFixed))) {
      this.expandOnHover()
      $('body').addClass(ClassName.expandFeature)
    }

    $(Selector.contentWrapper).click(function () {
      // Enable hide menu when clicking on the content-wrapper on small screens
      if ($(window).width() <= this.options.collapseScreenSize && $('body').hasClass(ClassName.open)) {
        this.close()
      }
    }.bind(this))

    // __Fix for android devices
    $(Selector.searchInput).click(function (e) {
      e.stopPropagation()
    })

    $(Selector.controlSidebar).on('fixedLayout.controlsidebar', function () {
      this.expandOnHover()
    }.bind(this))

    $(Selector.controlSidebar).on('expandOnHover.controlsidebar', function () {
      this.expandOnHover()
    }.bind(this))
  }

  PushMenu.prototype.toggle = function () {
    var windowWidth = $(window).width()
    var isOpen = !$('body').hasClass(ClassName.collapsed)

    if (windowWidth <= this.options.collapseScreenSize) {
      isOpen = $('body').hasClass(ClassName.open)
    }

    if (!isOpen) {
      this.open()
    } else {
      this.close()
    }
  }

  PushMenu.prototype.open = function () {
    var windowWidth = $(window).width()

    if (windowWidth > this.options.collapseScreenSize) {
      $('body').removeClass(ClassName.collapsed)
        .trigger($.Event(Event.expanded))
    }
    else {
      $('body').addClass(ClassName.open)
        .trigger($.Event(Event.expanded))
    }
  }

  PushMenu.prototype.close = function () {
    var windowWidth = $(window).width()
    if (windowWidth > this.options.collapseScreenSize) {
      $('body').addClass(ClassName.collapsed)
        .trigger($.Event(Event.collapsed))
    } else {
      $('body').removeClass(ClassName.open + ' ' + ClassName.collapsed)
        .trigger($.Event(Event.collapsed))
    }
  }

  PushMenu.prototype.expandOnHover = function () {
    $(Selector.mainSidebar).hover(function () {
      if ($('body').is(Selector.mini + Selector.collapsed)
        && $(window).width() > this.options.collapseScreenSize) {
        this.expand()
      }
    }.bind(this), function () {
      if ($('body').is(Selector.expanded)) {
        this.collapse()
      }
    }.bind(this))
  }

  PushMenu.prototype.expand = function () {
    setTimeout(function () {
      $('body').removeClass(ClassName.collapsed)
        .addClass(ClassName.expanded)
    }, this.options.expandTransitionDelay)
  }

  PushMenu.prototype.collapse = function () {
    setTimeout(function () {
      $('body').removeClass(ClassName.expanded)
        .addClass(ClassName.collapsed)
    }, this.options.expandTransitionDelay)
  }

  // PushMenu Plugin Definition
  // ==========================
  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data = $this.data(DataKey)

      if (!data) {
        var options = $.extend({}, Default, $this.data(), typeof option == 'object' && option)
        $this.data(DataKey, (data = new PushMenu(options)))
      }

      if (option == 'toggle') data.toggle()
    })
  }

  var old = $.fn.pushMenu

  $.fn.pushMenu = Plugin
  $.fn.pushMenu.Constructor = PushMenu

  // No Conflict Mode
  // ================
  $.fn.pushMenu.noConflict = function () {
    $.fn.pushMenu = old
    return this
  }

  // Data API
  // ========
  /* $(document).on('click', Selector.button, function (e) {
   e.preventDefault()
   Plugin.call($(this), 'toggle')
   })
   $(window).on('load', function () {
   Plugin.call($(Selector.button))
   })*/
}(jQuery)

/* Tree()
 * ======
 * Converts a nested list into a multilevel
 * tree view menu.
 *
 * @Usage: $('.my-menu').tree(options)
 *         or add [data-widget="tree"] to the ul element
 *         Pass any option as data-option="value"
 */
+ function ($) {
  'use strict'

  var DataKey = 'lte.tree'

  var Default = {
    animationSpeed: 500,
    accordion: true,
    followLink: false,
    trigger: '.treeview a'
  }

  var Selector = {
    tree: '.tree',
    treeview: '.treeview',
    treeviewMenu: '.treeview-menu',
    open: '.menu-open, .active',
    li: 'li',
    data: '[data-widget="tree"]',
    active: '.active'
  }

  var ClassName = {
    open: 'menu-open',
    tree: 'tree'
  }

  var Event = {
    collapsed: 'collapsed.tree',
    expanded: 'expanded.tree'
  }

  // Tree Class Definition
  // =====================
  var Tree = function (element, options) {
    this.element = element
    this.options = options

    $(this.element).addClass(ClassName.tree)

    $(Selector.treeview + Selector.active, this.element).addClass(ClassName.open)

    this._setUpListeners()
  }

  Tree.prototype.toggle = function (link, event) {
    var treeviewMenu = link.next(Selector.treeviewMenu)
    var parentLi = link.parent()
    var isOpen = parentLi.hasClass(ClassName.open)

    if (!parentLi.is(Selector.treeview)) {
      return
    }

    if (!this.options.followLink || link.attr('href') == '#') {
      event.preventDefault()
    }

    if (isOpen) {
      this.collapse(treeviewMenu, parentLi)
    } else {
      this.expand(treeviewMenu, parentLi)
    }
  }

  Tree.prototype.expand = function (tree, parent) {
    var expandedEvent = $.Event(Event.expanded)

    if (this.options.accordion) {
      var openMenuLi = parent.siblings(Selector.open)
      var openTree = openMenuLi.children(Selector.treeviewMenu)
      this.collapse(openTree, openMenuLi)
    }

    parent.addClass(ClassName.open)
    var treeElement = this.element;
    tree.slideDown(this.options.animationSpeed, function () {
      $(treeElement).trigger(expandedEvent)
    })
  }

  Tree.prototype.collapse = function (tree, parentLi) {
    var collapsedEvent = $.Event(Event.collapsed)

    tree.find(Selector.open).removeClass(ClassName.open)
    parentLi.removeClass(ClassName.open)
    var treeElement = this.element;
    tree.slideUp(this.options.animationSpeed, function () {
      tree.find(Selector.open + ' > ' + Selector.treeview).slideUp()
      $(treeElement).trigger(collapsedEvent)
    })
  }

  // Private

  Tree.prototype._setUpListeners = function () {
    var that = this

    $(this.element).on('click', this.options.trigger, function (event) {
      that.toggle($(this), event)
    })
  }

  // Plugin Definition
  // =================
  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data = $this.data(DataKey)

      if (!data) {
        var options = $.extend({}, Default, $this.data(), typeof option == 'object' && option)
        $this.data(DataKey, new Tree($this, options))
      }
    })
  }

  var old = $.fn.tree

  $.fn.tree = Plugin
  $.fn.tree.Constructor = Tree

  // No Conflict Mode
  // ================
  $.fn.tree.noConflict = function () {
    $.fn.tree = old
    return this
  }

  // Tree Data API
  // =============
  /* $(window).on('load', function () {
   $(Selector.data).each(function () {
   Plugin.call($(this))
   })
   })*/

}(jQuery)

/* ControlSidebar()
 * ===============
 * Toggles the state of the control sidebar
 *
 * @Usage: $('#control-sidebar-trigger').controlSidebar(options)
 *         or add [data-toggle="control-sidebar"] to the trigger
 *         Pass any option as data-option="value"
 */
+ function ($) {
  'use strict'

  var DataKey = 'lte.controlsidebar'

  var Default = {
    slide: true
  }

  var Selector = {
    sidebar: '.control-sidebar',
    data: '[data-toggle="control-sidebar"]',
    open: '.control-sidebar-open',
    bg: '.control-sidebar-bg',
    wrapper: '.wrapper',
    content: '.content-wrapper',
    boxed: '.layout-boxed',
    dataSidebar: '[data-layout="sidebar-collapse"]'
  }

  var ClassName = {
    open: 'control-sidebar-open',
    fixed: 'fixed',
    skinDark: 'control-sidebar-dark',
    skinLight: 'control-sidebar-light',
    collapsed: 'sidebar-collapse'
  }

  var Event = {
    collapsed: 'collapsed.controlsidebar',
    expanded: 'expanded.controlsidebar',
    tabShown: 'tabShown.controlsidebar',
    changeLayout: 'changeLayout.controlsidebar',
    fixedLayout: 'fixedLayout.controlsidebar',
    expandOnHover: 'expandOnHover.controlsidebar'
  }

  // ControlSidebar Class Definition
  // ===============================
  var ControlSidebar = function (element, options) {
    this.element = element
    this.options = options
    this.hasBindedResize = false

    this.init()
  }

  ControlSidebar.prototype.init = function () {
    // Add click listener if the element hasn't been
    // initialized using the data API
    if (!$(this.element).is(Selector.data)) {
      $(this).on('click', this.toggle)
    }

    this.fix()
    $(window).resize(function () {
      this.fix()
    }.bind(this))

    $(Selector.sidebar).find('[data-toggle="tab"]').on('shown.bs.tab', function () {
      $(Selector.sidebar).trigger($.Event(Event.tabShown))
    })
  }

  ControlSidebar.prototype.toggle = function (event) {
    if (event) event.preventDefault()

    this.fix()

    if (!$(Selector.sidebar).is(Selector.open) && !$('body').is(Selector.open)) {
      this.expand()
    } else {
      this.collapse()
    }
  }

  ControlSidebar.prototype.expand = function () {
    if (!this.options.slide) {
      $('body').addClass(ClassName.open)
    } else {
      $(Selector.sidebar).addClass(ClassName.open)
    }

    $(this.element).trigger($.Event(Event.expanded))
  }

  ControlSidebar.prototype.collapse = function () {
    $('body, ' + Selector.sidebar).removeClass(ClassName.open)
    $(this.element).trigger($.Event(Event.collapsed))
  }

  ControlSidebar.prototype.fix = function () {
    if ($('body').is(Selector.boxed)) {
      this._fixForBoxed($(Selector.bg))
    }
  }

  // Private

  ControlSidebar.prototype._fixForBoxed = function (bg) {
    bg.css({
      position: 'absolute',
      height: $(Selector.wrapper).height()
    })
  }

  ControlSidebar.prototype.changeSidebarSkin = function () {
    var sidebar = $(Selector.sidebar)
    if (sidebar.hasClass(ClassName.skinDark)) {
      sidebar.removeClass(ClassName.skinDark)
      sidebar.addClass(ClassName.skinLight)
    } else {
      sidebar.removeClass(ClassName.skinLight)
      sidebar.addClass(ClassName.skinDark)
    }
  }

  ControlSidebar.prototype.changeLayout = function (cls) {
    $('body').toggleClass(cls)
    $(Selector.sidebar).trigger($.Event(Event.changeLayout))
    if ($('body').hasClass(ClassName.fixed) && cls == ClassName.fixed) {
      $(Selector.sidebar).trigger($.Event(Event.fixedLayout))
    }
    this.fix()
  }

  ControlSidebar.prototype.hover = function (cls) {
    this.changeLayout(cls)
    var slide = !this.options.slide

    this.options.slide = slide
    if (!slide)
      $(Selector.sidebar).removeClass(ClassName.open)
  }

  ControlSidebar.prototype.expandOnHover = function (element) {
    element.attr('disabled', true)
    $(Selector.sidebar).trigger($.Event(Event.expandOnHover))
    if (!$('body').hasClass(ClassName.collapsed))
      $(Selector.dataSidebar).click()
  }


  // Plugin Definition
  // =================
  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data = $this.data(DataKey)

      if (!data) {
        var options = $.extend({}, Default, $this.data(), typeof option == 'object' && option)
        $this.data(DataKey, (data = new ControlSidebar($this, options)))
      }

      if (typeof option == 'string') data.toggle()
    })
  }

  var old = $.fn.controlSidebar

  $.fn.controlSidebar = Plugin
  $.fn.controlSidebar.Constructor = ControlSidebar

  // No Conflict Mode
  // ================
  $.fn.controlSidebar.noConflict = function () {
    $.fn.controlSidebar = old
    return this
  }

  // ControlSidebar Data API
  // =======================
  /* $(document).on('click', Selector.data, function (event) {
   if (event) event.preventDefault()
   Plugin.call($(this), 'toggle')
   })*/

}(jQuery)

/* BoxWidget()
 * ======
 * Adds box widget functions to boxes.
 *
 * @Usage: $('.my-box').boxWidget(options)
 *         or add [data-widget="box-widget"] to the ul element
 *         Pass any option as data-option="value"
 */
+ function ($) {
  'use strict'

  var DataKey = 'lte.boxwidget'

  var Default = {
    animationSpeed: 500,
    collapseTrigger: '[data-widget="collapse"]',
    fullscreenTrigger: '[data-widget="fullscreen"]',
    removeTrigger: '[data-widget="remove"]',
    collapseIcon: 'fa-minus',
    expandIcon: 'fa-plus',
    fullscreenIcon: 'fa-expand',
    compressIcon: 'fa-compress',
    removeIcon: 'fa-times'
  }

  var Selector = {
    data: '.box',
    collapsed: '.collapsed-box',
    fullscreen: '.fullscreen-box',
    body: '.box-body',
    footer: '.box-footer',
    tools: '.box-tools'
  }

  var ClassName = {
    collapsed: 'collapsed-box',
    fullscreen: 'fullscreen-box',
    fullscreenMode: 'fullscreen-box-mode',
  }

  var Event = {
    collapsed: 'collapsed.boxwidget',
    expanded: 'expanded.boxwidget',
    fullscreen: 'fullscreen.boxwidget',
    compress: 'compress.boxwidget',
    removed: 'removed.boxwidget'
  }

  // BoxWidget Class Definition
  // =====================
  var BoxWidget = function (element, options) {
    this.element = element
    this.options = options

    this._setUpListeners()
  }

  BoxWidget.prototype.toggleCollapse = function () {
    var isOpen = !$(this.element).is(Selector.collapsed)

    if (isOpen) {
      this.collapse()
    } else {
      this.expand()
    }
  }

  BoxWidget.prototype.toggleFullscreen = function () {
    var isFull = $(this.element).is(Selector.fullscreen)

    if (isFull) {
      this.compress()
    } else {
      this.fullscreen()
    }
  }

  BoxWidget.prototype.compress = function () {
    var compressEvent = $.Event(Event.compress)
    var compressIcon = this.options.compressIcon
    var fullscreenIcon = this.options.fullscreenIcon
    $(this.element).removeClass(ClassName.fullscreen)
    $('body').removeClass(ClassName.fullscreenMode)

    $(Selector.tools)
      .find('.' + compressIcon)
      .removeClass(compressIcon)
      .addClass(fullscreenIcon)

    $(this.element).trigger(compressEvent)
  }

  BoxWidget.prototype.fullscreen = function () {
    var fullscreenEvent = $.Event(Event.fullscreen)
    var compressIcon = this.options.compressIcon
    var fullscreenIcon = this.options.fullscreenIcon

    $(this.element).addClass(ClassName.fullscreen)
    $('body').addClass(ClassName.fullscreenMode)

    $(Selector.tools)
      .find('.' + fullscreenIcon)
      .removeClass(fullscreenIcon)
      .addClass(compressIcon)

    $(this.element).trigger(fullscreenEvent)
  }

  BoxWidget.prototype.expand = function () {
    var expandedEvent = $.Event(Event.expanded)
    var collapseIcon = this.options.collapseIcon
    var expandIcon = this.options.expandIcon

    $(this.element).removeClass(ClassName.collapsed)

    $(Selector.tools)
      .find('.' + expandIcon)
      .removeClass(expandIcon)
      .addClass(collapseIcon)

    $(this.element).find(Selector.body + ', ' + Selector.footer)
      .slideDown(this.options.animationSpeed, function () {
        $(this.element).trigger(expandedEvent)
      }.bind(this))
  }

  BoxWidget.prototype.collapse = function () {
    var collapsedEvent = $.Event(Event.collapsed)
    var collapseIcon = this.options.collapseIcon
    var expandIcon = this.options.expandIcon

    $(Selector.tools)
      .find('.' + collapseIcon)
      .removeClass(collapseIcon)
      .addClass(expandIcon)

    $(this.element).find(Selector.body + ', ' + Selector.footer)
      .slideUp(this.options.animationSpeed, function () {
        $(this.element).addClass(ClassName.collapsed)
        $(this.element).trigger(collapsedEvent)
      }.bind(this))
  }

  BoxWidget.prototype.remove = function () {
    var removedEvent = $.Event(Event.removed)

    $(this.element).slideUp(this.options.animationSpeed, function () {
      $(this.element).trigger(removedEvent)
      $(this.element).remove()
    }.bind(this))
  }

  // Private

  BoxWidget.prototype._setUpListeners = function () {
    var that = this

    $(this.element).on('click', this.options.collapseTrigger, function (event) {
      if (event) event.preventDefault()
      that.toggleCollapse($(this))
    })

    $(this.element).on('click', this.options.fullscreenTrigger, function (event) {
      if (event) event.preventDefault()
      that.toggleFullscreen($(this))
    })

    $(this.element).on('click', this.options.removeTrigger, function (event) {
      if (event) event.preventDefault()
      that.remove($(this))
    })
  }

  // Plugin Definition
  // =================
  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data = $this.data(DataKey)

      if (!data) {
        var options = $.extend({}, Default, $this.data(), typeof option == 'object' && option)
        $this.data(DataKey, (data = new BoxWidget($this, options)))
      }

      if (typeof option == 'string') {
        if (typeof data[option] == 'undefined') {
          throw new Error('No method named ' + option)
        }
        data[option]()
      }
    })
  }

  var old = $.fn.boxWidget

  $.fn.boxWidget = Plugin
  $.fn.boxWidget.Constructor = BoxWidget

  // No Conflict Mode
  // ================
  $.fn.boxWidget.noConflict = function () {
    $.fn.boxWidget = old
    return this
  }

  // BoxWidget Data API
  // ==================
  /*$(window).on('load', function () {
   $(Selector.data).each(function () {
   Plugin.call($(this))
   })
   })*/

}(jQuery)
