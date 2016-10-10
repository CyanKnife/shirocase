package main.java.com.self.shiro.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @date 2015-09-14 09:50
 * @Description 工程配置
 */
@Component
public class ProjectConfig {

    @Value("${resource.image.domain}")
    private String resourceImageDomain;

    /**
     * 可提现金额
     */
    @Value("${withdrawal.amount}")
    private Integer withdrawalAmount;

    /**
     * 提现次数
     */
    @Value("${withdrawal.count}")
    private Integer withdrawalCount;

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
     * 营业执照
     */
    @Value("${user.businessLicense.image}")
    private String businessLicense;
    /**
     * 组织机构代码证
     */
    @Value("${user.organization.image}")
    private String organization;
    /**
     * 税务证
     */
    @Value("${user.tax.image}")
    private String tax;
    /**
     * 三合一营业执照
     */
    @Value("${user.businessLicenseAll.image}")
    private String businessLicenseAll;
    /**
     * 源文件上传 --- Add By WuZheng
     */
    @Value("${order.solution.file}")
    private String sourceFile;
    /**
     * 订单相关
     * `
     */
    @Value("${order.solution.file}")
    private String orderPath;

    public String getOrderPath() {
        return orderPath;
    }

    public void setOrderPath(String orderPath) {
        this.orderPath = orderPath;
    }

    /**
     * 案例
     */
    @Value("${user.case.path}")
    private String userCasePath;


    public String getUserCasePath() {
        return userCasePath;
    }

    public void setUserCasePath(String userCasePath) {
        this.userCasePath = userCasePath;
    }


    public Integer getUserExpiration() {
        return userExpiration;
    }

    public void setUserExpiration(Integer userExpiration) {
        this.userExpiration = userExpiration;
    }

    public Integer getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public Integer getWithdrawalCount() {
        return withdrawalCount;
    }

    public void setWithdrawalCount(Integer withdrawalCount) {
        this.withdrawalCount = withdrawalCount;
    }

    public void setWithdrawalAmount(Integer withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getResourceImageDomain() {
        return resourceImageDomain;
    }

    public void setResourceImageDomain(String resourceImageDomain) {
        this.resourceImageDomain = resourceImageDomain;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getBusinessLicenseAll() {
        return businessLicenseAll;
    }

    public void setBusinessLicenseAll(String businessLicenseAll) {
        this.businessLicenseAll = businessLicenseAll;
    }
}