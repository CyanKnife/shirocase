package com.self.shiro.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2016-07-18
 * @Description 异常处理
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ex", ex);

        // 根据不同错误转向不同页面
//        Integer httpStatus = httpServletResponse.getStatus();

        if(ex instanceof RuntimeException) {
            if (httpServletRequest.getRequestURI().startsWith("/user/profile") || httpServletRequest.getRequestURI().startsWith("/user/register")  ||
                    httpServletRequest.getRequestURI().startsWith("/user/resume") || httpServletRequest.getRequestURI().startsWith("/user/works") ||
                    httpServletRequest.getRequestURI().startsWith("/file/upload") || httpServletRequest.getRequestURI().startsWith("/index")) {
                ex.printStackTrace();
                return new ModelAndView("/exception_profile", model);
            } else {
                ex.printStackTrace();
                return new ModelAndView("/exception");
            }
        } else {
            ex.printStackTrace();
            return new ModelAndView("404.jsp", model);
        }
    }
}
