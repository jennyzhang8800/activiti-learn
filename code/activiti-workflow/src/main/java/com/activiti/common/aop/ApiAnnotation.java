package com.activiti.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口統一出參注解
 * Created by 12490 on 2017/8/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiAnnotation {
    String[] validate() default {};  //必传参数
    boolean insertLog() default true;
}
