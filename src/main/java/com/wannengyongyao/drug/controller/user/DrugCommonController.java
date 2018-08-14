package com.wannengyongyao.drug.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.wannengyongyao.drug.common.JwtObject;
import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.model.DrugUser;
import com.wannengyongyao.drug.model.DrugWeixinUser;
import com.wannengyongyao.drug.service.user.DrugCommonService;
import com.wannengyongyao.drug.service.user.DrugUserService;
import com.wannengyongyao.drug.util.StringUtil;
import com.wannengyongyao.drug.util.TokenUtil;
import com.wannengyongyao.drug.util.WxUtils;
import com.wannengyongyao.drug.vo.LoginVo;
import com.wannengyongyao.drug.vo.WeChatLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class DrugCommonController {
    @Autowired
    private DrugCommonService commonService;

    @Autowired
    private DrugUserService userService;

    @Autowired
    private Cache<String, String> smsCache;

    @Value("${drug.user.appid}")
    private String appid;

    @Value("${drug.user.secret}")
    private String secret;

    /**
     * 用户端banner
     *
     * @return
     */
    @RequestMapping(value="/common/banners", method= {RequestMethod.GET})
    public ResultObject banner(){
        List<DrugBanner> bannerList = commonService.banner(1);
        return ResultObject.ok(bannerList);
    }

    /**
     * 发送手机短信
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value="/common/sms/code", method= {RequestMethod.GET})
    public ResultObject smsCode(@RequestParam("mobile")String mobile){
        String smsCode = smsCache.getIfPresent(mobile);
        if (smsCode != null){
            return ResultObject.fail(ResultCode.NOT_REPEAT_SEND_CODE);
        }

        String code = StringUtil.getRandomCode(6);
        smsCache.put(mobile, code);

        return ResultObject.ok(code);
    }

    /**
     * 用户登录并注册
     *
     * @param loginVo
     * @return
     */
    @RequestMapping(value="/common/login", method= {RequestMethod.POST})
    public ResultObject login(@Valid @RequestBody LoginVo loginVo){
        String smsCode = smsCache.getIfPresent(loginVo.getMobile());
        if (smsCode == null || !smsCode.equalsIgnoreCase(loginVo.getCode())){
            return ResultObject.fail(ResultCode.INVALID_SMS_CODE);
        }
        smsCache.invalidate(loginVo.getMobile());

        // 获取用户微信授权信息
        DrugWeixinUser wxUser = userService.getByOpenId(loginVo.getOpenid());
        if (wxUser == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 注册用户
        DrugUser user = loginVo.asUser();
        user.setAvatar(wxUser.getAvatarUrl());
        user.setGender("1".equals(wxUser.getGender()) ? 1 : 0);
        user.setCreateIp("localhost");
        int rows = userService.insertUser(user);
        if (rows < 1){
            return ResultObject.fail(ResultCode.FAILED);
        }

        // 生成access_token
        JwtObject jwt = new JwtObject(user.getId());
        Optional<String> token = TokenUtil.createJwtToken(jwt.toJson());
        if (!token.isPresent()){
            return ResultObject.fail(ResultCode.FAILED);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("access_token", token.get());
        return ResultObject.ok(resultMap);
    }

    /**
     * 获取微信用户信息，并保存。
     *
     * @param loginVo
     * @return
     */
    @RequestMapping(value="/common/wechat/login", method= {RequestMethod.POST})
    public ResultObject wxLogin(@RequestBody WeChatLoginVo loginVo){
        // 根据appid和secret获取session_key
        Optional<JSONObject> json = WxUtils.getSessionKey(appid, secret, loginVo.getJscode());
        if (!json.isPresent()) {
            return ResultObject.fail(ResultCode.FAILED);
        }

        String sessionKey = json.get().getString("session_key");
        String openid = json.get().getString("openid");

        // 根据session_key, encryptedData, iv解密微信用户授权信息
        Optional<String> jsonString = WxUtils.getUserInfo(sessionKey, loginVo.getEncryptedData(), loginVo.getIv());
        if (!jsonString.isPresent()) {
            return ResultObject.fail(ResultCode.FAILED);
        }

        // decode json
        DrugWeixinUser user = JSON.parseObject(jsonString.get(), DrugWeixinUser.class);
        if (userService.insertWeixinUser(user) < 1){
            return ResultObject.fail(ResultCode.FAILED);
        }

        return ResultObject.ok("openid",openid);
    }

    /**
     * 获取省份列表
     *
     * @return
     */
    @RequestMapping(value="/common/province", method= {RequestMethod.GET})
    public ResultObject province(){
        return ResultObject.ok(commonService.getProvince());
    }

    /**
     * 根据省份获取城市列表
     *
     * @param code
     * @return
     */
    @RequestMapping(value="/common/city", method= {RequestMethod.GET})
    public ResultObject city(@RequestParam("code")String code){
        return ResultObject.ok(commonService.getCity(code.substring(0,2)));
    }

    /**
     * 根据城市获取区列表
     *
     * @param code
     * @return
     */
    @RequestMapping(value="/common/district", method= {RequestMethod.GET})
    public ResultObject district(@RequestParam("code")String code){
        return ResultObject.ok(commonService.getDistrict(code.substring(0,4)));
    }
}