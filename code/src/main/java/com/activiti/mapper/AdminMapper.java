package com.activiti.mapper;

import com.activiti.pojo.user.StudentWorkInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {

    /**
     * 查询课程总人数
     * @param courseCode
     * @return
     */
    long countAllGradeByCourseCode(String courseCode);

    /**
     * 分页查询课程答题成绩信息
     * @param courseCode
     * @param offset
     * @param limit
     * @return
     */
    List<StudentWorkInfo>selectAllWorkInfoByCourseCode(@Param("courseCode") String courseCode,@Param("offset")long offset,@Param("limit")int limit);
}
