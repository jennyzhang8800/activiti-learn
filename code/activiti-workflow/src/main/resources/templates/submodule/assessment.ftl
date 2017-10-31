<div class="my-assessment">
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>题目列表:</legend>
        <div style="margin: 20px 30px 20px;">
        <#if (scheduleDtoList?? && scheduleDtoList?size>0)>
            <#list scheduleDtoList as item>
                <button class="layui-btn layui-btn-normal my-assessment-courseBtn"
                        courseCode="${item.courseCode}">${item.courseName}</button>
            </#list>
        <#else >
            <p>当前没有需要互评的作业</p>
        </#if>
        </div>
    </fieldset>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend class="my-assessment-courseCode">题目ID:<span></span></legend>
        <br>
        <br>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">题目</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea my-assessment-question" name="" cols="" rows=""
                          readonly></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">参考答案</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea my-assessment-standardAnswer" name="" cols="" rows=""
                          readonly></textarea>
            </div>
        </div>
        <div class="my-assessment-origin" style="display: none">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">学生答案</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea my-assessment-answer" name="" cols="" rows=""
                              readonly></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">分数</label>
                <div class="layui-input-block">
                    <input type="text" required lay-verify="required" placeholder="分数" autocomplete="off"
                           class="layui-input my-assessment-grade">
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">评语</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入评语" class="layui-textarea my-assessment-judgement"></textarea>
                </div>
            </div>
        </div>
        <div>
            <table class="my-assessment-table" lay-filter="my-assessment-table">
            </table>
        </div
        <div class="layui-form-item">
            <div class="layui-input-block my-assessment-commit">
                <button class="layui-btn" lay-submit="">立即提交</button>
            </div>
        </div>
    </fieldset>
</div>
<script type="text/html" id="my-assessment-operation">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="answer">参与评分</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="save">保存</a>
</script>
<script>
    layui.use(['table', 'layer'], function () {
        var table = layui.table, layer = layui.layer, $ = layui.jquery;
        var judgeGradeList = {};
        var judgeCount;
        var courseCode;
        var parent = $('.my-assessment');
        $('.my-assessment .my-assessment-courseBtn').on('click', function () {
            courseCode = $(this).attr('courseCode');
            parent.find('.my-assessment-courseCode span').html('  ' + courseCode);
            $.ajax({
                url: './api/user/selectWorkListToJudge',
                data: {courseCode: courseCode},
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '提醒',
                            content: data.errorMessage
                        });
                    } else {
                        var response = data.data;
                        var workList = response.workList;
                        for (var i in workList) {
                            judgeGradeList[workList[i]['emailAddress']] = {};
                        }
                        judgeCount = response.workList.length;
                        parent.find('.my-assessment-standardAnswer').text(response.answer);
                        parent.find('.my-assessment-question').text(response.question);
                        table.render({
                            elem: '.my-assessment-table',
                            data: workList,
                            height: 250,
                            width: 3000,
                            cols: [[ //标题栏
                                {field: 'courseCode', title: '题目ID', width: 100},
                                {field: 'userName', title: '用户名', width: 100},
                                {field: 'emailAddress', title: '邮箱', width: 250},
                                {
                                    field: 'operation',
                                    title: '操作',
                                    width: 200,
                                    templet: '#my-assessment-operation'
                                }
                            ]],
                            skin: 'row', //表格风格
                            even: true,
                            page: false //是否显示分页
                        });
                        parent.find('.my-assessment-commit').show();
                    }
                }
            });
        });

        //提交事件
        parent.find('.my-assessment-commit button').on('click', function () {
            if (JSONLength(judgeGradeList) !== judgeCount || !isJsonValidate(judgeGradeList)) {
                layer.open({
                    title: '提交失败',
                    content: '请完成所有互评任务！'
                });
                return false;
            }
            $.ajax({
                url: './api/user/commitJudgementInfo',
                data: {
                    judge: JSON.stringify(judgeGradeList),
                    courseCode: courseCode
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '提交失败',
                            content: data.errorMessage
                        });
                    } else {
                        layer.open({
                            title: '提交成功',
                            content: data.data
                        });
                    }
                }
            })
        });

        //评分
        table.on('tool(my-assessment-table)', function (obj) {
            var answer = parent.find('.my-assessment-origin .my-assessment-answer');
            var judgement = parent.find('.my-assessment-origin .my-assessment-judgement');
            var grade = parent.find('.my-assessment-origin .my-assessment-grade');
            var emailAddress = obj.data.emailAddress;
            var detail = judgeGradeList[emailAddress];
            if (obj.event === 'answer') {
                parent.find('.my-assessment-origin').show();
                answer.val(obj.data.workDetail);
                judgement.val(detail.judgement);
                grade.val(detail.grade);
            } else {
                if (judgement.val() === '') {
                    layer.msg('请输入评语', {
                        time: 2000 //2s后自动关闭
                    });
                } else if (grade.val() === '' || !chekNum(grade.val())) {
                    layer.msg('请输入分数', {
                        time: 2000 //2s后自动关闭
                    });
                } else {
                    detail['grade'] = grade.val();
                    detail['judgement'] = judgement.val();
                    parent.find('.my-assessment-origin').hide();
                }
            }
        });

        /**
         * 判断是否为数字
         * @param s
         * @returns {boolean}
         */
        function chekNum(s) {
            return s !== null && s !== "" ? !isNaN(s) : false;
        }

        /**
         *计算json数组长度
         * @param obj
         * @returns {number}
         * @constructor
         */
        function JSONLength(obj) {
            var size = 0, key;
            for (key in obj) {
                if (obj.hasOwnProperty(key)) size++;
            }
            return size;
        }

        //p判断json数据合法性
        function isJsonValidate(json) {
            for (var key in json) {
                var obj = json[key];
                if (!obj['grade'] || !obj['judgement'])
                    return false;
            }
            return true;
        }
    })

</script>