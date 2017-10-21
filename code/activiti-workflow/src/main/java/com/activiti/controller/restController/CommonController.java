package com.activiti.controller.restController;

import com.activiti.common.aop.ApiAnnotation;
import com.activiti.common.utils.CommonUtil;
import com.activiti.mapper.ScheduleMapper;
import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.service.CommonService;
import com.activiti.service.ScheduleService;
import com.activiti.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by 12490 on 2017/8/14.
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {
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
        String courseCode = scheduleDto.getCourseCode();
        if (null != scheduleService.selectScheduleTime(courseCode)) throw new Exception(courseCode + "该课程已经存在");
        if (null == courseCode) throw new Exception("courseCode字段不能为空");
        if (!commonUtil.validateTime(scheduleDto)) throw new Exception("时间段配置错误");
        scheduleMapper.createTable(commonUtil.generateTableName(courseCode));
        scheduleService.insertScheduleTime(scheduleDto);
        commonUtil.addNewActivitiJob(scheduleDto);
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
            scheduleMapper.dropTable(commonUtil.generateTableName(courseCode));
            commonUtil.removeNewActivitiJob(scheduleService.selectScheduleTime(courseCode));
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
    @RequestMapping("/getStudentGradeAnalysis")
    @ApiAnnotation
    public Object getStudentGradeAnalysis(@RequestParam("courseCode") String courseCode) {
        return commonService.getStudentGradeAnalysis(courseCode);
    }
}
