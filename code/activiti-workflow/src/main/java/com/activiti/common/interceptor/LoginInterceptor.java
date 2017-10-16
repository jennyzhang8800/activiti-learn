package com.activiti.common.interceptor;

import com.activiti.common.utils.CommonUtil;
import com.activiti.common.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param httpServletRequest  httpServletRequest
     * @param httpServletResponse httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String email = CommonUtil.getEmailFromSession(httpServletRequest);
        logger.info("{user=" + email + "}>>>START HTTP REQUEST:" + httpServletRequest.getRequestURL());
        if ("".equals(email) || null == email) {
            httpServletResponse.sendRedirect("login?redirectUrl=" + httpServletRequest.getServletPath());
            return false;
        }
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String email = (String) httpServletRequest.getSession().getAttribute(ConstantsUtils.sessionEmail);
        if (null != modelAndView && !"".equals(email) && null != email)
            modelAndView.getModel().put("userEmail", httpServletRequest.getSession().getAttribute(ConstantsUtils.sessionEmail));
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        String email = (String) httpServletRequest.getSession().getAttribute("userEmail");
        logger.info("{user=" + email + "}>>>END HTTP REQUEST:" + httpServletRequest.getRequestURL());
    }
}
