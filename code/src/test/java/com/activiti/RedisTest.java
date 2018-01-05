package com.activiti;

import com.activiti.pojo.user.User;
import com.activiti.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by 12490 on 2017/8/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    /**
     * 简单键值对存储测试
     * @throws Exception
     */
    @Test
    public void setGetTest() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }

    /**
     * 存储对象测试
     * @throws Exception
     */
    @Test
    public void testObj() throws Exception {
        User user=new User("123", "aa", "243", "aa","S12345");
        ValueOperations<String, User> operations=redisTemplate.opsForValue();
        operations.set("user", user);
        operations.set("user_expire", user,100, TimeUnit.SECONDS);
        Thread.sleep(1000);
        //redisTemplate.delete("com.neo.f");
        boolean exists=redisTemplate.hasKey("user_expire");
        if(exists){
            System.out.println("exists is true");
        }else{
            System.out.println("exists is false");
        }
         Assert.assertEquals("aa", operations.get("user_expire").getUserName());
    }

    @Test
    public void ORMCacheTest(){
        for (int i=0;i<1000;i++){
            System.out.println(JSONObject.toJSONString(userService.findUserInfo("1249055292@qq.com")));
        }
    }
}
