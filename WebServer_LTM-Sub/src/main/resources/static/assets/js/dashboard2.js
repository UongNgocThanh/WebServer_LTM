$(function () {
    'use strict';

    // Kiểm tra và khởi tạo sortable cho dashboard widgets
    if ($('.connectedSortable').length) {
        $('.connectedSortable').sortable({
            placeholder: 'sort-highlight',
            connectWith: '.connectedSortable',
            handle: '.box-header, .nav-tabs',
            forcePlaceholderSize: true,
            zIndex: 999999
        });
        $('.connectedSortable .box-header, .connectedSortable .nav-tabs-custom').css('cursor', 'move');
    }

    // Kiểm tra và khởi tạo sortable cho todo list
    if ($('.todo-list').length) {
        $('.todo-list').sortable({
            placeholder: 'sort-highlight',
            handle: '.handle',
            forcePlaceholderSize: true,
            zIndex: 999999
        });

        /* The todo list plugin */
        $('.todo-list').todoList({
            onCheck: function () {
                console.log($(this), 'The element has been checked');
            },
            onUnCheck: function () {
                console.log($(this), 'The element has been unchecked');
            }
        });
    }
});
