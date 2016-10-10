package main.java.com.self.shiro.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @date 2015-09-14 09:50
 * @Description 工程配置
 */
@Component
public class ProjectConfig {

    /**
     * 过期时间
     */
    @Value("${user.expiration}")
    private Integer userExpiration;
    /**
     * 身份证
     */
    @Value("${user.identity.image}")
    private String identityImage;
    /**
     * 订单相关
     */
    @Value("${order.solution.file}")
    private String orderPath;

    public String getOrderPath() {
        return orderPath;
    }

    public void setOrderPath(String orderPath) {
        this.orderPath = orderPath;
    }

    public Integer getUserExpiration() {
        return userExpiration;
    }

    public void setUserExpiration(Integer userExpiration) {
        this.userExpiration = userExpiration;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }

}