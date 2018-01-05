package com.activiti.common.interceptor;

import com.activiti.common.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器注册
 */

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new LoginInterceptor(env))
                .addPathPatterns("/**")
                .excludePathPatterns(ConstantsUtils.excludePathPatterns);
        registry.addInterceptor(new CommonInterceptor(env))
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mooc-workflow/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

}
