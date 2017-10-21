package com.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    public void execute(DelegateExecution execution) {
        String operationType = (String) execution.getVariable("operationType");
        System.out.println("收到了传来的参数" + operationType);
    }
}
