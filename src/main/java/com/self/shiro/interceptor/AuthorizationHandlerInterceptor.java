package com.self.shiro.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @date 2015-11-19 19:18
 * @Description 认证拦截器
 */
public class AuthorizationHandlerInterceptor extends HandlerInterceptorAdapter {

    protected final static Logger logger = LoggerFactory.getLogger(AuthorizationHandlerInterceptor.class);

    /**
     * 拦截请求做预处理, 校验请求认证
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
       if (checkLogin(request)) {
            return true;
        }
        response.sendRedirect("/user/login");
        return false;
    }

    private boolean checkLogin(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        //没有登录
        if (session == null) {
            return false;
        }
        if (!isLogin(session)) {
            return false;
        }
        //登录过期
        if (isExpiration(session)) {
            return false;
        }
        //黑名单
        return !isBlack(session);
    }

    /**
     * 黑名单
     *
     * @param session
     * @return
     */
    private boolean isBlack(HttpSession session) {
        return false;
    }

    /**
     * 是否登录
     *
     * @param session
     * @return
     */
    private boolean isLogin(HttpSession session) {
        //return session.getAttribute(SessionConstant.USER_UID) != null && session.getAttribute(SessionConstant.USER_ACC) != null;
        return true;
    }

    /**
     * 过期
     *
     * @param session
     * @return
     */
    private boolean isExpiration(HttpSession session) {

        return false;
//        Date expiration = (Date) session.getAttribute(SessionConstant.USER_EXPIRATION);
//        if (expiration == null || !expiration.before(DateUtils.getCurrentDate())) {
//            session.setAttribute(SessionConstant.USER_EXPIRATION, DateUtil.getAfterMinute(30));
//            return false;
//        } else {
//            session.invalidate();
//            return true;
//        }
    }

}