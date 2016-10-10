package main.java.com.self.shiro.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session 取值相关工具类
 *
 * @create 2016/5/16 11:08
 */
public class SessionUtil {
    /**
     * 获取session 中的keeperId
     *
     * @param request 请求对象
     * @return keeperId
     * @throws NullPointerException     if {@code request == null},{@code session == null},{@code keeperId == null}
     * @throws IllegalArgumentException if keeperIdSession is empty
     * @throws NumberFormatException    if parse string to integer failed
     */
    public static Integer getKeeperId(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("request is null");
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new NullPointerException("session does not exist");
        }
        Integer keeperId = (Integer) session.getAttribute(SessionConstant.USER_UID);
        if (keeperId == null) {
            throw new NullPointerException("keeper id does not exist");
        }
        return keeperId;
    }

    private SessionUtil() {
        throw new IllegalAccessError();
    }
}
