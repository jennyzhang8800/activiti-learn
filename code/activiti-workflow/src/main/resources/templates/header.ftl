<div class="layui-header">
    <div class="layui-logo">学生互评系统</div>
    <ul class="layui-nav layui-layout-right">
        <li class="layui-nav-item">
            <a href="javascript:;"><img src="${base}/img/beier.png" class="layui-nav-img">${userEmail!"我"}</a>
        </li>
        <li class="layui-nav-item"><a href="./logout">退出</a></li>
    </ul>
</div>
<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <ul class="layui-nav layui-nav-tree" lay-filter="test">
            <li class="layui-nav-item  layui-nav-itemed">
                <a href="javascript:;">我的任务</a>
                <dl class="layui-nav-child">
                <@identity_validate userEmail="${userEmail!}" id="1">
                    <#if identity?? && identity==true>
                        <dd><a id="my-judgement" href="javascript:;">成绩审核</a></dd>
                    <#else >
                        <dd><a id="my-answer" href="javascript:;">答题</a></dd>
                        <dd><a id="my-assessment" href="javascript:;">参与互评</a></dd>
                    </#if>
                </@identity_validate>
                </dl>
            </li>
        <@identity_validate userEmail="${userEmail!}" id="1">
            <#if identity?? && identity==true>
                <li class="layui-nav-item"><a id="my-grade-info" href="javascript:;">成绩信息</a></li>
                <li class="layui-nav-item">
                    <a href="javascript:;">管理员配置</a>
                    <dl class="layui-nav-child">
                        <dd><a id="my-time-conf" href="javascript:;">工作流配置</a></dd>
                        <dd><a id="my-user-role" href="javascript:;">管理员配置</a></dd>
                        <dd><a id="my-log-view" href="javascript:;">日志查看</a></dd>
                        <dd><a id="my-emailLog-view" href="javascript:;">邮件发送情况</a></dd>
                    </dl>
                </li>
            <#else >
                <li class="layui-nav-item"><a id="my-job-done" href="javascript:;">任务完成情况</a></li>
                <li class="layui-nav-item"><a id="my-grade-info" href="javascript:;">成绩信息</a></li>
            </#if>
        </@identity_validate>
        </ul>
    </div>
</div>