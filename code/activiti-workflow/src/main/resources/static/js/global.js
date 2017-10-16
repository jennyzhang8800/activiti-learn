layui.use('element', function () {
    var $ = layui.jquery, layer = layui.layer;
    var element = layui.element;
    var layui_body = $('.layui-body');

    $('#my-answer').on('click', function () {
        if ($('.my-answer').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./answer'));
    });

    $('#my-assessment').on('click', function () {
        if ($('.my-assessment').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./assessment'));
    });

    $('#my-judgement').on('click', function () {
        if ($('.my-judgement').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./judgement'));
    });

    $('#my-time-conf').on('click', function () {
        if ($('.my-time-conf').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./timeConf'));
    });

    $('#my-job-done').on('click', function () {
        if ($('.my-job-done').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./jobDone'));
    });

    $('#my-grade-info').on('click', function () {
        if ($('.my-grade-info').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./gradeInfo'));
    });

    $('#my-user-role').on('click', function () {
        if ($('.my-user-role').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./userRole'));
    });

    $('#my-log-view').on('click', function () {
        if ($('.my-log-view').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./logView'));
    });

    $('#my-emailLog-view').on('click', function () {
        if ($('.my-emailLog-view').length)return false;
        layui_body.html('');
        layui_body.html(ajaxGet('./emailLogView'));
    });

    var ajaxGet = function (url) {
        return $.ajax({url: url, async: false}).responseText;
    }
});
