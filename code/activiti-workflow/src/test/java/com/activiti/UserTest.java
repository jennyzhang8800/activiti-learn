package com.activiti;

import com.activiti.mapper.UserMapper;
import com.activiti.pojo.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 12490 on 2017/8/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    private static Logger logger = LoggerFactory.getLogger(UserTest.class);
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertUser() {
        for (int i = 0; i < 10; i++)
            logger.info("返回的主键=" + userMapper.insertUser(CommonTestUtil.getUserInfo()));
    }
}
