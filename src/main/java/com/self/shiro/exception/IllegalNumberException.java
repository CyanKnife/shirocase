package main.java.com.self.shiro.exception;



/**
 * 数字异常
 * 用于在金额转换时表示非法数字
 *
 * @create 2016/2/22 15:04
 */
public class IllegalNumberException extends ServiceException {
    public IllegalNumberException() {
        this.serviceBusinessCode = ServiceBusinessCode.ILLEGAL_NUMBER_FORMAT_ERROR;
    }
}
