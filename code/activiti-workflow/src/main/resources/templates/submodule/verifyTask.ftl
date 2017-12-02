<div class="my-verifyTask">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>待审核的作业</legend>
        <div>
            <table class="myVerifyTable" lay-filter="myVerifyTable">
            </table>
        </div>
    </fieldset>
    <br>
    <div style="display: none" id="my-verifyTask-details">
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">题目</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea question" name="" cols="" rows=""
                          readonly></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">参考答案</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea standardAnswer" name="" cols="" rows=""
                          readonly></textarea>
            </div>
        </div>
    </div>
</div>

<script>
    layui.use(['table', 'laypage', 'layer'], function () {
        var table = layui.table, $ = layui.jquery;

        function init() {
            $.ajax({
                url: './api/user/selectAllTeacherTask',
                type: "POST",
                jsonType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '数据请求失败',
                            content: '<p>' + data.errorMessage + '</p>'
                        })
                    } else {
                        table.render({
                            elem: '.myVerifyTable',
                            data: data.data,
                            height: 280,
                            width: 2000,
                            cols: [[ //标题栏
                                {field: 'taskId', title: '任务ID', width: 150},
                                {field: 'emailAddress', title: '邮箱', width: 150},
                                {field: 'courseCode', title: '题号', width: 150},
                                {field: 'workDetail', title: '提交作业内容', width: 700},
                                {field: 'grade', title: '成绩', width: 100, edit: 'text'},
                                {
                                    field: 'details',
                                    title: '题目详情',
                                    width: 150,
                                    templet: '#my-verify-details'
                                },
                                {
                                    field: 'commit',
                                    title: '提交',
                                    width: 150,
                                    templet: '#my-verify-commit'
                                }
                            ]],
                            skin: 'row', //表格风格
                            even: true,
                            page: false //是否显示分页
                        })
                    }
                }
            });
        }

        init();

        table.on('tool(myVerifyTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'commit') {
                $.ajax({
                    url: './api/user/finishTeacherVerifyTask',
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
                            init();
                            layer.open({
                                title: '提交成功',
                                content: '<p>' + JSON.stringify(result) + '</p>'
                            })
                        }
                    }
                })
            } else if (obj.event === 'details') {
                var courseCode = data.courseCode;
                var id=$('#my-verifyTask-details');
                $.ajax({
                    url: './api/common/selectCourseDetails',
                    data: {courseCode: courseCode},
                    type: "POST",
                    dateType: 'json',
                    success: function (result) {
                        if (!result.success) {
                            layer.open({
                                title: '获取数据失败',
                                content: '<p>' + result.errorMessage + '</p>'
                            })
                        } else {
                            var details=result.data.details;
                            id.find('.question').text(details.question);
                            id.find('.standardAnswer').text(details.answer);
                            layer.open({
                                title: '题目详情',
                                area: ['700px', '400px'],
                                content: id.html()
                            });
                        }
                    }
                })
            }
        });
    })
</script>
<script type="text/html" id="my-verify-commit">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="commit">提交成绩</a>
</script>
<script type="text/html" id="my-verify-details">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="details">点击查看详情</a>
</script>