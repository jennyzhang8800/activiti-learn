package com.activiti.mapper;

import com.activiti.pojo.user.StudentWorkInfo;
import com.activiti.pojo.user.User;
import com.activiti.pojo.user.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生相关的操作
 * Created by 12490 on 2017/8/1.
 */
@Repository
public interface UserMapper {

    /**
     * 查询学生基础信息
     *
     * @param emailAddr
     * @return
     */
    User findUserInfo(String emailAddr);

    /**
     * 插入学生信息
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 插入学生作业信息
     *
     * @param studentWorkInfo
     * @return
     */
    int insertUserWork(StudentWorkInfo studentWorkInfo);

    /**
     * 查询学生作业信息
     *
     * @param studentWorkInfo
     * @return
     */
    StudentWorkInfo selectStudentWorkInfo(StudentWorkInfo studentWorkInfo);

    /**
     * 查询学生作业信息(所有课程)
     *
     * @param email
     * @param offset
     * @param limit
     * @return
     */
    List<StudentWorkInfo> selectStudentWorkInfoPage(@Param("email") String email, @Param("offset") long offset, @Param("limit") int limit);

    /**
     * 查询所有课程成绩
     * @param email
     * @return
     */
    List<StudentWorkInfo> selectAllWorkInfo(String email);

    /**
     * 学生总数量
     *
     * @return
     */
    long countStudentWorkInfo(String email);

    /**
     * 查询没有分数的学生的email
     *
     * @param courseCode
     * @return
     */
    List<StudentWorkInfo> selectNoGradeUser(String courseCode);

    /**
     * 查询没有参加完互评的学生
     *
     * @param courseCode
     * @return
     */
    List<StudentWorkInfo> selectUnFinishJudgeUser(String courseCode);

    /**
     * 查询所有学生成绩
     *
     * @param courseCode
     * @return
     */
    List<StudentWorkInfo> selectAllUserWork(String courseCode);

    /**
     * 打乱学生信息表
     *
     * @return
     */
    int chaosUserInfo(@Param("tableName") String tableName, @Param("courseCode") String courseCode);

    /**
     * 删除混序学生表
     *
     * @return
     */
    int deleteChaosUserInfo(@Param("tableName") String tableName);

    /**
     * 插入用户角色
     *
     * @param userRole
     * @return
     */
    int insertUserRole(UserRole userRole);

    /**
     * 查询所有用户角色
     *
     * @return
     */
    List<UserRole> selectAllUserRole();

    /**
     * 删除用户角色
     *
     * @param email
     * @return
     */
    int deleteUserRole(String email);

    /**
     * 查询参加了课程的所有学生
     * @param courseCode
     * @return
     */
    List<String> selectAllStuInCourse(String courseCode);
}
