<div class="my-answer">
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>题目列表:</legend>
        <div style="    margin: 20px 30px 20px;">
        <#if scheduleDtoList??>
            <#list scheduleDtoList as item>
                <button class="layui-btn layui-btn-normal my-answer-courseBtn"
                        courseCode="${item.courseCode}">${item.courseName}</button>
            </#list>
        </#if>
        </div>
    </fieldset>

    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend class="my-answer-fieldset-courseCode" courseCode="">题目ID:</legend>
    </fieldset>
    <div class="layui-form">
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">题目内容</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea my-answer-question" name="question" cols="" rows="10"
                          readonly></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">开始作答</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" lay-verify="required" name="answer" rows="10"
                          class="layui-textarea my-answer-answer"></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn " lay-submit="" lay-filter="my-answer-commit-btn">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary my-answer-cancel-btn">重置</button>
            </div>
        </div>
    </div>
</div>

<script>
    layui.use(['form'], function () {
        var $ = layui.jquery;
        var form = layui.form;
        var fieldset = $('.my-answer .my-answer-fieldset-courseCode');
        $('.my-answer .my-answer-courseBtn').on('click', function () {
            var courseCode = $(this).attr('courseCode');
            $.ajax({
                url: './api/common/getQAContent',
                data: {courseCode: courseCode},
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    $('.my-answer-question').val(data.data);
                    fieldset.html('题目ID:' + courseCode);
                    fieldset.attr('courseCode', courseCode);
                }
            })
        });

        $('.my-answer .my-answer-cancel-btn').on('click', function () {
            $('.my-answer .my-answer-answer').val('');
        });

        //监听提交
        form.on('submit(my-answer-commit-btn)', function (data) {
            var courseCode = fieldset.attr('courseCode');
            $.ajax({
                url: './api/user/commitWork',
                data: {
                    courseCode: courseCode,
                    workDetail: data.field.answer
                },
                type: "POST",
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        layer.alert('<p>提交成功，可以到任务完成情况中查看详细信息，我们已将互评相关邮件发送至您的邮箱，请注意查收！<p>', {
                            title: '提交成功'
                        });
                        $('.my-answer .my-answer-answer').val('');
                    }
                    else
                        layer.alert('<p>' + result.errorMessage + '<p>', {
                            title: '提交失败'
                        });
                }
            });
            return false;
        });
        (function () {
            var errorMessage = '${errorMessage!""}';
            if (errorMessage)
                layer.alert('<p>' + errorMessage + '<p>', {
                    title: '提交失败'
                });
        })()
    })
</script>