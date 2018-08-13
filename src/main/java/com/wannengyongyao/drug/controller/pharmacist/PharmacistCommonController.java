package com.wannengyongyao.drug.controller.pharmacist;

import com.google.common.cache.Cache;
import com.wannengyongyao.drug.common.JwtObject;
import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.model.DrugSeller;
import com.wannengyongyao.drug.service.pharmacist.PharmacistService;
import com.wannengyongyao.drug.service.user.DrugCommonService;
import com.wannengyongyao.drug.util.TokenUtil;
import com.wannengyongyao.drug.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pharmacist")
public class PharmacistCommonController {
    @Autowired
    private DrugCommonService commonService;

    @Autowired
    private PharmacistService pharmacistService;

    @Autowired
    private Cache<String, String> smsCache;

    @Value("${drug.pharmacist.appid}")
    private String appid;

    @Value("${drug.pharmacist.secret}")
    private String secret;

    /**
     * 药师端banner列表
     *
     * @return
     */
    @RequestMapping(value="/common/banners", method= {RequestMethod.GET})
    public ResultObject banner(){
        List<DrugBanner> bannerList = commonService.banner(2);
        return ResultObject.ok(bannerList);
    }

    /**
     * 用户登录并注册
     *
     * @param loginVo
     * @return
     */
    @RequestMapping(value="/common/login", method= {RequestMethod.POST})
    public ResultObject login(@RequestBody LoginVo loginVo){
        String smscode = smsCache.getIfPresent(loginVo.getMobile());
        if (smscode == null || !smscode.equalsIgnoreCase(loginVo.getCode())){
            return ResultObject.fail(ResultCode.INVALID_SMS_CODE);
        }
        DrugSeller seller = pharmacistService.getPharmacitsByMobile(loginVo.getMobile());
        if (seller == null){
            return ResultObject.fail(ResultCode.PHARMACIST_NOT_EXIST);
        }
        JwtObject jwt = new JwtObject(seller.getId());
        Optional<String> token = TokenUtil.createJwtToken(jwt.toJson());
        if (!token.isPresent()){
            return ResultObject.fail(ResultCode.FAILED);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("access_token", token.get());
        return ResultObject.ok(resultMap);
    }
}
