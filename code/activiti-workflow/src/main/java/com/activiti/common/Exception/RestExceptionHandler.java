package com.activiti.common.Exception;

import com.activiti.pojo.restApiDto.RestApiResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * 接口统一异常处理
 * Created by liulinhui on 2017/8/5.
 */
@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public RestApiResponse constraintViolationException(ConstraintViolationException ex) {
        return new RestApiResponse(500, ex.getMessage(),false);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public RestApiResponse IllegalArgumentException(IllegalArgumentException ex) {
        logger.error(ExceptionUtils.getStackTrace(ex));
        return new RestApiResponse(500, ex.getMessage(),false);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public RestApiResponse noHandlerFoundException(Exception ex) {
        return new RestApiResponse(404, ex.getMessage(),false);
    }


    @ExceptionHandler(value = {Exception.class})
    public RestApiResponse unknownException(Exception ex) {
        ex.printStackTrace();
        return new RestApiResponse(500, ex.getMessage(),false);
    }

}
