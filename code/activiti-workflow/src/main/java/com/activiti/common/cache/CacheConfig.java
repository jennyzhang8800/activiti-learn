package com.activiti.common.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * ehCache配置
 * Created by 12490 on 2017/8/6.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * key生成器
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append("_");
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
}
