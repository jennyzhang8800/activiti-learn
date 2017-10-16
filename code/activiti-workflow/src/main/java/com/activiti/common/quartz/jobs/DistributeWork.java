package com.activiti.common.quartz.jobs;

import com.activiti.common.quartz.QuartzManager;
import com.activiti.common.utils.CommonUtil;
import com.activiti.mapper.ScheduleMapper;
import com.activiti.pojo.schedule.ScheduleDto;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 任务管理
 * Created by 12490 on 2017/8/19.
 */
@Component
public class DistributeWork implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DistributeWork.class);

    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private CommonUtil commonUtil;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        JobCollection.commonUtil = (CommonUtil)applicationContext.getBean("CommonUtil");
        DateTime nowDate = new DateTime(new Date());
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.secondOfDay());
        scheduleMapper.selectAllOfScheduleTime().forEach(scheduleDto -> {
            if (comparator.compare(nowDate, new DateTime(scheduleDto.getPublishTime())) < 0) {
                scheduleDtoList.add(scheduleDto);
            }
        });
        scheduleDtoList.forEach(scheduleDto -> {
            commonUtil.addNewActivitiJob(scheduleDto);
        });
    }
}
