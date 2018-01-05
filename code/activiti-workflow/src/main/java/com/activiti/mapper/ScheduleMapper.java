package com.activiti.mapper;

import com.activiti.pojo.schedule.ScheduleDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课程各个阶段时间表
 * Created by 12490 on 2017/8/19.
 */
@Repository
public interface ScheduleMapper {

    /**
     * 查詢指定課程的時間表
     * @param courseCode
     * @return
     */
    ScheduleDto selectScheduleTime(String courseCode);

    /**
     * 插入制定课程时间表
     * @param scheduleDto
     */
    void insertScheduleTime(ScheduleDto scheduleDto);

    /**
     * 更新指定课程时间表
     * @param scheduleDto
     */
    void updateScheduleTime(ScheduleDto scheduleDto);

    /**
     * 查询所有课程的时间表
     * @return
     */
    List<ScheduleDto> selectAllScheduleTime(@Param("offset")long offset, @Param("limit") int limit);

    /**
     * 删除课程
     * @param courseCode
     * @return
     */
    int deleteCourse(String courseCode);

    /**
     * 查询所有课程
     * @return
     */
    List<ScheduleDto> selectAllOfScheduleTime();

    /**
     * 计算课程总数
     * @return
     */
    long countAllScheduleTime();

    /**
     * 每个课程新建一张表
     * @param tableName
     * @return
     */
    int createTable(@Param("tableName")String tableName);

    /**
     * 删除课程相关的表
     * @param tableName
     * @return
     */
    int dropTable(@Param("tableName")String tableName);

}
