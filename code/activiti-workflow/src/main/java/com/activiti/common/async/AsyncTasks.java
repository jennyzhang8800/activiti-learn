package com.activiti.common.async;

import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.tools.InvokeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * 异步任务
 */
@Component
public class AsyncTasks {
    private final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);
    @Autowired
    private ToolsMapper toolsMapper;

    @Async("insertInvokeLogTask")
    public void insertInvokeLogTask(InvokeLog invokeLog) throws InterruptedException {
        toolsMapper.insertInvokeLog(invokeLog);
    }
}
