package com.wannengyongyao.drug.controller.user;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugOrderShare;
import com.wannengyongyao.drug.service.user.DrugOrderService;
import com.wannengyongyao.drug.service.user.DrugUserService;
import com.wannengyongyao.drug.util.DrugConstants;
import com.wannengyongyao.drug.util.RequestUtil;
import com.wannengyongyao.drug.vo.DrugOrderVo;
import com.wannengyongyao.drug.vo.PhotoOrderVo;
import com.wannengyongyao.drug.vo.RewardVo;
import com.wannengyongyao.drug.vo.ShareVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class DrugOrderController {
    private final Logger logger = LoggerFactory.getLogger(DrugOrderController.class);

    @Autowired
    private DrugOrderService orderService;

    @Autowired
    private DrugUserService userService;

    /**
     * 下单
     * @param request
     * @param orderVo
     * @return
     */
    @RequestMapping(value="/order", method= {RequestMethod.POST})
    public ResultObject order(HttpServletRequest request, @Valid @RequestBody DrugOrderVo orderVo){
        Long userId = RequestUtil.getUserId(request);

        if (orderVo.getDrugs().isEmpty()){
            logger.error("invalid order goods");
            return ResultObject.fail(ResultCode.FAILED);
        }

        orderVo.setUserId(userId);
        int rows = orderService.newOrder(orderVo);
        if (rows == -2){
            return ResultObject.fail(ResultCode.COUPON_NOT_EXIST);
        }
        if (rows == -3){
            return ResultObject.fail(ResultCode.COUPON_USED);
        }
        if (rows == -4){
            return ResultObject.fail(ResultCode.COUPON_EXPIRED);
        }
        if (rows == -5){
            return ResultObject.fail(ResultCode.COUPON_NOT_STARTED);
        }

        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 拍照下单
     *
     * @param request
     * @param orderVo
     * @return
     */
    @RequestMapping(value="/photo/order", method= {RequestMethod.POST})
    public ResultObject photoOrder(HttpServletRequest request, @Valid @RequestBody PhotoOrderVo orderVo){
        Long userId = RequestUtil.getUserId(request);

        if (orderVo.getDrugs().isEmpty()){
            logger.error("invalid order photo goods");
            return ResultObject.fail(ResultCode.FAILED);
        }

        orderVo.setUserId(userId);
        int rows = orderService.newPhotoOrder(orderVo);
        if (rows == -2){
            return ResultObject.fail(ResultCode.COUPON_NOT_EXIST);
        }
        if (rows == -3){
            return ResultObject.fail(ResultCode.COUPON_USED);
        }
        if (rows == -4){
            return ResultObject.fail(ResultCode.COUPON_EXPIRED);
        }
        if (rows == -5){
            return ResultObject.fail(ResultCode.COUPON_NOT_STARTED);
        }

        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 取消订单
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value="/cancel-order", method= {RequestMethod.POST})
    public ResultObject cancelOrder(HttpServletRequest request,
                                    @RequestParam("orderId")Long orderId){
        Long userId = RequestUtil.getUserId(request);
        int rows = orderService.cancelOrder(userId, orderId);
        if (rows == -1){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }
        else if (rows == -2){
            return ResultObject.fail(ResultCode.ORDER_CANCELED);
        }
        else if (rows == -3){
            return ResultObject.fail(ResultCode.ORDER_CANNOT_CANCEL);
        }
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 订单确认
     *
     * @param request
     * @param orderId
     * @param sellerId
     * @return
     */
    @RequestMapping(value="/confirm-order", method= {RequestMethod.POST})
    public ResultObject confirmOrder(HttpServletRequest request,
                                     @RequestParam("orderId")Long orderId,
                                     @RequestParam("sellerId")Long sellerId){
        Long userId = RequestUtil.getUserId(request);

        ResultCode r = orderService.confirmOrder(userId, orderId, sellerId);
        return ResultObject.cond(r == ResultCode.OK, r);
    }

    /**
     * 待抢订单（暂不卖家报价）
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/order/waiting", method= {RequestMethod.GET})
    public ResultObject waitingOrder(HttpServletRequest request,
                                    @RequestParam("page")Integer page){
        Long userId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("userId", userId);
        conditionMap.put("status", OrderStatus.INIT.get());
        conditionMap.put("grab", 0);
        List<DrugOrder> orders = orderService.list(conditionMap);

        return ResultObject.ok(orders);
    }

    /**
     * 待确认订单（有卖家报价）
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/order/unconfirmed", method= {RequestMethod.GET})
    public ResultObject unconfirmedOrder(HttpServletRequest request,
                                     @RequestParam("page")Integer page){
        Long userId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("userId", userId);
        conditionMap.put("status", OrderStatus.INIT.get());
        conditionMap.put("unconfirmed", 0);
        List<DrugOrder> orders = orderService.list(conditionMap);

        return ResultObject.ok(orders);
    }

    /**
     * 已完成订单
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/order/completed", method= {RequestMethod.GET})
    public ResultObject completedOrder(HttpServletRequest request,
                                    @RequestParam("page")Integer page){
        Long userId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);

        conditionMap.put("userId", userId);
        conditionMap.put("status", OrderStatus.COMPLETED.get());
        List<DrugOrder> orders = orderService.list(conditionMap);

        return ResultObject.ok(orders);
    }

    /**
     * 已支付确认订单
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/order/confirmed", method= {RequestMethod.GET})
    public ResultObject confirmedOrder(HttpServletRequest request,
                                       @RequestParam("page")Integer page){
        Long userId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);

        conditionMap.put("userId", userId);
        conditionMap.put("status", OrderStatus.CONFIRM.get());
        List<DrugOrder> orders = orderService.list(conditionMap);

        return ResultObject.ok(orders);
    }

    /**
     * 已发货订单
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/order/shipped", method= {RequestMethod.GET})
    public ResultObject shippedOrder(HttpServletRequest request,
                                       @RequestParam("page")Integer page){
        Long userId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);

        conditionMap.put("userId", userId);
        conditionMap.put("status", OrderStatus.SHIPPED.get());
        List<DrugOrder> orders = orderService.list(conditionMap);

        return ResultObject.ok(orders);
    }

    /**
     * 订单详情
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value="/order/detail", method= {RequestMethod.GET})
    public ResultObject orderDetail(HttpServletRequest request,
                                    @RequestParam("orderId")Long orderId){
        Long userId = RequestUtil.getUserId(request);
        DrugOrder order = orderService.orderDetail(orderId);
        if (order == null){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        if (order.getUserId().longValue() != userId.longValue()){
            return ResultObject.fail(ResultCode.FAILED);
        }

        return ResultObject.ok(order);
    }

    /**
     * 订单报价列表
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value="/seller/orders", method= {RequestMethod.GET})
    public ResultObject sellerOrder(HttpServletRequest request,
                                    @RequestParam("orderId")Long orderId){
        Long userId = RequestUtil.getUserId(request);
        DrugOrder order = orderService.getOrderStatus(orderId);
        if (order == null){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }
        if (order.getUserId().longValue() != userId.longValue()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("orderId", orderId);
        conditionMap.put("start", 0);
        conditionMap.put("pageSize", 10);
        return ResultObject.ok(orderService.listByOrder(conditionMap));
    }

    /**
     * 增加悬赏金
     *
     * @param request
     * @param vo
     * @return
     */
    @RequestMapping(value="/order/reward", method= {RequestMethod.POST})
    public ResultObject reward(HttpServletRequest request,
                               @RequestBody @Valid RewardVo vo){
        Long userId = RequestUtil.getUserId(request);
        DrugOrder order = orderService.getOrderStatus(vo.getOrderId());
        if (order == null){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }
        if (order.getUserId().longValue() != userId.longValue()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }
        if (order.getOrderStatus().intValue() != OrderStatus.INIT.get()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = orderService.addRewardAmount(vo.getOrderId(), vo.getAmount());
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 订单转发
     *
     * @param request
     * @param vo
     * @return
     */
    @RequestMapping(value="/order/share", method= {RequestMethod.POST})
    public ResultObject share(HttpServletRequest request,
                               @RequestBody @Valid ShareVo vo) {
        DrugOrder order = orderService.getOrderStatus(vo.getOrderId());
        if (order == null) {
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        DrugOrderShare orderShare = new DrugOrderShare();
        orderShare.setOrderId(vo.getOrderId());
        orderShare.setOpenid(vo.getOpenid());
        orderShare.setCreateTime(LocalDateTime.now());
        orderShare.setShareType(0);
        int rows = orderService.insertOrderShare(orderShare);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 订单加速
     *
     * @param request
     * @param vo
     * @return
     */
    @RequestMapping(value="/order/speedup", method= {RequestMethod.POST})
    public ResultObject speedup(HttpServletRequest request,
                              @RequestBody @Valid ShareVo vo) {
        DrugOrder order = orderService.getOrderStatus(vo.getOrderId());
        if (order == null) {
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        DrugOrderShare orderShare = new DrugOrderShare();
        orderShare.setOrderId(vo.getOrderId());
        orderShare.setOpenid(vo.getOpenid());
        orderShare.setCreateTime(LocalDateTime.now());
        orderShare.setShareType(1);
        int rows = orderService.insertOrderShare(orderShare);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 分享加速统计
     * 
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value="/order/share/stat", method= {RequestMethod.POST})
    public ResultObject shareStat(HttpServletRequest request,
                                  @RequestParam("orderId")Long orderId){
        return ResultObject.ok(orderService.getOrderShare(orderId));
    }
}
