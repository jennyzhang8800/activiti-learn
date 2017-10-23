package com.activiti.common.utils;

import com.alibaba.fastjson.JSONArray;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowTakeListener implements ExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(ExecutionListener.class);
    @Autowired
    private TaskService taskService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String businessKey = delegateExecution.getProcessBusinessKey();
        logger.info("businessKey="+businessKey);
        taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list().forEach(task -> {
            jsonArray.add(task.getAssignee());
//            taskService.complete(task.getId());
        });
        delegateExecution.setVariable("emailAlertList", jsonArray.toJSONString());
    }
}
