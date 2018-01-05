package com.activiti.pojo.user;

import java.io.Serializable;

public class VerifyTask implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private String email;
    private String answer;
    private String status; //done:已完成  wait:待完成
    private String courseCode;  //课程代码
    private double grade;
    private int judgeTimes;    //被多少人评论了
    private String judgerEmail;  //老师邮箱

    public VerifyTask() {
    }

    public VerifyTask(String email, String answer, String courseCode, int judgeTimes) {
        this.email = email;
        this.answer = answer;
        this.courseCode = courseCode;
        this.judgeTimes = judgeTimes;
    }

    public VerifyTask(String email, String status, String courseCode, double grade, String judgerEmail) {
        this.email = email;
        this.status = status;
        this.courseCode = courseCode;
        this.grade = grade;
        this.judgerEmail = judgerEmail;
    }

    public String getJudgerEmail() {
        return judgerEmail;
    }

    public void setJudgerEmail(String judgerEmail) {
        this.judgerEmail = judgerEmail;
    }

    public int getJudgeTimes() {
        return judgeTimes;
    }

    public void setJudgeTimes(int judgeTimes) {
        this.judgeTimes = judgeTimes;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "VerifyTask{" +
                "email='" + email + '\'' +
                ", answer='" + answer + '\'' +
                ", status='" + status + '\'' +
                ", courseCode='" + courseCode + '\'' +
                '}';
    }
}
