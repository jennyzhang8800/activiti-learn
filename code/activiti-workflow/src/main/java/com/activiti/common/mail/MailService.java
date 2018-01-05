package com.activiti.common.mail;

import javax.mail.MessagingException;

/**发送邮件接口
 * Created by 12490 on 2017/8/2.
 */
public interface MailService {

    void sendSimpleMail(String desAddr, String subject, String content);

    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    void sendAttachmentsMail(String to, String subject, String content, String filePat) throws MessagingException;

    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;
}
