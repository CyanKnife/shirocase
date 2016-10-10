package main.java.com.self.shiro.controller.common;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Descriptions : 文件上传
 * Create : 2016/4/18 16:48
 * Update : 2016/4/18 16:48
 */
@Controller
@RequestMapping("/file/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private final String FILE_SPLIT="\\.";
    private final String SEPARATOR_SLASH = "/";

    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private FileUploadTempService fileUploadTempService;


    /**
     *
     * @param identityImage
     * @return
     */
    @RequestMapping(value = "/idCard", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public UserIdentityVo uploadIdCardImage(@RequestParam(value = "identityImage") CommonsMultipartFile identityImage) {

        String fileName = identityImage.getOriginalFilename();
        String[] filsNameSplit = fileName.split(FILE_SPLIT);
        if (identityImage.getSize() > 2*1024*1024) {
            logger.debug("上传文件过大");
            return null;
        }
        if(filsNameSplit.length<2 || !isContains(filsNameSplit[1]))
        {
            logger.debug("上传文件类型不对");
            return null;
        }
        String annotationFilePath = null;
        StringBuilder sb = new StringBuilder();

        // 目录注解路径
        annotationFilePath = projectConfig.getIdentityImage();
        //目录规则
        sb.append(FileAnnotationConstant.USER_IDCARD_PATH);

        FilenameBo filenameBo = FilenameUtils.getUserImgFilename(annotationFilePath, filsNameSplit[1], UserTypeEnum.SUPPLIER, FileTypeRuleEnum.IMG);
        if (filenameBo == null) {
            logger.error("save businessLicense fail");
            return null;
        }
        //临时表中存包含注解在内的所有路径
        String pathStartFromAnnotation = filenameBo.getFullname();
        String pathStartFromUuid = filenameBo.getName();

        String path = pathStartFromAnnotation.substring(0, pathStartFromAnnotation.lastIndexOf(SEPARATOR_SLASH) + 1);
        String tailName = pathStartFromUuid.substring(pathStartFromUuid.lastIndexOf(SEPARATOR_SLASH) + 1);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(path,tailName);
        //保存
        try {
            identityImage.transferTo(targetFile);
        } catch (Exception e) {
            logger.error("File upload exception",e);
            throw new FileUploadException();
        }
        //mongo业务表中存注解后的路径(目录规则<角色>+文件名规则)
        String PathFromRole = new StringBuilder().append(SEPARATOR_SLASH).append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        //String PathBeforeRole = sb.append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        UserIdentityVo userIdentityVo = new UserIdentityVo(PathFromRole,PathFromRole);
        //临时表
        FileUploadTempBo fileUploadTempBo = new FileUploadTempBo(pathStartFromAnnotation,filsNameSplit[1],fileName,false, DateUtils.getCurrentDate());
        Long fileId = fileUploadTempService.create(fileUploadTempBo);
        userIdentityVo.setFileId(fileId);
        return userIdentityVo ;
    }

    /**
     *
     * @param identityImage
     * @return
     */
    @RequestMapping(value = "/businessLicense", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public CpUserBusinessLicenseVo uploadBusinessLicenseImage(@RequestParam(value = "identityImage") CommonsMultipartFile identityImage) {

        String fileName = identityImage.getOriginalFilename();
        String[] filsNameSplit = fileName.split(FILE_SPLIT);
        if (identityImage.getSize() > 2*1024*1024) {
            logger.debug("上传文件过大");
            return null;
        }
        if(filsNameSplit.length<2 || !isContains(filsNameSplit[1]))
        {
            logger.debug("上传文件类型不对");
            return null;
        }
        String annotationFilePath = null;
        StringBuilder sb = new StringBuilder();

        // 目录注解路径
        annotationFilePath = projectConfig.getBusinessLicense();
        //目录规则
        sb.append(FileAnnotationConstant.USER_BUSINESSLICENSE_PATH);

        FilenameBo filenameBo = FilenameUtils.getUserImgFilename(annotationFilePath, filsNameSplit[1], UserTypeEnum.SUPPLIER, FileTypeRuleEnum.IMG);
        if (filenameBo == null) {
            logger.error("save businessLicense fail");
            return null;
        }
        //临时表中存包含注解在内的所有路径
        String pathStartFromAnnotation = filenameBo.getFullname();
        String pathStartFromUuid = filenameBo.getName();

        String path = pathStartFromAnnotation.substring(0, pathStartFromAnnotation.lastIndexOf(SEPARATOR_SLASH) + 1);
        String tailName = pathStartFromUuid.substring(pathStartFromUuid.lastIndexOf(SEPARATOR_SLASH) + 1);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(path,tailName);
        //保存
        try {
            identityImage.transferTo(targetFile);
        } catch (Exception e) {
            logger.error("File upload exception",e);
            throw new FileUploadException();
        }
        //mongo业务表中存注解后的路径(目录规则<角色>+文件名规则)
        String PathFromRole = new StringBuilder().append(SEPARATOR_SLASH).append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        //String PathBeforeRole = sb.append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        CpUserBusinessLicenseVo cpUserBusinessLicenseVo = new CpUserBusinessLicenseVo(PathFromRole,PathFromRole);
        //临时表
        FileUploadTempBo fileUploadTempBo = new FileUploadTempBo(pathStartFromAnnotation,filsNameSplit[1],fileName,false, DateUtils.getCurrentDate());
        Long fileId = fileUploadTempService.create(fileUploadTempBo);
        cpUserBusinessLicenseVo.setFileId(fileId);
        return cpUserBusinessLicenseVo ;
    }

    /**
     *
     * @param identityImage
     * @return
     */
    @RequestMapping(value = "/organization", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public CpUserOrganizationVo uploadOrganizationImage(@RequestParam(value = "identityImage") CommonsMultipartFile identityImage) {

        String fileName = identityImage.getOriginalFilename();
        String[] filsNameSplit = fileName.split(FILE_SPLIT);
        if (identityImage.getSize() > 2*1024*1024) {
            logger.debug("上传文件过大");
            return null;
        }
        if(filsNameSplit.length<2 || !isContains(filsNameSplit[1]))
        {
            logger.debug("上传文件类型不对");
            return null;
        }
        String annotationFilePath = null;
        StringBuilder sb = new StringBuilder();

        // 目录注解路径
        annotationFilePath = projectConfig.getOrganization();
        //目录规则
        sb.append(FileAnnotationConstant.USER_ORGANIZATION_PATH);

        FilenameBo filenameBo = FilenameUtils.getUserImgFilename(annotationFilePath, filsNameSplit[1], UserTypeEnum.SUPPLIER, FileTypeRuleEnum.IMG);
        if (filenameBo == null) {
            logger.error("save businessLicense fail");
            return null;
        }
        //临时表中存包含注解在内的所有路径
        String pathStartFromAnnotation = filenameBo.getFullname();
        String pathStartFromUuid = filenameBo.getName();

        String path = pathStartFromAnnotation.substring(0, pathStartFromAnnotation.lastIndexOf(SEPARATOR_SLASH) + 1);
        String tailName = pathStartFromUuid.substring(pathStartFromUuid.lastIndexOf(SEPARATOR_SLASH) + 1);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(path,tailName);
        //保存
        try {
            identityImage.transferTo(targetFile);
        } catch (Exception e) {
            logger.error("File upload exception",e);
            throw new FileUploadException();
        }
        //mongo业务表中存注解后的路径(目录规则<角色>+文件名规则)
        String PathFromRole = new StringBuilder().append(SEPARATOR_SLASH).append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        //String PathBeforeRole = sb.append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        CpUserOrganizationVo cpUserOrganizationVo = new CpUserOrganizationVo(PathFromRole,PathFromRole);
        //临时表
        FileUploadTempBo fileUploadTempBo = new FileUploadTempBo(pathStartFromAnnotation,filsNameSplit[1],fileName,false, DateUtils.getCurrentDate());
        Long fileId = fileUploadTempService.create(fileUploadTempBo);
        cpUserOrganizationVo.setFileId(fileId);
        return cpUserOrganizationVo ;
    }

    /**
     *
     * @param identityImage
     * @return
     */
    @RequestMapping(value = "/tax", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public CpUserTaxVo uploadTaxImage(@RequestParam(value = "identityImage") CommonsMultipartFile identityImage) {

        String fileName = identityImage.getOriginalFilename();
        String[] filsNameSplit = fileName.split(FILE_SPLIT);
        if (identityImage.getSize() > 2*1024*1024) {
            logger.debug("上传文件过大");
            return null;
        }
        if(filsNameSplit.length<2 || !isContains(filsNameSplit[1]))
        {
            logger.debug("上传文件类型不对");
            return null;
        }
        String annotationFilePath = null;
        StringBuilder sb = new StringBuilder();

        // 目录注解路径
        annotationFilePath = projectConfig.getTax();
        //目录规则
        sb.append(FileAnnotationConstant.USER_TAX_PATH);

        FilenameBo filenameBo = FilenameUtils.getUserImgFilename(annotationFilePath, filsNameSplit[1], UserTypeEnum.SUPPLIER, FileTypeRuleEnum.IMG);
        if (filenameBo == null) {
            logger.error("save businessLicense fail");
            return null;
        }
        //临时表中存包含注解在内的所有路径
        String pathStartFromAnnotation = filenameBo.getFullname();
        String pathStartFromUuid = filenameBo.getName();

        String path = pathStartFromAnnotation.substring(0, pathStartFromAnnotation.lastIndexOf(SEPARATOR_SLASH) + 1);
        String tailName = pathStartFromUuid.substring(pathStartFromUuid.lastIndexOf(SEPARATOR_SLASH) + 1);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(path,tailName);
        //保存
        try {
            identityImage.transferTo(targetFile);
        } catch (Exception e) {
            logger.error("File upload exception",e);
            throw new FileUploadException();
        }
        //mongo业务表中存注解后的路径(目录规则<角色>+文件名规则)
        String PathFromRole = new StringBuilder().append(SEPARATOR_SLASH).append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        //String PathBeforeRole = sb.append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        CpUserTaxVo cpUserTaxVo = new CpUserTaxVo(PathFromRole,PathFromRole);
        //临时表
        FileUploadTempBo fileUploadTempBo = new FileUploadTempBo(pathStartFromAnnotation,filsNameSplit[1],fileName,false, DateUtils.getCurrentDate());
        Long fileId = fileUploadTempService.create(fileUploadTempBo);
        cpUserTaxVo.setFileId(fileId);
        return cpUserTaxVo ;
    }

    /**
     *
     * @param identityImage
     * @return
     */
    @RequestMapping(value = "/businessLicenseAll", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public CpUserBusinessLicenseAllVo uploadBusinessLicenseAllImage(@RequestParam(value = "identityImage") CommonsMultipartFile identityImage) {

        String fileName = identityImage.getOriginalFilename();
        String[] filsNameSplit = fileName.split(FILE_SPLIT);
        if (identityImage.getSize() > 2*1024*1024) {
            logger.debug("上传文件过大");
            return null;
        }
        if(filsNameSplit.length<2 || !isContains(filsNameSplit[1]))
        {
            logger.debug("上传文件类型不对");
            return null;
        }
        String annotationFilePath = null;
        StringBuilder sb = new StringBuilder();

        // 目录注解路径
        annotationFilePath = projectConfig.getBusinessLicenseAll();
        //目录规则
        sb.append(FileAnnotationConstant.USER_BUSINESSLICENSE_ALL_PATH);

        FilenameBo filenameBo = FilenameUtils.getUserImgFilename(annotationFilePath, filsNameSplit[1], UserTypeEnum.SUPPLIER, FileTypeRuleEnum.IMG);
        if (filenameBo == null) {
            logger.error("save businessLicense fail");
            return null;
        }
        //临时表中存包含注解在内的所有路径
        String pathStartFromAnnotation = filenameBo.getFullname();
        String pathStartFromUuid = filenameBo.getName();

        String path = pathStartFromAnnotation.substring(0, pathStartFromAnnotation.lastIndexOf(SEPARATOR_SLASH) + 1);
        String tailName = pathStartFromUuid.substring(pathStartFromUuid.lastIndexOf(SEPARATOR_SLASH) + 1);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(path,tailName);
        //保存
        try {
            identityImage.transferTo(targetFile);
        } catch (Exception e) {
            logger.error("File upload exception",e);
            throw new FileUploadException();
        }
        //mongo业务表中存注解后的路径(目录规则<角色>+文件名规则)
        String PathFromRole = new StringBuilder().append(SEPARATOR_SLASH).append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        //String PathBeforeRole = sb.append(UserTypeEnum.SUPPLIER.getRole()).append(pathStartFromUuid).toString();
        CpUserBusinessLicenseAllVo cpUserBusinessLicenseAllVo = new CpUserBusinessLicenseAllVo(PathFromRole,PathFromRole);
        //临时表
        FileUploadTempBo fileUploadTempBo = new FileUploadTempBo(pathStartFromAnnotation,filsNameSplit[1],fileName,false, DateUtils.getCurrentDate());
        Long fileId = fileUploadTempService.create(fileUploadTempBo);
        cpUserBusinessLicenseAllVo.setFileId(fileId);
        return cpUserBusinessLicenseAllVo ;
    }

    private boolean isContains(String postfix)
    {
        if(StringUtils.isEmpty(postfix))
        {
            return false;
        }
        if(postfixs == null)
        {
            postfixs = new ArrayList<>();
            postfixs.add("jpg");
            postfixs.add("jpeg");
            postfixs.add("png");
            postfixs.add("bmp");
            postfixs.add("gif");
        }
        String upperPostfix = postfix.toUpperCase();
        for(String fix : postfixs)
        {
            if(upperPostfix.equals(fix.toUpperCase()))
            {
                return true;
            }
        }
        return false;
    }

    private List<String> postfixs;

}
