package com.activiti.pojo.user;

import java.io.Serializable;

/**
 * 用户基本信息表
 * Created by 12490 on 2017/8/6.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private String userId;
    private String userName;
    private String emailAddress;
    private String abilityLevel;
    private String courseCode;

    public User() {
    }

    public User(String userName, String emailAddress,String courseCode) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.courseCode=courseCode;
    }

    public User(String userId, String userName, String emailAddress, String abilityLevel,String courseCode) {
        this.userId = userId;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.abilityLevel = abilityLevel;
        this.courseCode=courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAbilityLevel() {
        return abilityLevel;
    }

    public void setAbilityLevel(String abilityLevel) {
        this.abilityLevel = abilityLevel;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", abilityLevel='" + abilityLevel + '\'' +
                '}';
    }
}
