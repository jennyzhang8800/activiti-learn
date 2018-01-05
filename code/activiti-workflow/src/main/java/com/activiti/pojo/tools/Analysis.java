package com.activiti.pojo.tools;

import java.io.Serializable;
import java.util.Date;

public class Analysis implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private Date lastCommitTime;
    private double grade;

    public Date getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(Date lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
