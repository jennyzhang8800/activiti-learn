package com.activiti.controller;

import com.activiti.common.aop.ApiAnnotation;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/activitiTest")
public class Test {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @RequestMapping("/start")
    @ResponseBody
    @ApiAnnotation
    public Object startProcess(@RequestParam(value = "operationType") String operationType) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("operationType", operationType);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);
        return processInstance.getTenantId();
    }

}
