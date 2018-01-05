package com.activiti.common.kafka;

import com.activiti.common.utils.CommonUtil;
import com.activiti.common.utils.ConstantsUtils;
import com.activiti.mapper.ToolsMapper;
import com.activiti.pojo.email.EmailDto;
import com.activiti.pojo.tools.EmailLog;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

/**
 * kafka 消费者监听器
 * Created by liulinhui on 2017/8/3.
 */
@Configuration
public class Listener {
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    @Value(value = "${spring.mail.username}")
    private String sendEmail;

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ToolsMapper toolsMapper;


    /**
     * Kafka消費者发送邮件
     *
     * @param record 消息
     */
    @KafkaListener(topics = {ConstantsUtils.emailTopic})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            EmailDto emailDto = JSON.parseObject((String) message, EmailDto.class);
            try {
                commonUtil.sendEmail(emailDto);
                toolsMapper.insertEmailLog(new EmailLog(emailDto, "success", sendEmail));
            } catch (Exception e) {
                toolsMapper.insertEmailLog(new EmailLog(emailDto, "fail", sendEmail));
            }
        }
    }
}
