package com.activiti.common.utils;

import com.activiti.common.kafka.MailProducer;
import com.activiti.common.redis.RedisCommonUtil;
import com.activiti.mapper.UserMapper;
import com.activiti.pojo.email.EmailDto;
import com.activiti.pojo.email.EmailType;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.pojo.user.StudentWorkInfo;
import com.activiti.service.ScheduleService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 相关操作
 */
@Component
public class ActivitiHelper {
    private final Logger logger = LoggerFactory.getLogger(ActivitiHelper.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private RedisCommonUtil redisCommonUtil;
    @Autowired
    private MailProducer mailProducer;

    /**
     * 作业分配算法
     *
     * @param emailList 学生待分配学生列表
     * @return 分配结果集合
     */
    public Map<String, List> distributeTask(List<String> emailList) {
        //返回分配结果集
        Map<String, List> resultMap = new HashMap<>();
        // 每个学生需要批改的份数
        int times = 4;
        int count = emailList.size();
        // 打乱学生顺序
        Collections.shuffle(emailList);
        // 循环遍历
        emailList.forEach(email -> {
            // 取到学生Email对应的id
            int stuId = emailList.indexOf(email);
            List<String> list = new ArrayList<>();
            // 依次取后times个学生作为互评对象
            for (int i = 1; i <= times; i++) {
                int id = stuId + i;
                // 避免空指针异常并取到正确的互评对象
                int result = id >= count ? id - count : id;
                list.add(emailList.get(result));
            }
            resultMap.put(email, list);
        });
        return resultMap;
    }


    /**
     * activiti分配作业
     *
     * @param jsonObject
     */
    @SuppressWarnings("unchecked")
    public void distributeTask(JSONObject jsonObject) {
        List<String> emailList = (List<String>) jsonObject.get("emailList");
        String courseCode = (String) jsonObject.get("courseCode");
        Collections.shuffle(emailList);
        logger.info("启动作业分配流程");
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        int judgeTimes = scheduleDto.getJudgeTimes();//每份作业被批改次数（默认为4）
        int countWork = emailList.size();
        //为每一个人分配要批改的作业
        emailList.forEach(email -> {
            int studentId = emailList.indexOf(email);
            JSONArray jsonArray = new JSONArray();
            for (int i = 1; i <= judgeTimes; i++) {
                int id = studentId + i;
                int result = id >= countWork ? id - countWork : id;
                jsonArray.add(emailList.get(result));
            }
            //设置流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("courseCode", courseCode);
            variables.put("assignee", email);
            variables.put("judgeEmailList", jsonArray.toJSONString());
            variables.put("timeout", scheduleDto.getTimeout());
            String businessKey = "assessment";
            //启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("assessmentWorkFlow", businessKey, variables);
            logger.info("用户" + email + ">>>>>>>>>>启动流程：" + processInstance.getId());
            userMapper.updateDistributeStatus(courseCode, email);
            logger.info("更新用户" + email + "分配状态");
        });
    }

    /**
     * 作业分配任务
     *
     * @param execution
     */
    public void beginDistributeTask(DelegateExecution execution) {
        String courseCode = execution.getVariable("courseCode").toString();
        JSONArray emailList = JSONArray.parseArray(execution.getVariable("assigneeJSONArray").toString());
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        int judgeTimes = scheduleDto.getJudgeTimes();//每份作业被批改次数（默认为4）
        int countWork = emailList.size();
        emailList.forEach(email -> {
            int studentId = emailList.indexOf(email);
            JSONArray jsonArray = new JSONArray();
            for (int i = 1; i <= judgeTimes; i++) {
                int id = studentId + i;
                int result = id >= countWork ? id - countWork : id;
                jsonArray.add(emailList.get(result));
            }
            redisCommonUtil.put(ConstantsUtils.beginDistributeTask + courseCode + email.toString(), jsonArray.toString(), ConstantsUtils.beginDistributeTaskExpire);
            userMapper.updateDistributeStatus(courseCode, email.toString());
            logger.info("更新用户" + email + "分配状态");
        });
    }

    /**
     * 查询需要评论的作业
     *
     * @param assignee
     * @param courseCode
     * @return
     */
    public JSONArray selectWorkListToJudge(String assignee, String courseCode, String... newFunc) {
        List<Task> list = taskService
                .createTaskQuery()//
                .taskAssignee(assignee)//个人任务的查询
                .list();
        JSONArray jsonArray = new JSONArray();
        if (null == newFunc) {
            list.forEach(task -> {
                String taskId = task.getId();
                if (courseCode.equals(taskService.getVariable(taskId, "courseCode"))) {
                    JSONArray.parseArray(taskService.getVariable(taskId, "judgeEmailList").toString()).forEach(a -> {
                        jsonArray.add(a.toString());
                    });
                }
            });
        } else {
            list.forEach(task -> {
                String taskId = task.getId();
                if (courseCode.equals(taskService.getVariable(taskId, "courseCode"))) {
                    JSONArray.parseArray(redisCommonUtil.get(ConstantsUtils.beginDistributeTask + courseCode + assignee).toString()).forEach(a -> {
                        jsonArray.add(a.toString());
                    });
                }
            });
        }
        if (taskService.createTaskQuery().taskAssignee(assignee).list() == null || taskService.createTaskQuery().taskAssignee(assignee).list().size() == 0) {
            logger.info(">>>>>>>>>>>>>>>>>>>>我已经没有任务了");
        }
        return jsonArray;
    }

    /**
     * 没有参与互评邮件提醒
     *
     * @param execution
     */
    public void emailAlert(DelegateExecution execution) {
        String emailList = (String) execution.getVariable("emailAlertList");
        JSONArray jsonArray = JSONArray.parseArray(emailList);
        if (null == jsonArray)
            return;
        logger.info("开始向这些人发邮件" + jsonArray);
        jsonArray.forEach(email -> {
            mailProducer.send(new EmailDto(email.toString(), EmailType.simple,
                    "未参与互评任务", "由于您没有参加互评,该题目分数为0"));
        });
    }

    /**
     *
     */
    public void emailAlertForTeacherVerify(DelegateExecution execution) {
        logger.info("开始向老师发邮件人发邮件");
    }

    /**
     * 结束流程
     *
     * @param assignee
     * @param courseCode
     */
    public void completeTask(String assignee, String courseCode) {
        taskService
                .createTaskQuery()//
                .taskAssignee(assignee)//个人任务的查询
                .list().forEach(task -> {
            String taskId = task.getId();
            if (courseCode.equals(taskService.getVariable(taskId, "courseCode"))) {
                taskService.complete(taskId);
            }
        });
    }

    /**
     * 启动教师审查流程
     *
     * @param studentWorkInfo
     */
    public void startTeacherVerify(StudentWorkInfo studentWorkInfo) {
        String courseCode = studentWorkInfo.getCourseCode();
        String email = studentWorkInfo.getEmailAddress();
        Map<String, Object> variables = new HashMap<>();
        variables.put("studentWorkInfo", ((JSONObject) JSONObject.toJSON(studentWorkInfo)).toJSONString());
        String businessKey = "verifyTaskBusinessKey";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("verifyTask", businessKey, variables);
        logger.info("用户" + email + ">>>>>>>>>>启动教师审核流程：" + processInstance.getId());
        userMapper.askToVerify(courseCode, email);
    }

    /**
     * 启动流程
     *
     * @param courseCode
     */
    public void startAnswerToAssessment(String courseCode, ScheduleDto scheduleDto) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("courseCode", courseCode);
        String businessKey = "assessment";
        if ("no".equals(scheduleDto.getIsAppeal()))
            runtimeService.startProcessInstanceByKey("answerToAssessmentNojudge", businessKey, variables);
        else runtimeService.startProcessInstanceByKey("answerToAssessmentNojudge", businessKey, variables);
    }

    /**
     * 判断状态  0：直接结束  1：子流程执行  2：老师批改
     *
     * @param execution
     */
    public void activitiCondition(DelegateExecution execution) {
        String courseCode = (String) execution.getVariable("courseCode");
        int conditionType = 0;
        List<StudentWorkInfo> studentWorkInfoList = userMapper.selectAllNonDistributeUser(courseCode);
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        if (null != studentWorkInfoList && studentWorkInfoList.size() > 0) {
            DateTime last = new DateTime(studentWorkInfoList.get(0).getLastCommitTime());
            DateTime first = new DateTime(studentWorkInfoList.get(studentWorkInfoList.size() - 1).getLastCommitTime());
            int minTime = commonUtil.getNumFromString(scheduleDto.getAssessmentMinTimeSlot());
            int maxTime = commonUtil.getNumFromString(scheduleDto.getAssessmentMaxTimeSlot());
            int diff = 0;
            if (scheduleDto.getAssessmentMinTimeSlot().contains("M") && scheduleDto.getAssessmentMaxTimeSlot().contains("M"))
                diff = Minutes.minutesBetween(first, last).getMinutes();
            if (scheduleDto.getAssessmentMinTimeSlot().contains("D") && scheduleDto.getAssessmentMaxTimeSlot().contains("D"))
                diff = Days.daysBetween(first, last).getDays();
            if (scheduleDto.getAssessmentMinTimeSlot().contains("S") && scheduleDto.getAssessmentMaxTimeSlot().contains("S"))
                diff = Seconds.secondsBetween(first, last).getSeconds();
            if (diff >= minTime) {
                if (diff > maxTime) {
                    JSONArray jsonArray = new JSONArray();
                    studentWorkInfoList.forEach(studentWorkInfo -> {
                        jsonArray.add(studentWorkInfo);
                        userMapper.updateDistributeStatus(courseCode, studentWorkInfo.getEmailAddress());
                        startTeacherVerify(studentWorkInfo);
                    });
                    execution.setVariable("studentWorkInfoList", (jsonArray.toJSONString()));
                    conditionType = 2;
                } else {
                    if (studentWorkInfoList.size() >= scheduleDto.getDistributeMaxUser()) {
                        List<String> assigneeList = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray();
                        studentWorkInfoList.forEach(studentWorkInfo -> {
                            assigneeList.add(studentWorkInfo.getEmailAddress());
                            jsonArray.add(studentWorkInfo.getEmailAddress());
                        });
                        execution.setVariable("assigneeList", assigneeList);
                        execution.setVariable("assigneeJSONArray", jsonArray.toJSONString());
                        conditionType = 1;
                    }
                }
            }
        }
        logger.info("题目" + courseCode + "的conditionType=" + conditionType);
        execution.setVariable("conditionType", conditionType);
        execution.setVariable("courseCode", courseCode);
        execution.setVariable("timeout", scheduleDto.getTimeout());
    }

    /**
     * 查询教师所有任务
     */
    public JSONArray selectAllTeacherTask() {
        List<Task> list = taskService
                .createTaskQuery()//
                .taskAssignee("teacher")//个人任务的查询
                .list();
        JSONArray jsonArray = new JSONArray();
        list.forEach(task -> {
            String taskId = task.getId();
            Object object = taskService.getVariable(taskId, "studentWorkInfo");
            if (null != object) {
                JSONObject studentWorkInfo = JSONObject.parseObject(taskService.getVariable(taskId, "studentWorkInfo").toString());
                studentWorkInfo.put("taskId", taskId);
                jsonArray.add(studentWorkInfo);
            }
            Object studentWorkInfoList = taskService.getVariable(taskId, "studentWorkInfoList");
            if (null != studentWorkInfoList) {
//                JSONArray.parseArray(studentWorkInfoList.toString()).forEach(a -> {
//                    JSONObject studentWorkInfo = (JSONObject) JSON.toJSON(a);
//                    studentWorkInfo.put("taskId", taskId);
//                    jsonArray.add(studentWorkInfo);
//                });
                taskService.complete(taskId);
            }
        });
        return jsonArray;
    }

    /**
     * 结束任务
     *
     * @param taskId
     */
    public void finishTeacherVerifyTask(String taskId) {
        taskService.complete(taskId);
    }
}
