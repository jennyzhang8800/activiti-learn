package com.activiti.service;

import com.activiti.pojo.schedule.ScheduleDto;

import java.util.List;

/**
 * Created by 12490 on 2017/8/19.
 */
public interface ScheduleService {

    ScheduleDto selectScheduleTime(String courseCode);

    void insertScheduleTime(ScheduleDto scheduleDto);

    void updateScheduleTime(ScheduleDto scheduleDto);

    List<ScheduleDto> selectAllScheduleTime(long offset,int limit);
}
