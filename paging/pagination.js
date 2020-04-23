/**
 * 分页插件 . 实现自动左右伸缩
 *
 * 使用示例：
 * @example
 * ```javascript
 var pagination = new Pagination('#page', {
      total: 30,//总页数
      current: 1,// 当前页号
      showForwardInput: false,// 是否显示跳转按钮 默认不显示
      showNextAndPrev: true,// 是否显示上下页按钮 默认显示
      count: 5,// 显示可容纳的页数
      pageChange: function (newPageNum, oldPageNum) {
        console.log('当前页码：', newPageNum);
      }
    });
 * ```
 * @author fangjiang
 * @date 2020-03-07
 */

(function (global, factory) {
  // commonJS规范模块导出
  if (typeof module !== 'undefined' && module.exports) {
    module.exports = factory();
  }
  // AMD规范模块导出
  else if (typeof define === 'function') {
    define(function () {
      return factory();
    });
  }
  // 直接导出至浏览器window对象
  else {
    global.Pagination = factory();
  }
})(this, function () {
  var Pagination = function (el, options) {
    this.options = {
      // 是否显示上一下和下一页，默认显示
      showNextAndPrev: true,
      // 是否显示跳页输入框，默认不显示
      showForwardInput: false,
      // 总页数，默认0
      total: 0,
      // 显示页号个数，超出个数则显示省略号，默认6
      count: 6,
      // 当前页号, 默认1
      current: 1,
      prevLabel: '上一页',
      nextLabel: '下一页',
      /**
       * 页号发生改变的事件
       *
       * @param {number} oldPageNum 旧页号
       * @param {number} newPageNum 新页号
       */
      pageChange: function (oldPageNum, newPageNum) {
      }
    };

    // 属性拷贝
    for (var prop in options) {
      if (options.hasOwnProperty(prop)) {
        this.options[prop] = options[prop];
      }
    }
    // 当前分页所在的容器
    this.container = typeof el === 'string' ? document.querySelector(el) : el;
    // 当前页号
    this.current = this.options.current;
    // 分页需要操作的dom集合
    this.doms = {};
    // 渲染分页插件
    this.render();
  };

  /**
   * 渲染分页插件
   */
  Pagination.prototype.render = function () {
    if(this.options.total <= 0) {
      return;
    }
    this.remove();
    // 上一页html
    var prevPageHtml = '';
    // 下一页html
    var nextPageHtml = '';
    if (this.options.showNextAndPrev) {
      prevPageHtml = '<a class="prev-page" href="javascript:void(0);">' + this.options.prevLabel + '</a>';
      nextPageHtml = '<a class="next-page" href="javascript:void(0);">' + this.options.nextLabel + '</a>';
    }
    var forwardInputHtml = '';
    if (this.options.showForwardInput) {
      forwardInputHtml = '<div class="page-to">跳至<input type="text" oninput="this.value = this.value.replace(/[^0-9]/g, \'\')"/>页<span class="page-go">跳转</span></div>';
    }
    this.container.innerHTML = '<div class="pagination">' + prevPageHtml
        + '<ul></ul>' + nextPageHtml + forwardInputHtml + '</div>';
    // 装配dom 注： 页号相关dom需要渲染完页号后更新
    this.autowiredDoms();
    // 渲染页号
    this.renderPages(true);
  };

  /**
   * 渲染展示的页号
   *
   * @param {boolean} isForward 是否前进，否则后退
   */
  Pagination.prototype.renderPages = function (isForward) {
    // 省略的页号
    var ellipsisHtml = '<li class="ellipsis">…</li>';
    var pagesHtml = [];
    // 总页数小于或等于可显示的页号总数，不需要显示省略
    if (this.options.total <= this.options.count) {
      for (var i = 1; i <= this.options.total; i++) {
        if (this.current === i) {
          pagesHtml.push('<li class="current-page">' + i + '</li>');
        } else {
          pagesHtml.push('<li>' + i + '</i>');
        }
      }
    } else {
      // 根据当前页号设置游标
      var index = this.current >= this.options.count - 1 ? this.current : 1;
      var showCount = this.options.count;
      // 前进时如果剩余页数不足可显示则游标移到末尾，倒序输出
      if (index !== 1
          &&  this.options.total - this.current <= this.options.count - 1
          && isForward) {
        index = this.options.total;
        isForward = false;
        showCount += 1;
      }

      // 如果游标为1则顺序输出
      if (index === 1) {
        isForward = true;
      }

      if (showCount > this.options.count) {
        ellipsisHtml = '';
      }
      for (var j = 1; j < showCount; j++) {
        if (index > 0 && index < this.options.total) {
          if (this.current === index) {
            pagesHtml.push('<li class="current-page">' + index + '</li>');
          } else {
            pagesHtml.push('<li>' + index + '</i>');
          }
        }
        // 向右移动游标递增，有左移动递减
        isForward ? index++ : index--;
      }
      // 如果是后退则倒序数组
      if (!isForward) {
        pagesHtml = pagesHtml.reverse();
      }
      pagesHtml.push(ellipsisHtml);
      pagesHtml.push(
          '<li class="' + (this.current === this.options.total ? 'current-page'
          : '') + '">' + this.options.total + '</li>');
    }
    this.container.querySelector('ul').innerHTML = pagesHtml.join('');
    // 更新dom
    this.doms.ellipsisDom = this.container.querySelector('.ellipsis');
    // 处理禁止按钮
    this.handleForbidBtns();
  };

  /**
   * 从分页移除分页
   */
  Pagination.prototype.remove = function () {
    this.container.innerHTML = '';
  };

  /**
   * 处理禁止按钮
   *
   */
  Pagination.prototype.handleForbidBtns = function () {

    // 检查上一页按钮
    if (this.current === 1) {
      this.doms.prevDom.classList.add('forbid-btn');
      this.doms.prevDom.setAttribute('disable', 'true');
    } else if (this.doms.prevDom.classList.contains('forbid-btn')) {
      this.doms.prevDom.classList.remove('forbid-btn');
      this.doms.prevDom.removeAttribute('disable');
    }

    // 检查下一页按钮
    if (this.current === this.options.total) {
      this.doms.nextDom.classList.add('forbid-btn');
      this.doms.nextDom.setAttribute('disable', 'true');
    } else if (this.doms.nextDom.classList.contains('forbid-btn')) {
      this.doms.nextDom.classList.remove('forbid-btn');
      this.doms.nextDom.removeAttribute('disable');
    }
  };

  /**
   * 装配dom
   */
  Pagination.prototype.autowiredDoms = function () {
    this.doms.prevDom = this.container.querySelector('.prev-page');
    this.doms.nextDom = this.container.querySelector('.next-page');
    this.doms.forwardInputDom = this.container.querySelector('.page-to input');
    this.doms.forwardBtnDom = this.container.querySelector('.page-go');
    this.doms.pagesDom = this.container.querySelector('ul');
    // 绑定事件
    this.bindEvents();
  };

  /**
   * 页号跳转
   * 
   * @param {number} newPageNum 需要跳转的页号
   */
  Pagination.prototype.jump = function(newPageNum) {
    if(isNaN(newPageNum)) {
      return false;
    }
    var currentPageLi = this.container.querySelector('.current-page');
    var oldPageNum = parseInt(currentPageLi.innerText);
    if (oldPageNum === newPageNum) {
      return;
    }
    newPageNum = newPageNum <= 0 ? 1 : newPageNum;
    newPageNum = newPageNum > this.options.total ? this.options.total : newPageNum;
    this.current = newPageNum;
    var children = this.doms.pagesDom.children;
    var isFind = false;
    // 查找是否有跳转页号的元素
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      // 当有省略号时跳转页号和总页数相同必须重新渲染，如果没有省略号即使跳的是总页数号也可以不用重新渲染
      if (child.innerText === newPageNum + ''
          && !(newPageNum === this.options.total
              && this.doms.ellipsisDom)) {
        child.classList.add('current-page');
        currentPageLi.classList.remove('current-page');
        isFind = true;
        break;
      }
    }

    // 未找到重新渲染
    if (!isFind) {
      this.renderPages(newPageNum > oldPageNum);
    }

    try {
      this.options.pageChange.call(this, newPageNum, oldPageNum);
    } catch (e) {
      console.error(e);
    }
    this.handleForbidBtns();
  };

  /**
   * 绑定相关事件
   */
  Pagination.prototype.bindEvents = function () {
    var context = this;

    // 监听每个页号的点击
    this.doms.pagesDom.addEventListener('click', function (event) {
      var target = event.target || event.srcElement;
      if (target.tagName === 'LI' && !target.classList.contains('ellipsis')) {
        var currentPageLi = context.container.querySelector('.current-page');
        var oldPageNum = parseInt(currentPageLi.innerText);
        var newPageNum = context.current = parseInt(target.innerText);
        // 如果点击目标元素是省略号前一个，证明当前需要向右前进
        // 如果直接点击最后一个元素则看做向右前进(特殊，需要从从最后一个往前计算，然后倒序输出)
        var isLast = (context.doms.ellipsisDom &&
            target === context.doms.ellipsisDom.previousElementSibling)
            || newPageNum === context.options.total;
        
        // 如果点击元素是页号容器内容的第一个子元素，证明需要向左后移动，
        // 但如果页号为1则已经到了左端尽头，可不必重新渲染
        var isFirst = target === context.doms.pagesDom.firstChild;
        if (isLast || isFirst) {
          context.renderPages(isLast);
        } else {
          target.classList.add('current-page');
          currentPageLi.classList.remove('current-page');
          context.handleForbidBtns();
        }
		if (oldPageNum === newPageNum) {
          return;
        }
        try {
          context.options.pageChange.call(context, newPageNum, oldPageNum);
        } catch (e) {
          console.error(e);
        }
      }
    });

    // 监听上一页的点击
    this.doms.prevDom.addEventListener('click', function (event) {
      if(this.classList.contains('forbid-btn')) {
        return false;
      }
      var currentPageLi = context.container.querySelector('.current-page');
      var oldPageNum = parseInt(currentPageLi.innerText);
      var newPageNum = context.current = oldPageNum - 1;
      if (oldPageNum === newPageNum) {
        return;
      }
      var target = currentPageLi.previousElementSibling;
      // 当前页号是第一个或者是第二个时候，向左后移动
      if (!target || target === context.doms.pagesDom.firstChild) {
        context.renderPages(false);
      } else {
        target.classList.add('current-page');
        currentPageLi.classList.remove('current-page');
        context.handleForbidBtns();
      }
      try {
        context.options.pageChange.call(context, newPageNum, oldPageNum);
      } catch (e) {
        console.error(e);
      }
    });

    // 监听下一页的点击
    this.doms.nextDom.addEventListener('click', function (event) {
      if(this.classList.contains('forbid-btn')) {
        return false;
      }
      var currentPageLi = context.container.querySelector('.current-page');
      var oldPageNum = parseInt(currentPageLi.innerText);
      var newPageNum = context.current = oldPageNum + 1;
      if (oldPageNum === newPageNum) {
        return;
      }
      var target = currentPageLi.nextElementSibling;
      // 当前页号是倒数第一个或者第二个项右前移动
      if (context.doms.ellipsisDom
          && (target === context.doms.ellipsisDom.previousElementSibling
              || target === context.doms.ellipsisDom)) {
        context.renderPages(true);
      } else {
        target.classList.add('current-page');
        currentPageLi.classList.remove('current-page');
        context.handleForbidBtns();
      }
      try {
        context.options.pageChange.call(context, newPageNum, oldPageNum);
      } catch (e) {
        console.error(e);
      }
    });

    // 监听跳转按钮
    this.options.showForwardInput && this.doms.forwardBtnDom.addEventListener(
        'click', function () {
          var forwardInputValue = parseInt(context.doms.forwardInputDom.value);
          context.doms.forwardInputDom.value = '';
          context.jump(forwardInputValue);
        });
  };

  return Pagination;
});
