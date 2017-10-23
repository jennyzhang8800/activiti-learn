<div class="my-emailLog-view">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>通知邮件发送情况</legend>
        <div>
            <table class="emailInfoTable">
            </table>
            <div style="margin-left: 15%;" id="emailInfoLayPage"></div>
        </div>
    </fieldset>
</div>
<script>
    layui.use(['table', 'laypage'], function () {
        var table = layui.table;
        var laypage = layui.laypage;
        var $ = layui.jquery;
        $.ajax({
            url: './api/common/countEmailLog',
            dataType: 'json',
            type:"POST",
            success: function (data) {
                laypage.render({
                    elem: 'emailInfoLayPage',
                    count: data.data.count,
                    layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                    theme:'#FF5722',
                    limit:15,
                    limits:[20, 30, 40, 50],
                    jump: function (obj) {
                        var param = {page: obj.curr, limit: obj.limit};
                        $.ajax({
                            url: './api/common/selectEmailLog',
                            data: param,
                            type:"POST",
                            dataType: 'json',
                            success: function (data) {
                                table.render({
                                    elem: '.emailInfoTable',
                                    data: data.data,
                                    height: 650,
                                    width: 3000,
                                    cols: [[ //标题栏
                                        {field: 'receiveAddress', title: '收件人', width: 200},
                                        {field: 'sendAddress', title: '发件人', width: 200},
                                        {field: 'subject', title: '主题', width: 150},
                                        {field: 'content', title: '内容', width: 450},
                                        {field: 'rscPath', title: '资源地址', width: 200},
                                        {field: 'rscId', title: '资源id', width: 250},
                                        {field: 'sendTimeString', title: '发送时间', width: 170},
                                        {field: 'status', title: '发送状态', width: 100}
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
    })
</script>