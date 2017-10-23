<div class="my-grade-info">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>查看成绩:</legend>
    </fieldset>
    <div>
        <table class="myGradeInfoTable" lay-filter="myGradeInfoTable">
        </table>
    </div>

    <div class="my-grade-info-submodule" style="display: none">
            <table class="myGradeJudgeInfoTable">
            </table>
    </div>
</div>

<script>
    layui.use(['table', 'laypage', 'layer'], function () {
        var table = layui.table, laypage = layui.laypage, layer = layui.layer, $ = layui.jquery;

        /**
         * 加载成绩
         */
        function loadMyGradeInfoTable() {
            $.ajax({
                url: './api/user/selectStudentGrade',
                dataType: 'json',
                type:"POST",
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '数据请求失败',
                            content: '<p>' + data.errorMessage + '</p>'
                        })
                    } else {
                        table.render({
                            elem: '.myGradeInfoTable',
                            data: data.data,
                            height: 500,
                            width: 3000,
                            cols: [[ //标题栏
                                {field: 'courseCode', title: '题号', width: 100},
                                {field: 'emailAddress', title: '邮箱', width: 200},
                                {field: 'workDetail', title: '我的答案', width: 750},
                                {field: 'lastCommitTimeString', title: '提交时间', width: 160},
                                {field: 'grade', title: '成绩', width: 100},
                                {field: 'joinJudgeTimeString', title: '参与互评时间', width: 160},
                                {field: 'joinJudgeTimeString', title: '参与互评时间', width: 160},
                                {
                                    field: 'operation',
                                    title: '评阅人',
                                    width: 150,
                                    templet: '#my-grade-info-judge'
                                }
                            ]],
                            skin: 'row', //表格风格
                            even: true,
                            page: false //是否显示分页
                        })
                    }
                }
            })
        }

        table.on('tool(myGradeInfoTable)', function (obj) {
            var courseCode = obj.data.courseCode;
            if (obj.event === 'moreInfo') {
                $.ajax({
                    url: './api/user/selectWhoJudgeMe',
                    data: {courseCode: courseCode},
                    dataType: 'json',
                    type:"POST",
                    success: function (data) {
                        if (!data.success) {
                            layer.open({
                                title: '数据请求失败',
                                content: '<p>' + data.errorMessage + '</p>'
                            })
                        } else {
                            table.render({
                                elem: '.myGradeJudgeInfoTable',
                                data: data.data,
                                height: 200,
                                width: 700,
                                cols: [[ //标题栏
                                    {field: 'courseCode', title: '题号', width: 150},
                                    {field: 'judgeEmail', title: '邮箱', width: 250},
                                    {field: 'judgeTimeString', title: '提交时间', width: 200},
                                    {field: 'grade', title: '评分', width: 95}
                                ]],
                                skin: 'row', //表格风格
                                even: true,
                                page: false //是否显示分页
                            });
                            layer.open({
                                anim: 1,
                                type:1,
                                title:"互评详情",
                                closeBtn: 0,
                                area: ['703px', '237px'],
                                shadeClose: true,
                                content: $('.my-grade-info .my-grade-info-submodule').html()
                            })
                        }
                    }
                })
            }
        });

        setTimeout(function () {
            loadMyGradeInfoTable();
        }, 200);
    });
</script>

<script type="text/html" id="my-grade-info-judge">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="moreInfo">查看详情</a>
</script>