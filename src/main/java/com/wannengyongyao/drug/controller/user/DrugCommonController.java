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
import com.wannengyongyao.drug.util.PayService;
import com.wannengyongyao.drug.util.StringUtil;
import com.wannengyongyao.drug.util.TokenUtil;
import com.wannengyongyao.drug.util.WxUtils;
import com.wannengyongyao.drug.vo.WeChatLoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class DrugCommonController {
    private final Logger logger = LoggerFactory.getLogger(DrugCommonController.class);

    @Autowired
    private DrugCommonService commonService;

    @Autowired
    private DrugUserService userService;

    @Autowired
    private Cache<String, String> smsCache;

    @Autowired
    private PayService payService;

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
     * 根据openid获取access_token
     *
     * @param openid
     * @return
     */
    @RequestMapping(value="/common/openid", method= {RequestMethod.POST})
    public ResultObject openid(@RequestParam("openid")String openid){
        DrugUser user = userService.getUserByOpenid(openid);
        if (user == null){
            return ResultObject.fail(ResultCode.USER_PLEASE_LOGIN);
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
     * 用户登录并注册
     *
     * @param loginVo
     * @return

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
    }*/

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
        Long userId = userService.insertWeixinAndUser(user);
        if ( userId < 0){
            return ResultObject.fail(ResultCode.FAILED);
        }

        JwtObject jwt = new JwtObject(userId);
        Optional<String> token = TokenUtil.createJwtToken(jwt.toJson());
        if (!token.isPresent()){
            return ResultObject.fail(ResultCode.FAILED);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("access_token", token.get());
        resultMap.put("openid", openid);
        return ResultObject.ok(resultMap);
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

    /**
     * 获取字典
     *
     * @param classify
     * @return
     */
    @RequestMapping(value="/common/dict", method= {RequestMethod.GET})
    public ResultObject dict(@RequestParam("classify")String classify){
        return ResultObject.ok(commonService.listDict(classify));
    }

    @ResponseBody
    @RequestMapping(value="/pay/notify", method= {RequestMethod.POST})
    public String pay(HttpServletRequest request){
        Map<String, String> map = payService.getCallbackParams(request);
        if (map != null && map.get("result_code").equalsIgnoreCase("SUCCESS")) {
            String orderId = map.get("out_trade_no");
            //String openId = map.get("openid");

            //支付成功之后的逻辑
            commonService.payment(Long.valueOf(orderId));
        } else {
            logger.info("pay failed");
        }
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

    /**
     * 从用户地址登录并注册
     *
     * @param registerVo
     * @return

    @RequestMapping(value="/common/register", method= {RequestMethod.POST})
    public ResultObject newUser(@RequestBody UserRegisterVo registerVo) {
        String smsCode = smsCache.getIfPresent(registerVo.getMobile());
        if (smsCode == null || !smsCode.equalsIgnoreCase(registerVo.getCode())){
            return ResultObject.fail(ResultCode.INVALID_SMS_CODE);
        }
        smsCache.invalidate(registerVo.getMobile());

        // 获取用户微信授权信息
        DrugWeixinUser wxUser = userService.getByOpenId(registerVo.getOpenid());
        if (wxUser == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        DrugUser user = userService.getUserByOpenid(registerVo.getOpenid());
        if (user != null){
            return ResultObject.fail(ResultCode.USER_REGISTER_ALREADY);
        }

        // 注册用户
        user = registerVo.asUser();
        user.setAvatar(wxUser.getAvatarUrl());
        user.setName(wxUser.getNickName());
        user.setGender("1".equals(wxUser.getGender()) ? 1 : 0);
        int rows = userService.insertUserAndAddress(user, registerVo.asAddress());
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
    }*/
}
