package com.activiti.controller.restController;

import com.activiti.common.aop.ApiAnnotation;
import com.activiti.common.async.AsyncTasks;
import com.activiti.common.kafka.MailProducer;
import com.activiti.common.utils.ActivitiHelper;
import com.activiti.common.utils.CommonUtil;
import com.activiti.common.utils.ConstantsUtils;
import com.activiti.mapper.AdminMapper;
import com.activiti.mapper.UserMapper;
import com.activiti.mapper.VerifyTaskMapper;
import com.activiti.pojo.email.EmailDto;
import com.activiti.pojo.email.EmailType;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.pojo.user.*;
import com.activiti.service.CommonService;
import com.activiti.service.JudgementService;
import com.activiti.service.ScheduleService;
import com.activiti.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 12490 on 2017/8/1.
 */
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private JudgementService judgementService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private MailProducer mailProducer;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private VerifyTaskMapper verifyTaskMapper;
    @Autowired
    private AsyncTasks asyncTasks;
    @Autowired
    private ActivitiHelper activitiHelper;

    /*
     *  根据Email获取用户信息
     */

    @RequestMapping("/getUserInfo")
    @ResponseBody
    @ApiAnnotation
    public Object getUserInfo(@RequestParam(value = "email", required = true) String email) throws Exception {
        return userService.findUserInfo(email);
    }

    /**
     * 提交作业
     *
     * @param courseCode
     * @param workDetail
     * @param request
     * @return
     */
    @RequestMapping("/commitWork")
    @ResponseBody
    @ApiAnnotation
    public Object commitWork(@RequestParam(name = "courseCode") String courseCode,
                             @RequestParam(name = "workDetail") String workDetail, HttpServletRequest request) throws Exception {
        String email = CommonUtil.getEmailFromSession(request);
        if (userService.selectAllUserRole().stream().anyMatch(userRole -> {
            return email.equals(userRole.getEmail());
        })) throw new Exception("身为管理员的你不能提交作业！！！");
        StudentWorkInfo studentWorkInfo = new StudentWorkInfo(courseCode, email, workDetail, new Date());
        User user = new User(commonUtil.getRandomUserName(), email, courseCode);
        try {
            userService.insertUserWork(studentWorkInfo);
        } catch (Exception e) {
            throw new Exception("你已经参与过答题了！！");
        }
        userService.insertUser(user);
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        List<String> emailList = userMapper.selectNonDistributeUser(courseCode);
        //如果满了人数默认100人则异步启动流程
        if (null != emailList && emailList.size() >= scheduleDto.getDistributeMaxUser()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("emailList", emailList);
            jsonObject.put("courseCode", courseCode);
            jsonObject.put("email", email);
            asyncTasks.asyncTask(jsonObject, "distributeTask");
        }
        ModelMap modelMap = new ModelMap();
        modelMap.put("courseCode", courseCode);
        modelMap.put("workDetail", workDetail);
        modelMap.put("email", email);
        mailProducer.send(new EmailDto(email, EmailType.html, "答题成功", commonUtil.applyDataToView(modelMap, ConstantsUtils.successAnswerFtl)));
        return studentWorkInfo;
    }

    /**
     * 查询学生提交的作业
     *
     * @param courseCode
     * @return
     */
    @RequestMapping("/selectStudentWorkInfo")
    @ResponseBody
    @ApiAnnotation
    public Object selectStudentWorkInfo(
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "page", required = false, defaultValue = "1") long page,
            @RequestParam(value = "limit", required = false, defaultValue = "1") int limit,
            @RequestParam(value = "count", required = false) boolean count,
            HttpServletRequest request) {
        String email = CommonUtil.getEmailFromSession(request);
        if (null != courseCode && !"".equals(courseCode)) {
            StudentWorkInfo studentWorkInfo = new StudentWorkInfo();
            studentWorkInfo.setCourseCode(courseCode);
            studentWorkInfo.setEmailAddress(email);
            return userService.selectStudentWorkInfo(studentWorkInfo);
        } else if (count) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", userMapper.countStudentWorkInfo(email));
            return jsonObject;
        } else {
            return userMapper.selectStudentWorkInfoPage(email, (page - 1) * limit, limit);
        }
    }

    /**
     * 查询需要评论的作业
     *
     * @param courseCode
     * @return
     */
    @RequestMapping("/selectWorkListToJudge")
    @ResponseBody
    @ApiAnnotation
    public Object selectWorkListToJudge(@RequestParam(value = "courseCode") String courseCode, HttpServletRequest request) throws Exception {
        String email = CommonUtil.getEmailFromSession(request);
        StudentWorkInfo studentWorkInfo = userService.selectStudentWorkInfo(new StudentWorkInfo(courseCode, email));
        if (null != studentWorkInfo.getJoinJudgeTime())
            throw new Exception("您已经参加过互评");
        if ("false".equals(studentWorkInfo.getDistributeStatus()))
            throw new Exception("您还不能参加互评");
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        JSONObject response = commonService.getQAFromGitHub(scheduleDto.getGithubAddress());
        List<StudentWorkInfo> workInfoList = new ArrayList<>();
        JSONArray jsonArray = activitiHelper.selectWorkListToJudge(email, courseCode);
        jsonArray.forEach(index -> {
            workInfoList.add(userService.selectStudentWorkInfo(new StudentWorkInfo(courseCode, index.toString())));
        });
        if ("true".equals(studentWorkInfo.getDistributeStatus()) && null == studentWorkInfo.getJoinJudgeTime() && workInfoList.size() == 0)
            throw new Exception("您已经错过了互评机会");
        response.put("workList", workInfoList);
        return response;
    }

    /**
     * 提交互评作业
     *
     * @param judge
     * @param courseCode
     * @param request
     * @return
     */
    @RequestMapping("/commitJudgementInfo")
    @ResponseBody
    @ApiAnnotation
    public Object commitJudgementInfo(@RequestParam(value = "judge") String judge,
                                      @RequestParam(value = "courseCode") String courseCode,
                                      HttpServletRequest request) throws Exception {
        String email = CommonUtil.getEmailFromSession(request);
        if (null != userService.selectStudentWorkInfo(new StudentWorkInfo(courseCode, email)).getJoinJudgeTime())
            throw new Exception("您已经参加过互评");
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        int judgeLimitTimes = scheduleDto.getJudgeTimes();
        JSONObject judgeList = JSON.parseObject(judge);
        List<JudgementLs> judgementLsList = new ArrayList<>();
        judgeList.keySet().forEach(key -> {
            JSONObject jsonObject = (JSONObject) judgeList.get(key);
            judgementLsList.add(new JudgementLs(courseCode,
                    email, key, Double.valueOf(jsonObject.get("grade").toString())));
            List<JudgementLs> judgementLsList1 = judgementService.selectJudgementLs(new JudgementLs(courseCode, key));  //查询和这个人相关的互评流水
            if (judgementLsList1 != null && judgementLsList1.size() + 1 == judgeLimitTimes) {  //这个人被别人评价次数够了，计算他的最终分数
                double finalGrade = commonUtil.getMiddleNum(Double.valueOf(jsonObject.get("grade").toString()), judgementLsList1);
                judgementService.updateStuGrade(new StudentWorkInfo(courseCode, key, finalGrade));  //更新成绩
            }
        });
        judgementService.insertJudgementLs(judgementLsList);   //插入互评流水
        judgementService.updateStuJudgeTime(new StudentWorkInfo(courseCode, email, new Date()));  //更新这名用户参与互评的时间
        activitiHelper.completeTask(email, courseCode);
        return "提交评论结果成功";
    }

    /**
     * 查询所有的互评
     *
     * @return
     */
    @RequestMapping("/selectAllCommitJudgementInfo")
    @ResponseBody
    @ApiAnnotation
    public Object commitJudgementInfo(@RequestParam(value = "page", required = false, defaultValue = "1") long page,
                                      @RequestParam(value = "limit", required = false, defaultValue = "1") int limit,
                                      HttpServletRequest request) {
        String email = CommonUtil.getEmailFromSession(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", judgementService.selectCountJudge(email));
        jsonObject.put("list", judgementService.selectAllJudgementByEmail((page - 1) * limit, limit, email));
        return jsonObject;
    }

    /**
     * 查询学生成绩
     *
     * @return
     */
    @RequestMapping("/selectStudentGrade")
    @ResponseBody
    @ApiAnnotation
    public Object selectStudentGrade(HttpServletRequest request) {
        return userMapper.selectAllWorkInfo(CommonUtil.getEmailFromSession(request));
    }

    /**
     * 查询谁给我打了分数
     *
     * @return
     */
    @RequestMapping("/selectWhoJudgeMe")
    @ResponseBody
    @ApiAnnotation
    public Object selectWhoJudgeMe(@RequestParam(value = "courseCode") String courseCode, HttpServletRequest request) {
        String email = CommonUtil.getEmailFromSession(request);
        return judgementService.selectJudgementLs(new JudgementLs(courseCode, email));
    }

    /**
     * 删除管理员用户
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteUserRole")
    @ApiAnnotation
    public Object deleteUserRole(@RequestParam(value = "email") String email) {
        return userService.deleteUserRole(email);
    }

    /**
     * 添加管理员用户
     *
     * @param email
     * @param id
     * @param remarks
     * @return
     */
    @ResponseBody
    @RequestMapping("/addUserRole")
    @ApiAnnotation
    public Object addUserRole(@RequestParam(value = "email") String email,
                              @RequestParam(value = "id") int id,
                              @RequestParam(value = "remarks") String remarks) throws Exception {
        if (!commonUtil.emailFormat(email))
            throw new Exception("邮箱格式不正确");
        return userService.insertUserRole(new UserRole(id, email, remarks));
    }

    /**
     * 管理员查看学生成绩
     *
     * @param page
     * @param limit
     * @param courseCode
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/selectAllGradeInfoByAdmin")
    @ApiAnnotation
    public Object selectAllGradeInfoByAdmin(@RequestParam(value = "page", required = false, defaultValue = "1") long page,
                                            @RequestParam(value = "limit", required = false, defaultValue = "1") int limit,
                                            @RequestParam(value = "courseCode") String courseCode,
                                            HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", adminMapper.countAllGradeByCourseCode(courseCode));
        if (!(page == 1 && limit == 1))
            jsonObject.put("list", adminMapper.selectAllWorkInfoByCourseCode(courseCode, (page - 1) * limit, limit));
        return jsonObject;
    }

    /**
     * 查询管理员审查任务
     *
     * @param page
     * @param limit
     * @param status
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/selectMyJudgementWait")
    @ApiAnnotation
    public Object selectMyJudgementWait(@RequestParam(value = "page", required = false, defaultValue = "1") long page,
                                        @RequestParam(value = "limit", required = false, defaultValue = "1") int limit,
                                        @RequestParam(value = "status") String status,
                                        HttpServletRequest request) throws Exception {
        if (!commonUtil.isManageRole(CommonUtil.getEmailFromSession(request))) throw new Exception("非管理员不得查看！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", verifyTaskMapper.countAllTask(status));
        if (!(page == 1 && limit == 1)) {
            jsonObject.put("list", verifyTaskMapper.selectAllTask(status, (page - 1) * limit, limit));
        }
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("/insertAdminJudgementResult")
    @ApiAnnotation
    public Object insertAdminJudgementResult(@RequestParam(value = "email") String email,
                                             @RequestParam(value = "courseCode") String courseCode,
                                             @RequestParam(value = "grade") double grade,
                                             HttpServletRequest request) throws Exception {
        if (!commonUtil.isManageRole(CommonUtil.getEmailFromSession(request))) throw new Exception("非管理员不得查看！");
        String judgerEmail = CommonUtil.getEmailFromSession(request);
        verifyTaskMapper.updateTask(new VerifyTask(email, "done", courseCode, grade, judgerEmail));
        judgementService.updateStuGrade(new StudentWorkInfo(courseCode, email, grade));  //更新成绩
        return "更新成绩成功";
    }
}

