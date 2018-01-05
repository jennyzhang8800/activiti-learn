package com.activiti;

import com.activiti.pojo.user.User;
import com.activiti.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by 12490 on 2017/8/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class CacheTest {
    @Autowired
    private UserService userService;

    @Test
    public void putGetTest() throws InterruptedException {
        Long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 60000) {
            userService.findUserInfo("1249055292@qq.com");
            System.out.println((System.currentTimeMillis() - start) / 1000);
            Thread.sleep(1000);
        }
    }
}
