package com.activiti.common.async;

import com.activiti.common.utils.ActivitiHelper;
import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.tools.InvokeLog;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.groups.ConvertGroup;
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

    /**
     * 异步任务
     *
     * @param object
     * @param type   insertLog:插日志
     * @throws InterruptedException
     */
    @Async("asyncTask")
    public void asyncTask(Object object, String type) throws InterruptedException {
        if ("insertLog".equals(type))
            toolsMapper.insertInvokeLog((InvokeLog) object);
        if ("distributeTask".equals(type))
            activitiHelper.distributeTask((JSONObject)object);
    }
}
