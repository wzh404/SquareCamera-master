package com.wannengyongyao.drug.controller.user;

import com.github.pagehelper.Page;
import com.google.common.cache.Cache;
import com.wannengyongyao.drug.common.JwtObject;
import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.common.status.CouponStatus;
import com.wannengyongyao.drug.common.status.UserAddressStatus;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.user.DrugOrderService;
import com.wannengyongyao.drug.service.user.DrugSellerService;
import com.wannengyongyao.drug.service.user.DrugUserService;
import com.wannengyongyao.drug.util.DrugConstants;
import com.wannengyongyao.drug.util.RequestUtil;
import com.wannengyongyao.drug.util.TokenUtil;
import com.wannengyongyao.drug.vo.CartVo;
import com.wannengyongyao.drug.vo.LongTermVo;
import com.wannengyongyao.drug.vo.UserAddressVo;
import com.wannengyongyao.drug.vo.UserRegisterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
public class DrugUserController {
    private final Logger logger = LoggerFactory.getLogger(DrugUserController.class);

    @Autowired
    private DrugOrderService orderService;

    @Autowired
    private DrugUserService userService;

    @Autowired
    private DrugSellerService sellerService;

    /**
     * 结交药师
     *
     * @param pharmacistId
     * @return
     */
    @RequestMapping(value="/new/pharmacist", method= {RequestMethod.POST})
    public ResultObject newPharmacist(HttpServletRequest request,
                                      @RequestParam("pharmacistId") Integer pharmacistId){
        Long userId = RequestUtil.getUserId(request);

        DrugUserPharmacist userPharmacist = new DrugUserPharmacist();
        userPharmacist.setPharmacistId(pharmacistId);
        userPharmacist.setUserId(userId);
        userPharmacist.setCreateTime(LocalDateTime.now());
        int rows = userService.insertUserPharmacist(userPharmacist);
        if (rows == -1){
            return ResultObject.fail(ResultCode.USER_PHARMACIST_EXIST);
        }

        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 我的药师
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/my/pharmacist", method= {RequestMethod.GET})
    public ResultObject myPharmacist(HttpServletRequest request){
        Long userId = RequestUtil.getUserId(request);

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("userId", userId);
        //conditionMap.put("lon", lon);
        //conditionMap.put("lat", lat);
        Page<DrugSeller> sellerPage = sellerService.myPharmacists(1, 10, conditionMap);
        return ResultObject.ok(sellerPage.getResult());
    }

    /**
     * 我的代收药店
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/my/stores", method= {RequestMethod.GET})
    public ResultObject myStore(HttpServletRequest request){
        Long userId = RequestUtil.getUserId(request);

        List<DrugStore> stores = userService.getUserStores(userId);
        return ResultObject.ok(stores);
    }

    /**
     * 我的购物车
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/my/carts", method= {RequestMethod.GET})
    public ResultObject myCarts(HttpServletRequest request){
        Long userId = RequestUtil.getUserId(request);
        List<DrugUserCart> carts = userService.listUserCart(userId);

        return ResultObject.ok(carts);
    }

    /**
     * 购物车新增药品
     *
     * @param request
     * @param cartVo
     * @return
     */
    @RequestMapping(value="/new/cart", method= {RequestMethod.POST})
    public ResultObject newCarts(HttpServletRequest request,
                                 @RequestBody CartVo cartVo){
        Long userId = RequestUtil.getUserId(request);
        DrugUserCart cart = new DrugUserCart();
        cart.setUserId( userId);
        cart.setDrugId(cartVo.getDrugId());
        cart.setCreateTime(LocalDateTime.now());
        int rows = userService.insert(cart);
        if (rows == -1){
            return ResultObject.fail(ResultCode.FAILED);
        }

        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 删除购物车药品信息
     *
     * @param request
     * @param cartVo
     * @return
     */
    @RequestMapping(value="/delete/cart", method= {RequestMethod.POST})
    public ResultObject deleteCarts(HttpServletRequest request,
                                 @RequestBody CartVo cartVo){
        Long userId = RequestUtil.getUserId(request);

        List<DrugUserCart> carts = new ArrayList<>();
        for (Integer drugId : cartVo.getIds()){
            DrugUserCart cart = new DrugUserCart();
            cart.setUserId(userId);
            cart.setDrugId(drugId);
            carts.add(cart);
        }

        int rows = userService.deleteUserCart(carts);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 我的长期用药
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/my/longterm", method= {RequestMethod.GET})
    public ResultObject myLongterm(HttpServletRequest request){
        Long userId = RequestUtil.getUserId(request);
        List<DrugUserLongterm> longterms = userService.listUserLongTerm(userId);

        return ResultObject.ok(longterms);
    }

    /**
     * 新增长期药品
     *
     * @param request
     * @param longTermVo
     * @return
     */
    @RequestMapping(value="/new/longterm", method= {RequestMethod.POST})
    public ResultObject newLongTermVo(HttpServletRequest request,
                                 @RequestBody LongTermVo longTermVo){
        Long userId = RequestUtil.getUserId(request);
        DrugUserLongterm longTerm = new DrugUserLongterm();
        longTerm.setUserId( userId);
        longTerm.setDrugId(longTermVo.getDrugId());
        longTerm.setCreateTime(LocalDateTime.now());
        int rows = userService.insertUserLongTerm(longTerm);
        if (rows == -1){
            return ResultObject.fail(ResultCode.FAILED);
        }

        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 删除购物车药品信息
     *
     * @param request
     * @param longTermVo
     * @return
     */
    @RequestMapping(value="/delete/longterm", method= {RequestMethod.POST})
    public ResultObject deleteLongTerm(HttpServletRequest request,
                                    @RequestBody LongTermVo longTermVo){
        Long userId = RequestUtil.getUserId(request);

        List<DrugUserLongterm> longTerms = new ArrayList<>();
        for (Integer drugId : longTermVo.getIds()){
            DrugUserLongterm longTerm = new DrugUserLongterm();
            longTerm.setUserId(userId);
            longTerm.setDrugId(drugId);
            longTerms.add(longTerm);
        }

        int rows = userService.deleteUserLongTerm(longTerms);
        return ResultObject.cond(rows > 0, ResultCode.FAILED);
    }

    /**
     * 我的优惠券
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/my/coupons", method= {RequestMethod.GET})
    public ResultObject myCoupons(HttpServletRequest request,
                                  @RequestParam("page") int page){
        Long userId = RequestUtil.getUserId(request);
        List<DrugUserCoupon> coupons = userService.myCoupons(page, DrugConstants.PAGE_SIZE, userId);

        return ResultObject.ok(coupons);
    }

    /**
     * 用户添加优惠券
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/new/coupon", method= {RequestMethod.POST})
    public ResultObject newUserCoupon(HttpServletRequest request,
                                      @RequestParam("code")String code){
        Long userId = RequestUtil.getUserId(request);

        DrugUserCoupon userCoupon = new DrugUserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCode(code);
        userCoupon.setCreateTime(LocalDateTime.now());
        userCoupon.setStatus(CouponStatus.NORMAL.get());

        int ret = userService.insertUserCoupon(userCoupon);
        if (ret == -1){
            return ResultObject.fail(ResultCode.COUPON_NOT_EXIST);
        }
        if (ret == -2){
            return ResultObject.fail(ResultCode.COUPON_USER_ADDED);
        }

        return ResultObject.cond(ret > 0, ResultCode.FAILED);
    }

    @RequestMapping(value="/new/address", method= {RequestMethod.POST})
    public ResultObject newUserAddress(HttpServletRequest request,
                                @RequestBody UserAddressVo addressVo) {
        Long userId = RequestUtil.getUserId(request);
        addressVo.setUserId(userId);

        int ret = userService.insertUserAddress(addressVo.asAddress());
        return ResultObject.cond(ret > 0, ResultCode.FAILED);
    }

    @RequestMapping(value="/my/address", method= {RequestMethod.GET})
    public ResultObject myAddress(HttpServletRequest request) {
        Long userId = RequestUtil.getUserId(request);

        List<DrugUserAddress> address = userService.myAddress(userId);
        return ResultObject.ok(address);
    }

    @RequestMapping(value="/remove/address", method= {RequestMethod.POST})
    public ResultObject removeAddress(HttpServletRequest request,
                                      @RequestParam("id")Long id) {
        Long userId = RequestUtil.getUserId(request);
        int ret = userService.changeUserAddressStatus(id, userId, UserAddressStatus.DELETED.get());
        return ResultObject.cond(ret > 0, ResultCode.FAILED);
    }

    @RequestMapping(value="/default/address", method= {RequestMethod.POST})
    public ResultObject defaultAddress(HttpServletRequest request,
                                      @RequestParam("id")Long id) {
        Long userId = RequestUtil.getUserId(request);
        int ret = userService.changeUserAddressStatus(id, userId, UserAddressStatus.DEFAULT.get());
        return ResultObject.cond(ret > 0, ResultCode.FAILED);
    }
}
