package com.activiti.controller.restController;

import com.activiti.common.aop.ApiAnnotation;
import com.activiti.common.redis.RedisCommonUtil;
import com.activiti.common.utils.CommonUtil;
import com.activiti.common.utils.ConstantsUtils;
import com.activiti.common.utils.HttpClientUtil;
import com.activiti.mapper.ScheduleMapper;
import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.pojo.user.UserRole;
import com.activiti.service.CommonService;
import com.activiti.service.ScheduleService;
import com.activiti.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;


/**
 * Created by 12490 on 2017/8/14.
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ToolsMapper toolsMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private RedisCommonUtil redisCommonUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Autowired
    RepositoryService repositoryService;

    /**
     * GitHub请求题目和答案
     *
     * @param courseCode
     * @return
     */
    @RequestMapping("/getQAContent")
    @ResponseBody
    @ApiAnnotation
    public Object getQAFromGitHub(@RequestParam(value = "courseCode") String courseCode) throws Exception {
        ScheduleDto scheduleDto = scheduleMapper.selectScheduleTime(courseCode);
        if (null == scheduleDto) throw new Exception("本题目已经被老师撤销了！！！");
        String githubAddress = scheduleDto.getGithubAddress();
        return commonService.getQAFromGitHub(githubAddress).get("question");
    }

    /**
     * 获取已经部署得流程实例
     *
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping("/getProcessLists")
    @ResponseBody
    @ApiAnnotation
    public Object getProcessLists(@RequestParam(value = "page") int page,
                                  @RequestParam(value = "limit") int limit) throws Exception {
        JSONObject result = new JSONObject();
        int total = 2;
        result.put("total", total);
        int firstRow = (page - 1) * limit;
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().listPage(firstRow, limit);
        JSONArray jsonArray = new JSONArray();
        list.forEach(processDefinition -> {
            if ("answerToAssessment".equals(processDefinition.getKey()) || "answerToAssessmentNojudge".equals(processDefinition.getKey())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deploymentId", processDefinition.getDeploymentId());
                jsonObject.put("id", processDefinition.getId());
                jsonObject.put("name", processDefinition.getName());
                jsonObject.put("resourceName", processDefinition.getResourceName());
                jsonObject.put("key", processDefinition.getKey());
                jsonObject.put("diagramResourceName", processDefinition.getDiagramResourceName());
                jsonArray.add(jsonObject);
            }
        });
        result.put("data", jsonArray);
        return result;
    }

    /**
     * 查询指定课程的互评时间表
     *
     * @param courseCode
     * @return
     */
    @RequestMapping("/selectScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object selectScheduleTime(@RequestParam(value = "courseCode") String courseCode) {
        return scheduleService.selectScheduleTime(courseCode);
    }

    /**
     * 插入指定课程的互评时间
     *
     * @param param
     * @return
     */
    @RequestMapping("/insertScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object insertScheduleTime(@RequestParam(value = "data") String param) throws Exception {
        JSONObject jsonObject = JSON.parseObject(param);
        ScheduleDto scheduleDto = jsonObject.toJavaObject(ScheduleDto.class);
        scheduleDto.setGithubAddress(commonUtil.generateGitHubUrl(Integer.valueOf(scheduleDto.getCourseCode())));
        String courseCode = scheduleDto.getCourseCode();
        commonUtil.validateTimeRegexp(scheduleDto);
        if (null != scheduleService.selectScheduleTime(courseCode)) throw new Exception(courseCode + "该课程已经存在");
        if (null == courseCode) throw new Exception("courseCode字段不能为空");
        scheduleService.insertScheduleTime(scheduleDto);
        return "课程部署成功";
    }

    /**
     * 移除指定课程的互评时间
     *
     * @param courseCode
     * @return
     */
    @RequestMapping("/removeScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object removeScheduleTime(@RequestParam(value = "courseCode") String courseCode, HttpServletRequest request) throws Exception {
        String email = CommonUtil.getEmailFromSession(request);
        boolean identity = false;
        if (commonUtil.isManageRole(email)) {
            identity = true;
        }
        if (identity) {
            scheduleMapper.deleteCourse(courseCode);
            return "课程移除成功";
        } else {
            throw new Exception(email + "不是管理员，无法进行此操作！");
        }
    }

    /**
     * 更新指定课程的互评时间
     *
     * @param scheduleDto
     * @return
     */
    @RequestMapping("/updateScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object updateScheduleTime(ScheduleDto scheduleDto) throws Exception {
        String courseCode = scheduleDto.getCourseCode();
        if (null == courseCode) throw new Exception("courseCode字段不能为空");
        scheduleService.updateScheduleTime(scheduleDto);
        return scheduleService.selectScheduleTime(courseCode);
    }

    /**
     * 查询所有课程相关的时间表
     *
     * @return
     */
    @RequestMapping("/selectAllScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object selectAllScheduleTime(@RequestParam(value = "page") long page,
                                        @RequestParam(value = "limit") int limit) {
        List<ScheduleDto> scheduleDtoList = scheduleService.selectAllScheduleTime((page - 1) * limit, limit);
        return scheduleDtoList;
    }

    /**
     * 查询所有课程相关的时间表
     *
     * @return
     */
    @RequestMapping("/countAllScheduleTime")
    @ResponseBody
    @ApiAnnotation
    public Object countAllScheduleTime() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", scheduleMapper.countAllScheduleTime());
        return jsonObject;
    }

    /**
     * 日志查询
     *
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectInvokeLog")
    @ApiAnnotation(insertLog = false)
    public Object selectInvokeLog(@RequestParam(value = "page") long page,
                                  @RequestParam(value = "limit") int limit) {
        return toolsMapper.selectInvokeLog((page - 1) * limit, limit);
    }

    /**
     * 查询总页数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/countInvokeLog")
    @ApiAnnotation(insertLog = false)
    public Object countInvokeLog() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", toolsMapper.countInvokeLog());
        return jsonObject;
    }

    /**
     * 邮件日志查询
     *
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectEmailLog")
    @ApiAnnotation(insertLog = false)
    public Object selectEmailLog(@RequestParam(value = "page") long page,
                                 @RequestParam(value = "limit") int limit) {
        return toolsMapper.selectEmailLog((page - 1) * limit, limit);
    }

    /**
     * 邮件日志查询总页数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/countEmailLog")
    @ApiAnnotation(insertLog = false)
    public Object countEmailLog() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", toolsMapper.countEmailLog());
        return jsonObject;
    }

    /**
     * 学生提交时间分析
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStudentCommitTimeAnalysis")
    @ApiAnnotation
    public Object getStudentCommitTimeAnalysis(@RequestParam("courseCode") String courseCode) {
        return commonService.getStudentCommitTimeAnalysis(courseCode);
    }

    /**
     * 学生成绩分析
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStudentCommitGradeAnalysis")
    @ApiAnnotation
    public Object getStudentCommitGradeAnalysis(@RequestParam("courseCode") String courseCode) {
        return commonService.getStudentCommitGradeAnalysis(courseCode);
    }

    /**
     * 学生成绩分析
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStudentGradeAnalysis")
    @ApiAnnotation
    public Object getStudentGradeAnalysis(@RequestParam("courseCode") String courseCode) {
        return commonService.getStudentGradeAnalysis(courseCode);
    }

    /**
     * 对接
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/loginAbutment")
    @ApiAnnotation
    public Object loginAbutment(@RequestParam("email") String email,
                                @RequestParam("userName") String userName,
                                @RequestParam("userType") String userType,
                                @RequestParam("signature") String signature) throws Exception {
        String mySignature = httpClientUtil.getMD5(email + userName + userType + ConstantsUtils.password).toLowerCase();
        logger.info("loginAbutment get signature=" + signature + ",mySignature=" + mySignature);
        if (!signature.equals(mySignature)) throw new Exception("非法登录对接！！！！");
        String uuid = String.valueOf(commonUtil.getSequenceId());
        JSONObject cache = new JSONObject();
        cache.put("uuid", uuid);
        cache.put("userName", userName);
        cache.put("userType", userType);
        cache.put("email", email);
        logger.info("loginAbutment parameter is>>>>>>>>>>>>" + cache.toJSONString());
        redisCommonUtil.put(ConstantsUtils.loginAbutmentRedisStore + email, cache.toJSONString(), 5);
        if ("staff".equals(userType))
            userService.insertUserRole(new UserRole(1, email, "staff"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid);
        return jsonObject;
    }

    /**
     * 查询课程是否可以申诉
     *
     * @param courseCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/isCourseAppeal")
    @ApiAnnotation
    public Object isCourseAppeal(@RequestParam("courseCode") String courseCode) {
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAppeal", scheduleDto.getIsAppeal());
        return jsonObject;
    }

    /**
     * 题目详情
     *
     * @param courseCode
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping("/selectCourseDetails")
    @ApiAnnotation
    public Object selectCourseDetails(@RequestParam("courseCode") String courseCode) throws UnsupportedEncodingException {
        ScheduleDto scheduleDto = scheduleService.selectScheduleTime(courseCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("details", commonService.getQAFromGitHub(scheduleDto.getGithubAddress()));
        return jsonObject;
    }
}
