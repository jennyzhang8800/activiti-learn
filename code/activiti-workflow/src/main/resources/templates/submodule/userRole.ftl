<div class="my-user-role">
    <fieldset class="layui-elem-field" style="margin-top: 30px;">
        <legend>管理员列表</legend>
        <div class="layui-btn-group" style="margin: 1% 0% 1% 5%;">
            <button class="add-user-role layui-btn" data-type="parseTable">添加管理员</button>
        </div>
        <div style="padding: 0% 10% 3% 5%;">
            <table class="layui-table">
                <colgroup>
                    <col width="200">
                    <col width="500">
                    <col width="200">
                    <col width="50">
                </colgroup>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>邮箱</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#if userRoleList??>
                    <#list userRoleList as item>
                    <tr>
                        <td class="id">${item.id}</td>
                        <td class="email">${item.email}</td>
                        <td class="remarks">${item.remarks}</td>
                        <td>
                            <button email="${item.email}" data-method="notice" class="layui-btn-danger layui-btn">刪除
                            </button>
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </fieldset>
    <div style="display: none;" class="add-user-role-form">
        <form class="layui-form" action="./api/user/addUserRole" style="padding: 10% 20% 10% 0%;">
            <div class="layui-form-item">
                <label class="layui-form-label">ID</label>
                <div class="layui-input-block">
                    <input type="text" name="id" lay-verify="required" class="layui-input id">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮箱</label>
                <div class="layui-input-block">
                    <input type="text" name="email" lay-verify="required" class="layui-input email">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">备注</label>
                <div class="layui-input-block">
                    <input type="text" name="remarks" lay-verify="required" class="layui-input remarks">
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    layui.use('layer', function () {
        var $ = layui.jquery, layer = layui.layer;
        $('.my-user-role .layui-btn-danger').on('click', function () {
            var parentTr = $(this).parents('tr');
            var email = $(this).attr('email');
            layer.open({
                type: 1,
                title: false,//不显示标题栏,
                closeBtn: false,
                area: '300px;',
                shade: 0.8,
                id: 'LAY_layuipro', //设定一个id，防止重复弹出
                btn: ['确定', '取消'],
                btnAlign: 'c',
                moveType: 1, //拖拽模式，0或者1
                content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">确认移除邮用户：' + email + '吗？</div>',
                success: function (layero) {
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').on('click', function () {
                        $.ajax({
                            url: './api/user/deleteUserRole',
                            data: {email: email},
                            dataType: 'json',
                            success: function (data) {
                                if (!data.data) {
                                    layer.msg(email + '移除失败', {
                                        time: 3000, //20s后自动关闭
                                        btn: ['好的']
                                    });
                                }
                                else {
                                    layer.msg(email + '移除成功', {
                                        time: 3000 //2s后自动关闭
                                    });
                                    parentTr.remove();
                                }
                            },
                            error: function () {
                                layer.msg(email + '移除失败', {
                                    time: 3000, //20s后自动关闭
                                    btn: ['好的']
                                });
                            }
                        })
                    });
                },
            });
        });

        $('.my-user-role .add-user-role').on('click', function () {
            layer.open({
                title: ['添加管理员'],
                type: 1,
                offset: 'auto',
                id: 'layer-add-user-role', //防止重复弹出
                content: $('.my-user-role .add-user-role-form'),
                btn: ['确认', '取消'],
                btnAlign: 'c', //按钮居中
                shade: 0, //不显示遮罩
                yes: function () {
                    var form = $('.my-user-role .add-user-role-form .layui-form');
                    var email = form.find("input[name='email']").val();
                    $.ajax({
                        url: './api/user/addUserRole',
                        data: {
                            id: form.find("input[name='id']").val(),
                            email: email,
                            remarks: form.find("input[name='remarks']").val()
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (!data.success) {
                                layer.msg('错误信息：'+data.errorMessage, {
                                    time: 3000, //3s后自动关闭
                                    btn: ['好的']
                                });
                            }
                            else {
                                layer.msg('成功添加：'+email, {
                                    time: 3000 //3s后自动关闭
                                });
                                $('#my-user-role').trigger("click");
                            }
                        },
                        error: function () {
                            layer.msg(email+'添加失败', {
                                time: 3000, //20s后自动关闭
                                btn: ['好的']
                            });
                        }
                    });
                    layer.closeAll();
                }
            });
        })
    })
</script>