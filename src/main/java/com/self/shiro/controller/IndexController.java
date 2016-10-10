package main.java.com.self.shiro.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Descriptions : index.html
 * Create : 2016/6/20 10:53
 * Update : 2016/6/20 10:53
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CpUserService cpUserService;

    @Autowired
    private OrderQueryService orderQueryCpService;

    @Autowired
    private CpWithdrawalApplyService cpWithdrawalApplyService;

    @Autowired
    private CpUserProfileService cpUserProfileService;

    @RequestMapping(value = "")
    @ResponseBody
    public ModelAndView index( @SessionAttribute(value = SessionConstant.USER_UID) Integer cpUserId) {
        CpUserProfileBo cpUserProfileBo = cpUserProfileService.getByCpUserId(cpUserId);
        if(cpUserProfileBo == null || cpUserProfileBo.getAuthManageStatus() != ProcessSkipConstant.TO_INDEX){
            return new ModelAndView("redirect:/user/profile/authProcedure/show");
        }
        ModelAndView modelAndView = new ModelAndView("page/index/index");
        ModelAndView error = new ModelAndView(PageConstant.REDIRECT_PAGE).addObject("redirectUrl", PageConstant.REDIRECT_PAGE_URL);
        CpUserBo userBo = cpUserService.getById(cpUserId);
        if (userBo == null) {
            log.debug("首页查询创意人信息失败,创意人信息为null");
            return error;
        }
        // 返回用户信息
        UserLoginVo userVo = BeanMapper.map(userBo, UserLoginVo.class);
        modelAndView.addObject("user", userVo);

        //返回订单信息
        PcGetOrderBean queryCondition = new PcGetOrderBean();
        List<CpOrderListPcBo> cpOrderListPcBoList = new ArrayList<>();
        try {
            cpOrderListPcBoList = orderQueryCpService.cpGetPcUnFinishOrderList(cpUserId, queryCondition, 3, 0);
        } catch (Exception e) {
            log.error("there is something wrong with orders!");
        }

        //查询未完成订单总条数
        int unFinishedTotal = orderQueryCpService.cpGetPcUnFinishOrderListCount(cpUserId, queryCondition);
        int finishedTotal = orderQueryCpService.cpGetPcFinishOrderListCount(cpUserId, queryCondition);

        //封装返回给Datatables的VO List
        List<OrderListVo> unfinishedOrdersVoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(cpOrderListPcBoList != null) {
            for (CpOrderListPcBo bo : cpOrderListPcBoList) {
                OrderListVo resultVo = new OrderListVo();
                resultVo.setOrderId(bo.getId());
                resultVo.setCaseName(bo.getName());
                resultVo.setOrderNumber(bo.getNumber());
                resultVo.setStatusCode(bo.getDisplayStatus() == null ? 5 : bo.getDisplayStatus());
                resultVo.setOrderStatus(bo.getStatusName());
                resultVo.setProcess(bo.getProcess());
                resultVo.setAmount(bo.getAmount());
                resultVo.setUri(bo.getUri());

                String createDate = sdf.format(bo.getCreateTime());
                resultVo.setCreateDate(createDate);

                String finishDate = sdf.format(bo.getExpectedDate());
                resultVo.setFinishDate(finishDate);

                unfinishedOrdersVoList.add(resultVo);
            }
        }
        modelAndView.addObject("unfinishedOrders", unfinishedOrdersVoList);
        modelAndView.addObject("unfinishedTotal", unFinishedTotal);
        modelAndView.addObject("finishedTotal", finishedTotal);

        //返回可提现金额
        Long withdrawAmount = cpWithdrawalApplyService.findTotalWithdrawableAmount(cpUserId);
        modelAndView.addObject("amount", withdrawAmount == null ? 0 : withdrawAmount/100);

        return modelAndView;
    }

    /**
     * 加载左侧菜单
     * @param supplierId 创意人Id
     * @return nav or 异常页
     */
    @RequestMapping(value = "/nav/get")
    @ResponseBody
    public ModelAndView getMenu(@SessionAttribute(value = SessionConstant.USER_UID) Integer supplierId) {
        ModelAndView error = new ModelAndView(PageConstant.REDIRECT_PAGE).addObject("redirectUrl", PageConstant.REDIRECT_PAGE_URL);
        if (supplierId == null) {
            log.debug("session不存在");
            return error;
        }
        return new ModelAndView("page/index/nav");
    }
}
