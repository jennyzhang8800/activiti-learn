package com.activiti.common.utils;

import com.activiti.common.redis.RedisCommonUtil;
import com.activiti.mapper.VerifyTaskMapper;
import com.activiti.pojo.user.JudgementLs;
import com.activiti.pojo.user.StudentWorkInfo;
import com.activiti.pojo.user.VerifyTask;
import com.activiti.service.JudgementService;
import com.activiti.service.UserService;
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
    @Autowired
    private VerifyTaskMapper verifyTaskMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JudgementService judgementService;
    @Autowired
    private RedisCommonUtil redisCommonUtil;

    /**
     * 互评超时监听器
     *
     * @param delegateExecution
     * @throws Exception
     */
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String businessKey = delegateExecution.getProcessBusinessKey();
        taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list().forEach(task -> {
            if (delegateExecution.getProcessInstanceId().equals(task.getProcessInstanceId())) {
                jsonArray.add(task.getAssignee());
//                logger.info(task.getAssignee() + ">>>>>>>>>>>>>>>>>>>" + taskService.getVariable(task.getId(), "judgeEmailList").toString());
                String courseCode = taskService.getVariable(task.getId(), "courseCode").toString();
//                JSONArray jsonArray1 = JSONArray.parseArray(taskService.getVariable(task.getId(), "judgeEmailList").toString());
                JSONArray jsonArray1= JSONArray.parseArray(redisCommonUtil.get(ConstantsUtils.beginDistributeTask + courseCode + task.getAssignee()).toString());
                List<StudentWorkInfo> studentWorkInfoList = new ArrayList<>();
                jsonArray1.forEach(a -> {
                    studentWorkInfoList.add(userService.selectStudentWorkInfo(new StudentWorkInfo(courseCode, a.toString())));
                });
                judgementService.updateStuGrade(new StudentWorkInfo(courseCode, task.getAssignee(), 0d));
                studentWorkInfoList.forEach(studentWorkInfo -> {
                    List<JudgementLs> judgementLsList = judgementService.selectJudgementLs(new JudgementLs(courseCode, studentWorkInfo.getEmailAddress()));
                    int times = judgementLsList == null ? 0 : judgementLsList.size();
                    if (studentWorkInfo.getJoinJudgeTime() != null) {
                        verifyTaskMapper.insertTask(new VerifyTask(
                                studentWorkInfo.getEmailAddress(), studentWorkInfo.getWorkDetail(), studentWorkInfo.getCourseCode(), times
                        ));
                    }
                });
            }
        });
        delegateExecution.setVariable("emailAlertList", jsonArray.toJSONString());
    }
}
