<div class="my-log-view">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>接口调用日志查询</legend>
        <div>
            <table class="logInfoTable">
            </table>
            <div id="invokeLogLayPage"></div>
        </div>
    </fieldset>
</div>
<script>
    layui.use(['table', 'laypage'], function () {
        var table = layui.table;
        var laypage = layui.laypage;
        var $ = layui.jquery;
        $.ajax({
            url: './api/common/countInvokeLog',
            dataType: 'json',
            type:"POST",
            success: function (data) {
                laypage.render({
                    elem: 'invokeLogLayPage',
                    count: data.data.count,
                    layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                    theme:'#FF5722',
                    limit:15,
                    limits:[20, 30, 40, 50],
                    jump: function (obj) {
                        var param = {page: obj.curr, limit: obj.limit};
                        $.ajax({
                            url: './api/common/selectInvokeLog',
                            data: param,
                            type:"POST",
                            dataType: 'json',
                            success: function (data) {
                                table.render({
                                    elem: '.logInfoTable',
                                    data: data.data,
                                    height: 650,
                                    width: 3000,
                                    cols: [[ //标题栏
                                        {field: 'uuid', title: '序列号', width: 200},
                                        {field: 'invokeTime', title: '调用时长', width: 100},
                                        {field: 'params', title: '入参', width: 150},
                                        {field: 'result', title: '出参', width: 550},
                                        {field: 'email', title: '邮箱', width: 200},
                                        {field: 'requestUri', title: '访问地址', width: 250},
                                        {field: 'status', title: '调用状态', width: 100},
                                        {field: 'dateString', title: '时间', width: 170}
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