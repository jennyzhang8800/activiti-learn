<#if projectEnv?? && projectEnv=="pro">
    <#assign base="/mooc-workflow"+request.contextPath />
<#else >
    <#assign base=request.contextPath />
</#if>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes" hah="${projectEnv}">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="${base}/layui/css/layui.css" media="all">
<link rel="stylesheet" href="${base}/css/global.css" media="all">
<script src="${base}/js/jquery-3.2.1.min.js"></script>
<script src="${base}/layui/layui.js"></script>
<script src="${base}/js/global.js"></script>