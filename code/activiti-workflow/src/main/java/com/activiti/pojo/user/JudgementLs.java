package com.activiti.pojo.user;

import com.activiti.common.utils.CommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 互評流水
 * Created by 12490 on 2017/8/20.
 */
public class JudgementLs implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private String courseCode;
    private String judgerEmail;  //评分人的Email
    private String nonJudgerEmail;  //被评人的Email
    private Double grade;    //分数
    private Date judgeTime;   //评分时间
    private String judgement;    //评语
    private String judgeTimeString;

    public JudgementLs() {
    }

    public JudgementLs(String courseCode, String nonJudgeEmail) {
        this.courseCode = courseCode;
        this.nonJudgerEmail = nonJudgeEmail;
    }

    public JudgementLs(String courseCode, String judgeEmail, String nonJudgeEmail, Double grade) {
        this.courseCode = courseCode;
        this.judgerEmail = judgeEmail;
        this.nonJudgerEmail = nonJudgeEmail;
        this.grade = grade;
        this.judgeTime=new Date();
    }

    public String getJudgeTimeString() {
        return judgeTimeString;
    }

    public void setJudgeTimeString(String judgeTimeString) {
        this.judgeTimeString = judgeTimeString;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getJudgeEmail() {
        return judgerEmail;
    }

    public void setJudgeEmail(String judgeEmail) {
        this.judgerEmail = judgeEmail;
    }

    public String getNonJudgeEmail() {
        return nonJudgerEmail;
    }

    public void setNonJudgeEmail(String nonJudgeEmail) {
        this.nonJudgerEmail = nonJudgeEmail;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Date getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(Date judgeTime) {
        this.judgeTime = judgeTime;
        this.judgeTimeString= CommonUtil.dateToString(judgeTime);
    }

    public String getJudgement() {
        return judgement;
    }

    public void setJudgement(String judgement) {
        this.judgement = judgement;
    }

    @Override
    public String toString() {
        return "JudgementLs{" +
                "courseCode='" + courseCode + '\'' +
                ", judgeEmail='" + judgerEmail + '\'' +
                ", nonJudgeEmail='" + nonJudgerEmail + '\'' +
                ", grade=" + grade +
                ", judgeTime=" + judgeTime +
                ", judgement='" + judgement + '\'' +
                '}';
    }
}
