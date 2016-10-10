package main.java.com.self.shiro.controller.common.vo;

/**
 * Create : 2016/06/18 10:50
 * Descriptions : 返回结果
 */
public class ResultVo {
    /**
     * 成功(0:成功； 其余均为失败)
     */
    private Integer type = 0;
    /**
     * 失败原因
     */
    private String message;

    public ResultVo(String message) {
        this.message = message;
    }

    public ResultVo(Integer type) {
        this.type = type;
    }

    public ResultVo(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

