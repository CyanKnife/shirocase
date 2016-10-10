package main.java.com.self.shiro.utils;

/**
 * @date 2016-01-14 16:11
 * @Description 错误码工具类
 */
public class ErrorCodeUtils {
    /**
     * 根据错误码获取错误信息的KEY
     *
     * @param errorCode 错误码
     * @return
     */
    public static String getErrorMessageKey(int errorCode) {
        StringBuilder sb = new StringBuilder("api");
        sb.append(".");
        sb.append(errorCode);
        sb.append(".");
        sb.append("message");
        return sb.toString();
    }
}
