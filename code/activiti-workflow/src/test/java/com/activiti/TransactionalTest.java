package com.activiti;

import com.activiti.mapper.UserMapper;
import com.activiti.pojo.user.User;
import com.activiti.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransactionalTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void transactionalTest() throws Exception {
        User user = new User("qwe", "qweqwe", "eqweqw", "rewrwer","S12345");
        userMapper.insertUser(user);
        throw new Exception();
    }
}
