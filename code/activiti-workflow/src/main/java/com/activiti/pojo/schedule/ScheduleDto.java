package com.activiti.pojo.schedule;

import com.activiti.common.utils.CommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 互评时间表
 * Created by 12490 on 2017/8/6.
 */
public class ScheduleDto implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private String courseName;  //课程名称
    private String courseCode;  //课程代码
    private Date judgeStartTime; //互评开始时间
    private Date judgeEndTime;   //互评结束时间
    private Date auditStartTime;  //学生审查开始时间
    private Date auditEndTime;   //学生审查结束时间
    private Date publishTime;    //发布成绩时间
    private int judgeTimes;    //每个学生需要互评的次数（后期算出）
    private String githubAddress;   //题目地址
    private int distributeMaxUser;  //最大人数开始互拼
    private String timeout;  //互评超时时间
    private String isAppeal;   //是否允许申诉    yes:允许   no:不允许   默认不允许

    public ScheduleDto() {
    }

    public String getIsAppeal() {
        return isAppeal;
    }

    public void setIsAppeal(String isAppeal) {
        this.isAppeal = isAppeal;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public int getJudgeTimes() {
        return judgeTimes;
    }

    public void setJudgeTimes(int judgeTimes) {
        this.judgeTimes = judgeTimes;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Date getJudgeStartTime() {
        return judgeStartTime;
    }

    public void setJudgeStartTime(Date judgeStartTime) {
        this.judgeStartTime = judgeStartTime;
        this.judgeStartTimeString = CommonUtil.dateToString(judgeStartTime);
    }

    public Date getJudgeEndTime() {
        return judgeEndTime;
    }

    public void setJudgeEndTime(Date judgeEndTime) {
        this.judgeEndTime = judgeEndTime;
        this.judgeEndTimeString = CommonUtil.dateToString(judgeEndTime);
    }

    public Date getAuditStartTime() {
        return auditStartTime;
    }

    public void setAuditStartTime(Date auditStartTime) {
        this.auditStartTime = auditStartTime;
        this.auditStartTimeString = CommonUtil.dateToString(auditStartTime);
    }

    public Date getAuditEndTime() {
        return auditEndTime;
    }

    public void setAuditEndTime(Date auditEndTime) {
        this.auditEndTime = auditEndTime;
        this.auditEndTimeString = CommonUtil.dateToString(auditEndTime);
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
        this.publishTimeString = CommonUtil.dateToString(publishTime);
    }

    public String getGithubAddress() {
        return githubAddress;
    }

    public void setGithubAddress(String githubAddress) {
        this.githubAddress = githubAddress;
    }

    private String startTimeString;      //提交作业开始时间
    private String commitEndTimeString;  //提交作业结束时间
    private String judgeStartTimeString; //互评开始时间
    private String judgeEndTimeString;   //互评结束时间
    private String auditStartTimeString;  //学生审查开始时间
    private String auditEndTimeString;   //学生审查结束时间
    private String publishTimeString;    //发布成绩时间

    public int getDistributeMaxUser() {
        return distributeMaxUser;
    }

    public void setDistributeMaxUser(int distributeMaxUser) {
        this.distributeMaxUser = distributeMaxUser;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getCommitEndTimeString() {
        return commitEndTimeString;
    }

    public void setCommitEndTimeString(String commitEndTimeString) {
        this.commitEndTimeString = commitEndTimeString;
    }

    public String getJudgeStartTimeString() {
        return judgeStartTimeString;
    }

    public void setJudgeStartTimeString(String judgeStartTimeString) {
        this.judgeStartTimeString = judgeStartTimeString;
    }

    public String getJudgeEndTimeString() {
        return judgeEndTimeString;
    }

    public void setJudgeEndTimeString(String judgeEndTimeString) {
        this.judgeEndTimeString = judgeEndTimeString;
    }

    public String getAuditStartTimeString() {
        return auditStartTimeString;
    }

    public void setAuditStartTimeString(String auditStartTimeString) {
        this.auditStartTimeString = auditStartTimeString;
    }

    public String getAuditEndTimeString() {
        return auditEndTimeString;
    }

    public void setAuditEndTimeString(String auditEndTimeString) {
        this.auditEndTimeString = auditEndTimeString;
    }

    public String getPublishTimeString() {
        return publishTimeString;
    }

    public void setPublishTimeString(String publishTimeString) {
        this.publishTimeString = publishTimeString;
    }

    @Override
    public String toString() {
        return "ScheduleDto{" +
                "courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", judgeStartTime=" + judgeStartTime +
                ", judgeEndTime=" + judgeEndTime +
                ", auditStartTime=" + auditStartTime +
                ", auditEndTime=" + auditEndTime +
                ", publishTime=" + publishTime +
                ", judgeTimes=" + judgeTimes +
                '}';
    }
}
