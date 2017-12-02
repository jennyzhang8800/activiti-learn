<div class="my-time-conf">
<#--课程配置-->
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>已经部署的流程</legend>
        <div style="height:auto">
            <table class="my-time-process-table" lay-data="{height:100}" lay-filter="my-time-process-table">
            </table>
            <div id="my-time-process-LayPage"></div>
        </div>
    </fieldset>
    <div class="layui-form" style="display: none">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>配置流程为：<span class="process-key" process-key=""></span>的题目</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">题目</label>
            <div class="layui-input-block">
                <input type="text" name="courseName" lay-verify="required" placeholder="请输入" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">题号</label>
            <div class="layui-input-block">
                <input type="text" name="courseCode" lay-verify="required" placeholder="请输入" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开始互评人数</label>
            <div class="layui-input-block">
                <input type="text" name="distributeMaxUser" lay-verify="required" placeholder="请输入" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">互评超时时间</label>
            <div class="layui-input-block">
                <input type="text" name="timeout" lay-verify="required"
                       placeholder="格式（10秒钟:PT10S , 一天:PT1D  ,一小时:PT1H, 一分钟:PT1M）默认PT7D" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最小间隔时间</label>
            <div class="layui-input-block">
                <input type="text" name="assessmentMinTimeSlot" lay-verify="required"
                       placeholder="格式（10秒钟:PT10S , 一天:PT1D  ,一小时:PT1H, 一分钟:PT1M）默认PT7D" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最大间隔时间</label>
            <div class="layui-input-block">
                <input type="text" name="assessmentMaxTimeSlot" lay-verify="required"
                       placeholder="格式（10秒钟:PT10S , 一天:PT1D  ,一小时:PT1H, 一分钟:PT1M）默认PT7D" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
    <#--<div class="layui-form-item">-->
    <#--<label class="layui-form-label">学生申诉</label>-->
    <#--<div class="layui-input-block">-->
    <#--<input type="radio" name="isAppeal" value="no" title="不允许" checked="">-->
    <#--<input type="radio" name="isAppeal" value="yes" title="允许">-->
    <#--</div>-->
    <#--</div>-->
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="my-time-conf-submit">立即提交</button>
                <button type="reset" class="my-time-conf-cancel layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </div>
    <br>
    <br>
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>已配置的题目</legend>
        <div style="height:auto">
            <table class="my-time-conf-table" lay-data="{height:100}" lay-filter="my-time-conf-table">
            </table>
            <div id="my-time-conf-LayPage"></div>
        </div>
    </fieldset>
    <div style="display: none" id="my-time-conf-judgement">
        <img src=${request.contextPath}/mooc-workflow/img/judgement.png>
    </div>

    <div style="display: none" id="my-time-conf-noJudgement">
        <img src=${request.contextPath}/mooc-workflow/img/nojudgement.png>
    </div>
</div>

<script>
    layui.use(['form', 'layedit', 'laydate', 'table', 'laypage'], function () {
        var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, $ = layui.jquery;
        var table = layui.table;
        var laypage = layui.laypage;
        var list = ['startTime', 'commitEndTime', 'judgeStartTime', 'judgeEndTime', 'auditStartTime', 'auditEndTime', 'publishTime'];
        for (var index in list) {
            //时间选择器
            laydate.render({
                elem: '.my-time-conf-' + list[index],
                type: 'datetime'
            });
        }
        /**
         * 加载数据表格
         */
        var loadTable = function () {
            $.ajax({
                url: './api/common/countAllScheduleTime',
                dataType: 'json',
                success: function (data) {
                    laypage.render({
                        elem: 'my-time-conf-LayPage',
                        count: data.data.count,
                        theme: '#FF5722',
                        limit: 5,
                        limits: [5, 10, 15],
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        jump: function (obj) {
                            var param = {page: obj.curr, limit: obj.limit};
                            $.ajax({
                                url: './api/common/selectAllScheduleTime',
                                data: param,
                                dataType: 'json',
                                success: function (data) {
                                    table.render({
                                        elem: '.my-time-conf-table',
                                        data: data.data,
                                        height: 315,
//                                        width: 900,
                                        cols: [[ //标题栏
                                            {field: 'courseName', title: '题目', width: 150},
                                            {field: 'courseCode', title: '题号', width: 150},
                                            {field: 'githubAddress', title: 'GitHub地址', width: 500},
                                            {field: 'isAppeal', title: '学生申诉', width: 100},
                                            {field: 'distributeMaxUser', title: '开始互评人数', width: 150},
                                            {field: 'timeout', title: '超时时间', width: 100},
                                            {field: 'assessmentMinTimeSlot', title: '最小间隔时间', width: 150},
                                            {field: 'assessmentMaxTimeSlot', title: '最大间隔时间', width: 150},
//                                            {field: 'judgeEndTimeString', title: '互评结束时间', width: 200},
//                                            {field: 'publishTimeString', title: '成绩发布时间', width: 200},
                                            {
                                                field: 'operation',
                                                title: '操作',
                                                width: 100,
                                                templet: '#my-time-conf-operation'
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


            $.ajax({
                url: './api/common/getProcessLists?page=1&limit=10',
                dataType: 'json',
                success: function (data) {
                    laypage.render({
                        elem: 'my-time-process-LayPage',
                        count: data.data.total,
                        theme: '#FF5722',
                        limit: 5,
                        limits: [5, 10, 15],
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        jump: function (obj) {
                            var param = {page: obj.curr, limit: obj.limit};
                            $.ajax({
                                url: './api/common/getProcessLists',
                                data: param,
                                dataType: 'json',
                                success: function (data) {
                                    table.render({
                                        elem: '.my-time-process-table',
                                        data: data.data.data,
                                        height: 150,
//                                        width: 900,
                                        cols: [[ //标题栏
                                            {field: 'deploymentId', title: 'deploymentId', width: 100},
                                            {field: 'id', title: 'id', width: 250},
                                            {field: 'name', title: 'name', width: 220},
                                            {field: 'key', title: 'key', width: 220},
                                            {field: 'resourceName', title: 'resourceName', width: 450},
                                            {field: 'diagramResourceName', title: 'diagramResourceName', width: 450},
                                            {
                                                field: 'choose',
                                                title: '操作',
                                                width: 100,
                                                templet: '#my-time-process-choose'
                                            },
                                            {
                                                field: 'picture',
                                                title: '流程图',
                                                width: 100,
                                                templet: '#my-time-process-picture'
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
        };
        //监听提交
        form.on('submit(my-time-conf-submit)', function (data) {
            if (parseInt(data.field.distributeMaxUser) <= 4) {
                layer.alert("开始互评人数最小5人", {
                    title: '部署失败'
                });
                return false;
            }
            data.field.isAppeal = $('.layui-form .process-key').attr('process-key') === 'answerToAssessment' ? 'yes' : 'no';
            $.ajax({
                url: './api/common/insertScheduleTime',
                data: {data: JSON.stringify(data.field)},
                type: "POST",
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        layer.alert(JSON.stringify(result), {
                            title: '部署成功'
                        });
                        loadTable();
                        $('.layui-form').hide();
                        $('.layui-form input').val('');
                    }
                    else
                        layer.alert(JSON.stringify(result), {
                            title: '部署失败'
                        });
                }
            });
            return false;
        });

        $('.my-time-conf .my-time-conf-cancel').on('click', function () {
            $('.my-time-conf input').val('');
        });

        loadTable();

        table.on('tool(my-time-conf-table)', function (obj) {
            var courseCode = obj.data.courseCode;
            if (obj.event === 'delete') {
                $.ajax({
                    url: './api/common/removeScheduleTime',
                    data: {courseCode: courseCode},
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            layer.open({
                                title: '操作成功',
                                shadeClose: true,
                                content: '<p>' + result.data + '<p>'
                            });
                            loadTable();
                        } else {
                            layer.open({
                                title: '操作失败',
                                shadeClose: true,
                                content: '<p>' + result.errorMessage + '<p>'
                            });
                        }
                    }
                })
            }
        });

        table.on('tool(my-time-process-table)', function (obj) {
            if (obj.event === 'choose') {
                $('.layui-form .process-key').html(obj.data.name).attr('process-key', obj.data.key);
                $('.layui-form').show();
            }
            if (obj.event === 'picture') {
                var content = obj.data.key === 'answerToAssessment' ? $('#my-time-conf-judgement').html() : $('#my-time-conf-noJudgement').html();
                var area= obj.data.key !== 'answerToAssessment'?['1500px', '650px']:['1300px', '870px'];
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
    })
</script>

<script type="text/html" id="my-time-conf-operation">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">删除</a>
</script>
<script type="text/html" id="my-time-process-choose">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="choose">选择</a>
</script>
<script type="text/html" id="my-time-process-picture">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="picture">查看</a>
</script>