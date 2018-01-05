<div class="my-job-done">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>已完成的答题任务</legend>
        <div>
            <table class="myJobAnswerDoneTable" lay-filter="myJobAnswerDoneTable">
            </table>
            <div style="margin-left: 30%;" id="myJobAnswerDonePage"></div>
        </div>
    </fieldset>
    <br><br>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>已完成的互评任务</legend>
        <div style="margin-left: 30%;">
            <table class="myJobAssessmentTable">
            </table>
            <div style="margin-left: 9%;" id="myJobAssessmentPage"></div>
        </div>
    </fieldset>
</div>

<div style="display: none" id="judgement">
    <img src=${request.contextPath}/mooc-workflow/img/judgement.png>
</div>

<div style="display: none" id="nojudgement">
    <img src=${request.contextPath}/mooc-workflow/img/nojudgement.png>
</div>

<script>
    layui.use(['table', 'laypage', 'layer'], function () {
        var table = layui.table;
        var laypage = layui.laypage;
        var layer = layui.layer;
        var $ = layui.jquery;

        /**
         * 已完成的答题任务
         */
        function loadMyJobAnswerDoneTable() {
            $.ajax({
                type: 'POST',
                url: './api/user/selectStudentWorkInfo',
                data: {count: true},
                dataType: 'json',
                success: function (data) {
                    laypage.render({
                        elem: 'myJobAnswerDonePage',
                        count: data.data.count,
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        theme: '#FF5722',
                        jump: function (obj) {
                            var param = {page: obj.curr, limit: obj.limit};
                            $.ajax({
                                type: 'POST',
                                url: './api/user/selectStudentWorkInfo',
                                data: param,
                                dataType: 'json',
                                success: function (result) {
                                    table.render({
                                        elem: '.myJobAnswerDoneTable',
                                        data: result.data,
                                        height: 280,
                                        width: 2000,
                                        cols: [[ //标题栏
                                            {field: 'courseCode', title: '题目ID', width: 150},
                                            {field: 'emailAddress', title: '邮箱', width: 200},
                                            {field: 'lastCommitTimeString', title: '提交时间', width: 200},
                                            {field: 'workDetail', title: '提交作业内容', width: 500},
                                            {
                                                field: 'originQuestion',
                                                title: '详细信息',
                                                width: 300,
                                                templet: '#my-job-done-answer-detail'
                                            }
                                        ]],
                                        skin: 'row', //表格风格
                                        even: true,
                                        page: false //是否显示分页
                                    })
                                }
                            })
                        }
                    });
                }
            });
        }

        setTimeout(function () {
            loadMyJobAnswerDoneTable();
        }, 100);

        //已完成的答题任务监控工具条
        table.on('tool(myJobAnswerDoneTable)', function (obj) {
            var courseCode = obj.data.courseCode;
            if (obj.event === 'origin') {
                $.ajax({
                    type: 'POST',
                    url: './api/common/getQAContent',
                    data: {courseCode: courseCode},
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            layer.open({
                                title: '原题',
                                shadeClose: true,
                                content: '<p>' + result.data + '<p>'
                            });
                        } else {
                            layer.open({
                                title: '请求出错',
                                shadeClose: true,
                                content: '<p>' + result.errorMessage + '<p>'
                            });
                        }
                    }
                })
            }
            if (obj.event === 'detail') {
                var isAppeal = false;
                var content = $('#nojudgement').html();
                var area= ['1500px', '650px'];
                $.ajax({
                    type: 'POST',
                    url: './api/common/isCourseAppeal',
                    data: {courseCode: courseCode},
                    dataType: 'json',
                    async:false,
                    success: function (result) {
                        if (result.success) {
                            isAppeal = result.data.isAppeal;
                        } else {
                            layer.open({
                                title: '请求出错',
                                shadeClose: true,
                                content: '<p>' + result.errorMessage + '<p>'
                            });
                        }
                    }
                });
                if (isAppeal==='yes') {
                    content = $('#judgement').html();
                    area= ['1300px', '870px'];
                }
                layer.open({
                    type: 1,
                    title: "流程图",
                    closeBtn: 1,
                    area: area,
                    shadeClose: true,
                    content: content
                });
            }
        });

        /**
         * 加载已完成的互评任务
         */
        function loadAssessmentDone() {
            $.ajax({
                type: 'POST',
                url: './api/user/selectAllCommitJudgementInfo',
                dataType: 'json',
                success: function (result) {
                    laypage.render({
                        elem: 'myJobAssessmentPage',
                        count: result.data.count,
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        theme: '#FF5722',
                        jump: function (obj) {
                            var param = {page: obj.curr, limit: obj.limit};
                            $.ajax({
                                type: 'POST',
                                url: './api/user/selectAllCommitJudgementInfo',
                                data: param,
                                dataType: 'json',
                                success: function (data) {
                                    table.render({
                                        elem: '.myJobAssessmentTable',
                                        data: data.data.list,
                                        height: 250,
                                        width: 700,
                                        cols: [[ //标题栏
                                            {field: 'courseCode', title: '题目ID', width: 150},
                                            {field: 'nonJudgeEmail', title: '邮箱', width: 250},
                                            {field: 'judgeTimeString', title: '提交时间', width: 200},
                                            {field: 'grade', title: '评分', width: 95}
                                        ]],
                                        skin: 'row', //表格风格
                                        even: true,
                                        page: false //是否显示分页
                                    })
                                }
                            })
                        }
                    });
                }
            })
        }

        setTimeout(function () {
            loadAssessmentDone();
        }, 500)
    });
</script>

<script type="text/html" id="my-job-done-answer-detail">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="origin">查看原题</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="detail">查看流程图</a>
</script>