package com.activiti.pojo.user;

import com.activiti.common.utils.CommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生作业相关
 * Created by 12490 on 2017/8/20.
 */
public class StudentWorkInfo implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private String courseCode;
    private String emailAddress;
    private String workDetail;
    private Date lastCommitTime;
    private String lastCommitTimeString;
    private Double grade;
    private String judgeType;  //teacher：老師评分  student:学生互评
    private int judgeTimes;      //作业被评论次数
    private Date joinJudgeTime;   //参与互评的时间
    private String joinJudgeTimeString;

    public String getLastCommitTimeString() {
        return lastCommitTimeString;
    }

    public void setLastCommitTimeString(String lastCommitTimeString) {
        this.lastCommitTimeString = lastCommitTimeString;
    }

    public StudentWorkInfo() {
    }

    public StudentWorkInfo(String courseCode, String emailAddress, Double grade) {
        this.courseCode = courseCode;
        this.emailAddress = emailAddress;
        this.grade = grade;
    }

    public StudentWorkInfo(String courseCode, String emailAddress) {
        this.courseCode = courseCode;
        this.emailAddress = emailAddress;
    }

    public StudentWorkInfo(String courseCode, String emailAddress, String workDetail, Date lastCommitTime) {
        this.courseCode = courseCode;
        this.emailAddress = emailAddress;
        this.workDetail = workDetail;
        this.lastCommitTime = lastCommitTime;
    }

    public StudentWorkInfo(String courseCode, String emailAddress, Date joinJudgeTime) {
        this.courseCode = courseCode;
        this.emailAddress = emailAddress;
        this.joinJudgeTime = joinJudgeTime;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getJoinJudgeTimeString() {
        return joinJudgeTimeString;
    }

    public void setJoinJudgeTimeString(String joinJudgeTimeString) {
        this.joinJudgeTimeString = joinJudgeTimeString;
    }

    public String getWorkDetail() {
        return workDetail;
    }

    public void setWorkDetail(String workDetail) {
        this.workDetail = workDetail;
    }

    public Date getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(Date lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
        this.lastCommitTimeString= CommonUtil.dateToString(lastCommitTime);
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    public int getJudgeTimes() {
        return judgeTimes;
    }

    public void setJudgeTimes(int judgeTimes) {
        this.judgeTimes = judgeTimes;
    }

    public Date getJoinJudgeTime() {
        return joinJudgeTime;
    }

    public void setJoinJudgeTime(Date joinJudgeTime) {
        this.joinJudgeTime = joinJudgeTime;
        this.joinJudgeTimeString=CommonUtil.dateToString(joinJudgeTime);
    }

    @Override
    public String toString() {
        return "StudentWorkInfo{" +
                "courseCode='" + courseCode + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", workDetail='" + workDetail + '\'' +
                ", lastCommitTime=" + lastCommitTime +
                ", grade=" + grade +
                ", judgeType='" + judgeType + '\'' +
                ", judgeTimes=" + judgeTimes +
                '}';
    }
}
