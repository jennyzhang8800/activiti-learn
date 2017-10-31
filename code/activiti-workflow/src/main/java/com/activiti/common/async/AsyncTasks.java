package com.activiti.common.async;

import com.activiti.common.utils.ActivitiHelper;
import com.activiti.common.utils.HttpClientUtil;
import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.tools.InvokeLog;
import com.activiti.pojo.user.JudgementLs;
import com.activiti.pojo.user.StudentWorkInfo;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.ConvertGroup;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 异步任务
 */
@Component
public class AsyncTasks {
    private final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);
    @Autowired
    private ToolsMapper toolsMapper;
    @Autowired
    private ActivitiHelper activitiHelper;
    @Autowired
    private HttpClientUtil httpClientUtil;

    /**
     * 异步任务
     *
     * @param object
     * @param type   insertLog:插日志
     * @throws InterruptedException
     */
    @Async("asyncTask")
    @SuppressWarnings("unchecked")
    public void asyncTask(Object object, String type) throws InterruptedException {
        if ("insertLog".equals(type))
            toolsMapper.insertInvokeLog((InvokeLog) object);
        //分配作业
        if ("distributeTask".equals(type))
            activitiHelper.distributeTask((JSONObject) object);
        //提交作业到gitlab
        if ("commitWorkToGitlab".equals(type)) {
            JSONObject jsonObject = (JSONObject) object;
            try {
                httpClientUtil.commitWorkToGitlab((StudentWorkInfo) jsonObject.get("studentWorkInfo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //提交成绩到gitlab
        if ("updateGradeToGitlab".equals(type)) {
            List<JSONObject> jsonObjectList = (List<JSONObject>) object;
            for (JSONObject jsonObject : jsonObjectList) {
                try {
                    httpClientUtil.updateGradeToGitlab((StudentWorkInfo) jsonObject.get("studentWorkInfo"),
                            (List<JudgementLs>) jsonObject.get("judgementLsList"), false);
                    Thread.sleep(8000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //提交老师修改的成绩到gitlab
        if ("teacherUpdateGradeToGitlab".equals(type)) {
            JSONObject jsonObject = (JSONObject) object;
            try {
                if (null == jsonObject.get("put")) {
                    httpClientUtil.updateGradeToGitlab((StudentWorkInfo) jsonObject.get("studentWorkInfo"),
                            (List<JudgementLs>) jsonObject.get("judgementLsList"), false);
                } else
                    httpClientUtil.updateGradeToGitlab((StudentWorkInfo) jsonObject.get("studentWorkInfo"),
                            (List<JudgementLs>) jsonObject.get("judgementLsList"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
