package com.wannengyongyao.drug.controller.user;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.DrugSeller;
import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.model.DrugUserPharmacist;
import com.wannengyongyao.drug.service.user.DrugOrderService;
import com.wannengyongyao.drug.service.user.DrugSellerService;
import com.wannengyongyao.drug.service.user.DrugUserService;
import com.wannengyongyao.drug.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class DrugUserController {
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
    public ResultObject myPharmacist(HttpServletRequest request,
                                     @RequestParam("lon")Double lon,
                                     @RequestParam("lat")Double lat){
        Long userId = RequestUtil.getUserId(request);

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("userId", userId);
        conditionMap.put("lon", lon);
        conditionMap.put("lat", lat);
        Page<DrugSeller> sellerPage = sellerService.myPharmacists(1, 10, conditionMap);
        return ResultObject.ok(sellerPage.getResult());
    }

    /**
     * 我的药师
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
}
