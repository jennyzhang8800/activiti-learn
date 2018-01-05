package com.activiti.common.aop;

import com.activiti.common.async.AsyncTasks;
import com.activiti.common.utils.CommonUtil;
import com.activiti.pojo.restApiDto.RestApiResponse;
import com.activiti.pojo.tools.InvokeLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 接口统一出参注解
 * Created by 12490 on 2017/8/19.
 */
@Aspect
@Component
public class ApiAspect {
    private static final Logger logger = LoggerFactory.getLogger(ApiAspect.class);
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private AsyncTasks asyncTasks;

    @Pointcut("@annotation(com.activiti.common.aop.ApiAnnotation)")
    public void allMethod() {
    }

    //用来计算消耗时间
    private ThreadLocal<Long> time = new ThreadLocal<>();

    private ThreadLocal<InvokeLog> invokeLog = new ThreadLocal<>();

    /**
     * 在所有标注@ParamCheck的地方切入
     *
     * @param joinPoint 切点
     */
    @Before("allMethod()")
    public void beforeExec(JoinPoint joinPoint) {
    }

    @Around("allMethod()")
    public Object aroundExec(ProceedingJoinPoint joinPoint) throws Throwable {
        time.set(System.currentTimeMillis());
        Object result;
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String email = CommonUtil.getEmailFromSession(request);
        String params = request.getQueryString();
        ApiAnnotation apiAnnotation = (ApiAnnotation) method.getAnnotation(ApiAnnotation.class);
        RestApiResponse restApiResponse = new RestApiResponse();
        InvokeLog log = new InvokeLog(commonUtil.getSequenceId(), params, email, request.getRequestURI());
        try {
            if (apiAnnotation.validate().length > 0) {
                //入参
                JSONObject param = (JSONObject) JSON.toJSON(joinPoint.getArgs()[0]);
                for (String i : apiAnnotation.validate()) {
                    if (null == param.get(i) || "".equals(param.get(i)))
                        throw new IllegalArgumentException("Required String parameter '" + i + "' is not present");
                }
            }
            result = joinPoint.proceed();
            restApiResponse = new RestApiResponse(true, result);
            log.setCommon("success", JSON.toJSON(restApiResponse).toString());
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restApiResponse = new RestApiResponse(500, e.getMessage(), false);
            log.setCommon("fail", JSON.toJSON(restApiResponse).toString());
        }
        invokeLog.set(log);
        if (apiAnnotation.insertLog()){
            asyncTasks.asyncTask(invokeLog.get(),"insertLog");
        }
        return restApiResponse;
    }

    /**
     * 在所有标注@ParamCheck的地方切入  后置通知
     *
     * @param joinPoint 切点
     */
    @After("allMethod()")
    public void afterExec(JoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        String ClassName = method.getDeclaringClass().getName();
        Long callTime = System.currentTimeMillis() - time.get();
        invokeLog.get().setInvokeTime(callTime);
        logger.info("类" + ClassName + "的方法" + method.getName() + "运行消耗" + callTime + "ms");
    }
}
