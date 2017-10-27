<div class="gradeInfoAdminView">
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>题目列表:</legend>
        <div style="    margin: 20px 30px 20px;">
        <#if scheduleDtoList??>
            <#list scheduleDtoList as item>
                <button class="layui-btn layui-btn-normal gradeInfoAdminView-courseBtn"
                        courseCode="${item.courseCode}">${item.courseName}</button>
            </#list>
        </#if>
        </div>
    </fieldset>

    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend class="all-gradeInfoAdminView-sourceCode">学生成绩信息:</legend>
    </fieldset>

    <div>
        <table class="gradeInfoAdminViewTable" lay-filter="gradeInfoAdminViewTable">
        </table>
        <div>
            <div id="gradeInfoAdminViewPage"></div>
        </div>
    </div>
</div>

<script>
    layui.use(['table', 'laypage', 'layer'], function () {
        var table = layui.table, laypage = layui.laypage, layer = layui.layer, $ = layui.jquery;
        $('.gradeInfoAdminView .gradeInfoAdminView-courseBtn').on('click', function () {
            var courseCode = $(this).attr('courseCode');
            $.ajax({
                url: './api/user/selectAllGradeInfoByAdmin',
                data: {courseCode: courseCode},
                type:"POST",
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: "失败",
                            content: '<p>' + data.errorMessage + '</p>'
                        })
                    } else {
                        laypage.render({
                            elem: 'gradeInfoAdminViewPage',
                            count: data.data.count,
                            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                            theme: '#FF5722',
                            limit: 10,
                            limits: [10, 15, 20, 25],
                            jump: function (obj) {
                                var param = {courseCode: courseCode, page: obj.curr, limit: obj.limit};
                                $.ajax({
                                    url: './api/user/selectAllGradeInfoByAdmin',
                                    data: param,
                                    type:"POST",
                                    dataType: 'json',
                                    success: function (data) {
                                        table.render({
                                            elem: '.gradeInfoAdminViewTable',
                                            data: data.data.list,
                                            height: 400,
                                            width: 3000,
                                            cols: [[ //标题栏
                                                {field: 'courseCode', title: '题号', width: 150},
                                                {field: 'emailAddress', title: '邮箱地址', width: 200},
                                                {field: 'workDetail', title: '作业详情', width: 860},
                                                {field: 'lastCommitTimeString', title: '提交时间', width: 200},
                                                {field: 'joinJudgeTimeString', title: '参与互评时间', width: 200},
                                                {field: 'grade', title: '成绩', width: 100}
                                            ]],
                                            skin: 'row', //表格风格
                                            even: true,
                                            page: false //是否显示分页
                                        })
                                    }
                                });
                            }
                        });
                    }
                }
            });
        });
    })
</script>