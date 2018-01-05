package com.activiti.service.serviceImpl;

import com.activiti.mapper.UserMapper;
import com.activiti.pojo.user.StudentWorkInfo;
import com.activiti.pojo.user.User;
import com.activiti.pojo.user.UserRole;
import com.activiti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 12490 on 2017/8/6.
 */
@Service
@EnableCaching
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable(value = "ehCache60", keyGenerator = "keyGenerator")
    public User findUserInfo(String email_address) {
        return userMapper.findUserInfo(email_address);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int insertUserWork(StudentWorkInfo studentWorkInfo) {
        return userMapper.insertUserWork(studentWorkInfo);
    }

    @Override
    public StudentWorkInfo selectStudentWorkInfo(StudentWorkInfo studentWorkInfo) {
        return userMapper.selectStudentWorkInfo(studentWorkInfo);
    }

    @Override
    public List<StudentWorkInfo> selectNoGradeUser(String courseCode) {
        return userMapper.selectNoGradeUser(courseCode);
    }

    @Override
    public List<StudentWorkInfo> selectAllUserWork(String courseCode) {
        return userMapper.selectAllUserWork(courseCode);
    }

    @Override
    public List<StudentWorkInfo> selectUnFinishJudgeUser(String courseCode) {
        return userMapper.selectUnFinishJudgeUser(courseCode);
    }

    @Override
    public int chaosUserInfo(String tableName, String courseCode) {
        return userMapper.chaosUserInfo(tableName, courseCode);
    }

    @Override
    public int deleteChaosUserInfo(String tableName) {
        return userMapper.deleteChaosUserInfo(tableName);
    }

    @Override
    public int insertUserRole(UserRole userRole) {
        return userMapper.insertUserRole(userRole);
    }

    @Override
    public List<UserRole> selectAllUserRole() {
        return userMapper.selectAllUserRole();
    }

    @Override
    public int deleteUserRole(String email) {
        return userMapper.deleteUserRole(email);
    }

    @Override
    public List<String> selectAllStuInCourse(String courseCode) {
        return userMapper.selectAllStuInCourse(courseCode);
    }
}
