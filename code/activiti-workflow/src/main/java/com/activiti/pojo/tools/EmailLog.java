package com.activiti.pojo.tools;


import com.activiti.common.utils.CommonUtil;
import com.activiti.pojo.email.EmailDto;

import java.io.Serializable;
import java.util.Date;

public class EmailLog implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private String receiveAddress;  //收件人
    private String sendAddress;  //发件人
    private String subject;    //主题
    private String content;     //内容
    private String rscPath;      //资源地址
    private String rscId;       //资源id
    private Date sendTime;       //发送时间
    private String sendTimeString;       //发送时间
    private String status;  //发送状态  0:失败  1:成功

    public EmailLog(EmailDto emailDto, String status, String sendAddress) {
        this.content = emailDto.getContent();
        this.receiveAddress = emailDto.getAddress();
        this.subject = emailDto.getSubject();
        this.rscId = emailDto.getRscId();
        this.rscPath = emailDto.getRscPath();
        this.status = status;
        this.sendTime = new Date();
        this.sendAddress = sendAddress;
    }

    public EmailLog() {
    }

    public String getSendTimeString() {
        return sendTimeString;
    }

    public void setSendTimeString(String sendTimeString) {
        this.sendTimeString = sendTimeString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRscPath() {
        return rscPath;
    }

    public void setRscPath(String rscPath) {
        this.rscPath = rscPath;
    }

    public String getRscId() {
        return rscId;
    }

    public void setRscId(String rscId) {
        this.rscId = rscId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
        this.sendTimeString = CommonUtil.dateToString(sendTime);
    }

    @Override
    public String toString() {
        return "EmailLog{" +
                "receiveAddress='" + receiveAddress + '\'' +
                ", sendAddress='" + sendAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", rscPath='" + rscPath + '\'' +
                ", rscId='" + rscId + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
