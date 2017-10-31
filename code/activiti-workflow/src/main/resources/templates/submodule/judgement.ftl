<div class="my-judgement">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>待批改的作业（针对参加过互评但是没有成绩的学生）</legend>
        <div>
            <table class="myJudgementWaitTable" lay-filter="myJudgementWaitTable">
            </table>
            <div id="myJudgementWaitPage"></div>
        </div>
    </fieldset>
    <br>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>已经批改的作业</legend>
        <div>
            <table class="myJudgementDoneTable">
            </table>
            <div id="myJudgementDonePage"></div>
        </div>
    </fieldset>
</div>

<script>
    layui.use(['table', 'laypage', 'layer'], function () {
        var table = layui.table, laypage = layui.laypage, $ = layui.jquery;

        /**
         * 加载待审核的成绩
         */
        function loadMyJudgementWaitTable() {
            var status = 'wait';
            $.ajax({
                url: './api/user/selectMyJudgementWait',
                data: {status: status},
                type: "POST",
                jsonType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '数据请求失败',
                            content: '<p>' + data.errorMessage + '</p>'
                        })
                    }
                    laypage.render({
                        elem: 'myJudgementWaitPage',
                        count: data.data.count,
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        theme: '#FF5722',
                        jump: function (obj) {
                            var param = {status: status, page: obj.curr, limit: obj.limit};
                            $.ajax({
                                type: 'POST',
                                url: './api/user/selectMyJudgementWait',
                                data: param,
                                dataType: 'json',
                                success: function (result) {
                                    table.render({
                                        elem: '.myJudgementWaitTable',
                                        data: result.data.list,
                                        height: 280,
                                        width: 2000,
                                        cols: [[ //标题栏
                                            {field: 'email', title: '邮箱', width: 150},
                                            {field: 'courseCode', title: '题号', width: 150},
                                            {field: 'answer', title: '提交作业内容', width: 700},
                                            {field: 'judgeTimes', title: '被打分次数', width: 100},
                                            {field: 'grade', title: '成绩', width: 100, edit: 'text'},
                                            {
                                                field: 'commit',
                                                title: '提交',
                                                width: 300,
                                                templet: '#my-judgement-commit'
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

        /**
         * 加载已经审核的成绩
         */
        function loadMyJudgementDoneTable() {
            var status = 'done';
            $.ajax({
                url: './api/user/selectMyJudgementWait',
                data: {status: status},
                type: "POST",
                jsonType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '数据请求失败',
                            content: '<p>' + data.errorMessage + '</p>'
                        })
                    }
                    laypage.render({
                        elem: 'myJudgementDonePage',
                        count: data.data.count,
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        theme: '#FF5722',
                        jump: function (obj) {
                            var param = {status: status, page: obj.curr, limit: obj.limit};
                            $.ajax({
                                type: 'POST',
                                url: './api/user/selectMyJudgementWait',
                                data: param,
                                dataType: 'json',
                                success: function (result) {
                                    table.render({
                                        elem: '.myJudgementDoneTable',
                                        data: result.data.list,
                                        height: 280,
                                        width: 2000,
                                        cols: [[ //标题栏
                                            {field: 'email', title: '邮箱', width: 200},
                                            {field: 'courseCode', title: '课程代码', width: 200},
                                            {field: 'answer', title: '提交作业内容', width: 1000},
                                            {field: 'judgeTimes', title: '被打分次数', width: 100},
                                            {field: 'grade', title: '成绩', width: 100}
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

        table.on('tool(myJudgementWaitTable)', function (obj) {
            var data = obj.data;
            delete data.answer;
            if (obj.event === 'commit' && data.grade) {
                $.ajax({
                    url: './api/user/insertAdminJudgementResult',
                    data: data,
                    type: "POST",
                    dateType: 'json',
                    success: function (result) {
                        if (!result.success) {
                            layer.open({
                                title: '提交失败',
                                content: '<p>' + result.errorMessage + '</p>'
                            })
                        } else {
                            loadMyJudgementWaitTable();
                            setTimeout(function () {
                                loadMyJudgementDoneTable();
                            }, 300);
                            layer.open({
                                title: '提交成功',
                                content: '<p>' + JSON.stringify(data) + '</p>'
                            })
                        }
                    }
                })
            }
        });

        loadMyJudgementWaitTable();
        setTimeout(function () {
            loadMyJudgementDoneTable();
        }, 300);
    })
</script>

<script type="text/html" id="my-judgement-commit">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="commit">提交成绩</a>
</script>
