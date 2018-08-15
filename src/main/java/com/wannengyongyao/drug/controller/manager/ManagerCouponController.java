package com.wannengyongyao.drug.controller.manager;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.service.manager.ManagerCouponService;
import com.wannengyongyao.drug.service.manager.ManagerSettlementService;
import com.wannengyongyao.drug.vo.ManagerCouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/manager")
public class ManagerCouponController {
    @Autowired
    private ManagerCouponService couponService;

    @Autowired
    private ManagerSettlementService settlementService;

    @RequestMapping(value="/coupons", method= {RequestMethod.POST})
    public ResultObject coupons(@Valid @RequestBody ManagerCouponVo couponVo){
        int ret = couponService.batchInsert(couponVo.getNum(), couponVo.getAmount(), couponVo.getStartDate(), couponVo.getEndDate());
        return ResultObject.cond(ret > 0, ResultCode.FAILED);
    }

    @RequestMapping(value="/test/settle", method= {RequestMethod.GET})
    public ResultObject settle(){
        settlementService.settle(10448315810011L);
        return ResultObject.ok();
    }
}
