package com.activiti.service.serviceImpl;

import com.activiti.mapper.ScheduleMapper;
import com.activiti.pojo.schedule.ScheduleDto;
import com.activiti.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 12490 on 2017/8/19.
 */
@Service
@EnableCaching
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;


    @Override
//    @Cacheable(value = "ehCache60", keyGenerator = "keyGenerator")
    public ScheduleDto selectScheduleTime(String courseCode) {
        return scheduleMapper.selectScheduleTime(courseCode);
    }

    @Override
    public void insertScheduleTime(ScheduleDto scheduleDto) {
        scheduleMapper.insertScheduleTime(scheduleDto);
    }

    @Override
    public void updateScheduleTime(ScheduleDto scheduleDto) {
        scheduleMapper.updateScheduleTime(scheduleDto);
    }

    @Override
    public List<ScheduleDto> selectAllScheduleTime(long offset,int limit) {
        return scheduleMapper.selectAllScheduleTime(offset,limit);
    }
}
