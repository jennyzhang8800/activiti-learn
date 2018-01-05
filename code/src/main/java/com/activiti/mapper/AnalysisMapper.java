package com.activiti.mapper;

import com.activiti.pojo.tools.Analysis;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报表统计
 */
@Repository
public interface AnalysisMapper {

    /**
     * 查询学生数据做分析
     * @param courseCode
     * @return
     */
    List<Analysis> selectAllStudentWorkInfo(String courseCode);
}
