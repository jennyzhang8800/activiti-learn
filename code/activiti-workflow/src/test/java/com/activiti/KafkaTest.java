package com.activiti;

import com.activiti.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Kafka测试类
 * Created by liulinhui on 2017/8/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private UserService userService;

    @Test
    public void producerTest() throws InterruptedException {
        String data=JSONObject.toJSONString(CommonTestUtil.getUserInfo());
        kafkaTemplate.send("FUCKING_TEST", data);
        Thread.sleep(5000);
    }

}
