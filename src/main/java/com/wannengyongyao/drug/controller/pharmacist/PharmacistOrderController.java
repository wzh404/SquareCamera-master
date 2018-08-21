package com.wannengyongyao.drug.controller.pharmacist;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.common.status.ShippingStatus;
import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugSeller;
import com.wannengyongyao.drug.service.pharmacist.PharmacistService;
import com.wannengyongyao.drug.service.user.DrugOrderService;
import com.wannengyongyao.drug.util.DrugConstants;
import com.wannengyongyao.drug.util.RequestUtil;
import com.wannengyongyao.drug.util.StringUtil;
import com.wannengyongyao.drug.vo.CollectionVo;
import com.wannengyongyao.drug.vo.PharmacistOrderVo;
import com.wannengyongyao.drug.vo.ShippingVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pharmacist")
public class PharmacistOrderController {
    private final Logger logger = LoggerFactory.getLogger(PharmacistOrderController.class);

    @Autowired
    private PharmacistService pharmacistService;

    @Autowired
    private DrugOrderService orderService;

    /**
     * 发送报价
     *
     * @param request
     * @param orderVo
     * @return
     */
    @RequestMapping(value="/order", method= {RequestMethod.POST})
    public ResultObject grab(HttpServletRequest request, @RequestBody PharmacistOrderVo orderVo){
        long sellerId = RequestUtil.getUserId(request);
        int ret = pharmacistService.grab(orderVo, sellerId);
        if (ret == -1){
            return ResultObject.fail(ResultCode.PHARMACIST_ORDER_EXIST);
        }
        if (ret == -2){
            return ResultObject.fail(ResultCode.PHARMACIST_NOT_EXIST);
        }
        if (ret == -3){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }
        if (ret == -4){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (ret < 1){
            return ResultObject.fail(ResultCode.FAILED);
        }
        return ResultObject.ok();
    }

    /**
     * 待抢购订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/grab", method= {RequestMethod.GET})
    public ResultObject listSellerGrab(HttpServletRequest request,
                                       @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("sellerId", sellerId);

        return ResultObject.ok(pharmacistService.listSellerGrab(conditionMap));
    }

    /**
     * 待确认订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/unconfirmed", method= {RequestMethod.GET})
    public ResultObject unconfirmed(HttpServletRequest request,
                                       @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("sellerId", sellerId);

        return ResultObject.ok(pharmacistService.listSellerUnconfirmed(conditionMap));
    }

    /**
     * 已发货订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/shipped", method= {RequestMethod.GET})
    public ResultObject shipped(HttpServletRequest request,
                                    @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("sellerId", sellerId);
        conditionMap.put("status", OrderStatus.SHIPPED);

        return ResultObject.ok(pharmacistService.listSeller(conditionMap));
    }

    /**
     * 已确认待发货订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/confirmed", method= {RequestMethod.GET})
    public ResultObject confirmed(HttpServletRequest request,
                                @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("sellerId", sellerId);
        conditionMap.put("status", OrderStatus.CONFIRM);

        return ResultObject.ok(pharmacistService.listSeller(conditionMap));
    }

    /**
     * 已完成订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/complete", method= {RequestMethod.GET})
    public ResultObject complete(HttpServletRequest request,
                                  @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("sellerId", sellerId);
        conditionMap.put("status", OrderStatus.COMPLETED);

        return ResultObject.ok(pharmacistService.listSeller(conditionMap));
    }

    /**
     * 待取货订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/order/collection", method= {RequestMethod.GET})
    public ResultObject collection(HttpServletRequest request,
                                 @RequestParam("page") int page){
        Long sellerId = RequestUtil.getUserId(request);
        if (page < 1) page = 1;
        DrugSeller seller = pharmacistService.getSeller(sellerId);
        if (seller == null){
            return ResultObject.fail(ResultCode.FAILED);
        }

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("start", (page - 1) * DrugConstants.PAGE_SIZE);
        conditionMap.put("pageSize", DrugConstants.PAGE_SIZE);
        conditionMap.put("storeId", seller.getStoreId());

        return ResultObject.ok(pharmacistService.listSeller(conditionMap));
    }

    /**
     * 卖家(药师)发货
     *
     * @return
     */
    @RequestMapping(value="/shipping", method= {RequestMethod.POST})
    public ResultObject shipping(HttpServletRequest request,
                                 @RequestBody ShippingVo shippingVo){
        Long sellerId = RequestUtil.getUserId(request);
        DrugOrder order = pharmacistService.getOrderStatus(shippingVo.getOrderId());
        if (order == null) {
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        if (sellerId.longValue() != order.getSellerId().longValue()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (order.getOrderStatus().intValue() != OrderStatus.CONFIRM.get()){
            return ResultObject.fail(ResultCode.ORDER_NOT_CONFIRM);
        }

        if (order.getPayStatus().intValue() != 1){
            return ResultObject.fail(ResultCode.ORDER_NOT_PAYMENT);
        }
        order.setShippingTime(LocalDateTime.now());
        order.setCollectionCode(StringUtil.getRandomCode(6));
        order.setOrderStatus(OrderStatus.SHIPPED.get());
        order.setShippingStatus(ShippingStatus.SHIPPED.get());

        int rows = pharmacistService.sellerShipping(order);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 用户取货确认
     *
     * @return
     */
    @RequestMapping(value="/collection", method= {RequestMethod.POST})
    public ResultObject collection(HttpServletRequest request,
                                 @RequestBody CollectionVo collectionVo){
        Long sellerId = RequestUtil.getUserId(request);
        DrugSeller seller = pharmacistService.getSeller(sellerId);
        if (seller == null){
            return ResultObject.fail(ResultCode.PHARMACIST_NOT_EXIST);
        }

        DrugOrder order = pharmacistService.getOrderStatus(collectionVo.getOrderId());
        if (order == null) {
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        // 是否代收药店
        if (seller.getStoreId().intValue() != order.getCollectionStoreId().intValue()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 订单状态(已发货)
        if (order.getOrderStatus().intValue() != OrderStatus.SHIPPED.get()){
            return ResultObject.fail(ResultCode.ORDER_NOT_SHIPPED);
        }

        // 是否已支付
        if (order.getPayStatus().intValue() != 1){
            return ResultObject.fail(ResultCode.ORDER_NOT_PAYMENT);
        }

        logger.info(order.getCollectionCode());
        // 发货码是否有效
        if (order.getCollectionCode() == null ||
                !order.getCollectionCode().equalsIgnoreCase(collectionVo.getCode())){
            return ResultObject.fail(ResultCode.ORDER_INVALID_COLLECTION_CODE);
        }

        order.setSignTime(LocalDateTime.now());
        order.setShippingStatus(ShippingStatus.COMPLETED.get());
        order.setOrderStatus(OrderStatus.COMPLETED.get());
        int rows = pharmacistService.sellerCollection(order);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
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
                                   @RequestParam("orderId") Long orderId){
        Long sellerId = RequestUtil.getUserId(request);
        DrugSeller seller = pharmacistService.getSeller(sellerId);
        if (seller == null){
            return ResultObject.fail(ResultCode.PHARMACIST_NOT_EXIST);
        }

        DrugOrder order = orderService.orderDetail(orderId);
        if (order == null){
            return ResultObject.fail(ResultCode.ORDER_NOT_EXIST);
        }

        // 是否卖家或代收药店
        if (!(order.getSellerId().longValue() == sellerId.longValue() ||
                seller.getStoreId().intValue() == order.getCollectionStoreId())){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        return ResultObject.ok(order);
    }
}
