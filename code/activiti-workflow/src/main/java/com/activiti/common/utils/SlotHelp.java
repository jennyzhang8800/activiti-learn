package com.activiti.common.utils;

import com.activiti.common.quartz.QuartzManager;
import com.activiti.mapper.ScheduleMapper;
import com.activiti.mapper.UserMapper;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.pojo.user.StudentWorkInfo;
import com.alibaba.fastjson.JSONArray;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlotHelp implements ApplicationContextAware {
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ActivitiHelper activitiHelper;
    private static final String cron = "0 0 0 * * *";  //每天执行一次


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        executeMe();
    }

    @Scheduled(cron = cron)
    private void executeMe() {
        List<ScheduleDto> scheduleDtoList = scheduleMapper.selectAllOfScheduleTime();
        scheduleDtoList.forEach(scheduleDto -> {
            String courseCode = scheduleDto.getCourseCode();
            List<StudentWorkInfo> studentWorkInfoList = userMapper.selectAllNonDistributeUser(courseCode);
            if (null != studentWorkInfoList && studentWorkInfoList.size() > 0) {
                DateTime last = new DateTime(studentWorkInfoList.get(0).getLastCommitTime());
                DateTime first = new DateTime(studentWorkInfoList.get(studentWorkInfoList.size() - 1).getLastCommitTime());
                int maxTime = commonUtil.getNumFromString(scheduleDto.getAssessmentMaxTimeSlot());
                int diff = 0;
                if (scheduleDto.getAssessmentMinTimeSlot().contains("M") && scheduleDto.getAssessmentMaxTimeSlot().contains("M"))
                    diff = Minutes.minutesBetween(first, last).getMinutes();
                if (scheduleDto.getAssessmentMinTimeSlot().contains("D") && scheduleDto.getAssessmentMaxTimeSlot().contains("D"))
                    diff = Days.daysBetween(first, last).getDays();
                if (scheduleDto.getAssessmentMinTimeSlot().contains("S") && scheduleDto.getAssessmentMaxTimeSlot().contains("S"))
                    diff = Seconds.secondsBetween(first, last).getSeconds();
                if (diff > maxTime) {
                    studentWorkInfoList.forEach(studentWorkInfo -> {
                        userMapper.updateDistributeStatus(courseCode, studentWorkInfo.getEmailAddress());
                        activitiHelper.startTeacherVerify(studentWorkInfo);
                    });
                }
            }
        });
    }
}
