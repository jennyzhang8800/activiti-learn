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
        <div>
            <table class="my-assessment-table" lay-filter="my-assessment-table">
            </table>
        </div
        <div class="layui-form-item">
            <div class="layui-input-block my-assessment-commit" style="display: none">
                <button class="layui-btn" lay-submit="">立即提交</button>
            </div>
        </div>
    </fieldset>
</div>

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
                type:"POST",
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layer.open({
                            title: '提醒',
                            content: data.errorMessage
                        });
                    } else {
                        var response = data.data;
                        judgeCount = response.workList.length;
                        parent.find('.my-assessment-standardAnswer').text(response.answer);
                        parent.find('.my-assessment-question').text(response.question);
                        table.render({
                            elem: '.my-assessment-table',
                            data: response.workList,
                            height: 250,
                            width: 3000,
                            cols: [[ //标题栏
                                {field: 'courseCode', title: '课程代码', width: 100},
                                {field: 'emailAddress', title: '邮箱', width: 220},
                                {field: 'workDetail', title: '回答', width: 600},
                                {field: 'grade', title: '请打分', width: 100, edit: 'text'}
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

        //单元格编辑事件
        table.on('edit(my-assessment-table)', function (obj) {
            if (!chekNum(obj.value)) {
                if (obj.value !== '')
                    layer.open({
                        title: '请重新输入',
                        content: '<p>输入的分数无效：' + obj.value + '</p>'
                    });
                delete judgeGradeList[obj.data.emailAddress];
            } else {
                judgeGradeList[obj.data.emailAddress] = {
                    grade: obj.data.grade
                }
            }
        });

        //提交事件
        parent.find('.my-assessment-commit button').on('click', function () {
            if (JSONLength(judgeGradeList) !== judgeCount) {
                layer.open({
                    title: '提交失败',
                    content: '请完成所有互评任务！'
                });
                return false;
            }
            $.ajax({
                url: './api/user/commitJudgementInfo',
                data: {
                    judge:JSON.stringify(judgeGradeList),
                    courseCode:courseCode
                },
                type:"POST",
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

        /**
         * 判断是否为数字
         * @param s
         * @returns {boolean}
         */
        function chekNum(s) {
            return s !== null && s !== "" ? !isNaN(s) : false;
        }

        /**
         *
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
    })
</script>