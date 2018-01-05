package com.activiti.service;

import com.activiti.pojo.user.JudgementLs;
import com.activiti.pojo.user.StudentWorkInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JudgementService {
    /**
     * 提交作业
     *
     * @param studentWorkInfo
     * @return
     */
    int commitWork(StudentWorkInfo studentWorkInfo);

    /**
     * 更新学生成绩
     *
     * @param studentWorkInfo
     * @return
     */
    int updateStuGrade(StudentWorkInfo studentWorkInfo);

    /**
     * 学生参与互评后更新学生互评时间
     *
     * @param studentWorkInfo
     * @return
     */
    int updateStuJudgeTime(StudentWorkInfo studentWorkInfo);

    /**
     * 插入学生互评记录
     *
     * @param list
     * @return
     */
    int insertJudgementLs(List<JudgementLs> list);

    /**
     * 查询学生作业信息
     *
     * @param studentWorkInfo
     * @return
     */
    StudentWorkInfo selectStudentWorkInfo(StudentWorkInfo studentWorkInfo);

    /**
     * 查询互评流水
     *
     * @param judgementLs
     * @return
     */
    List<JudgementLs> selectJudgementLs(JudgementLs judgementLs);

    /**
     * 查询提交作业的总人数
     *
     * @param tableName
     * @return
     */
    int countAllWorks(String tableName);

    /**
     * 查询打乱顺序后的学生ID
     *
     * @param emailAddress
     * @return
     */
    int selectChaosId(String emailAddress, String tableName);

    /**
     * 根据id在打乱的表里差Email
     *
     * @param userId
     * @param tableName
     * @return
     */
    String selectStudentWorkInfoChaosById(@Param("userId") String userId, @Param("tableName") String tableName);

    /**
     * 查询个人互评总数
     *
     * @param judgeEmail
     * @return
     */
    int selectCountJudge(String judgeEmail);

    /**
     * 查询互评流水，分页使用
     * @param offset
     * @param limit
     * @param judgeEmail
     * @return
     */
    List<JudgementLs> selectAllJudgementByEmail(@Param("offset") long offset, @Param("limit") int limit, String judgeEmail);
}
