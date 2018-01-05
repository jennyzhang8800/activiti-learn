package com.activiti;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexpTest {
    public static void main(String[] args) {
        String pattern = "PT(\\d+)[SMHD]";
        String a="PT23D";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(a); // 获取 matcher 对象
        System.out.println(m.find());
        System.out.println(m.start());
        System.out.println(m.end());
        System.out.println(a.matches(pattern));
    }

}
