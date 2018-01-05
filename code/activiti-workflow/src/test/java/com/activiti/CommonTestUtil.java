package com.activiti;

import com.activiti.pojo.user.User;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by 12490 on 2017/8/20.
 */
public class CommonTestUtil {

    public static User getUserInfo() {
        return new User("1", "fucker",getRandomString(9), "good","S12345");
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
