package com.activiti.common.kafka;

import com.activiti.common.utils.ConstantsUtils;
import com.activiti.pojo.email.EmailDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka生产者发送邮件信息
 * Created by liulinhui on 2017/8/3.
 */
@Component
public class MailProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * kafka生成邮件信息
     *
     * @param emailDto
     */
    public void send(EmailDto emailDto) {
        String content = JSONObject.toJSONString(emailDto);
        kafkaTemplate.send(ConstantsUtils.emailTopic, content);
    }

}
