package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.UserStatus;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.CryptoUtil;
import com.blueocean.azbrain.vo.LoginVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/manager/user")
public class ManagerUserController {
    private final Logger logger = LoggerFactory.getLogger(ManagerUserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户列表.
     *
     * @return
     */
    @RequestMapping(value="/search", method= {RequestMethod.GET})
    public ResultObject search(@RequestParam(value="name", required = false)  String name, @RequestParam("page") Integer page,
                             @RequestParam(value="start_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                             @RequestParam(value="end_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime){
        HashMap<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("name", name);
        conditionMap.put("startTime", startTime);
        conditionMap.put("endTime", endTime);
        Page<User> users = userService.findByPage(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("users", users.getResult());
        resultMap.put("page", ResultObject.pageMap(users));

        return ResultObject.ok(resultMap);
    }

    /**
     * 运营端登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/login", method= {RequestMethod.POST})
    public ResultObject login(HttpServletRequest request,
                              @Valid @RequestBody LoginVo loginVo){
        User user = userService.getUserByName(loginVo.getName());
        if (user == null){
            logger.warn("user {} not exsits", loginVo.getName());
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        String signedPwd = CryptoUtil.signature(loginVo.getPwd());
        if (!user.getPassword().equalsIgnoreCase(signedPwd)){
            logger.warn("{} incorrect password", loginVo.getName());
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        ManagerSessionObject.toSession(request.getSession(true), user);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", user.getId());
        resultMap.put("login_name", user.getLoginName());
        resultMap.put("nick_name", user.getName());

        return ResultObject.ok("user", resultMap);
    }

    /**
     * 查看用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/view", method= {RequestMethod.POST, RequestMethod.GET})
    public ResultObject get(@RequestParam("user_id") Integer userId){
        User user = userService.get(userId);
        if (user == null){
            logger.warn("user {} not exist", userId);
            return ResultObject.fail(ResultCode.USER_NOT_EXIST);
        }
        return ResultObject.ok("user", user);
    }

    /**
     * 停用用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/close", method= {RequestMethod.POST, RequestMethod.GET})
    public ResultObject close(@RequestParam("user_id") Integer userId){
        User user = userService.get(userId);
        if (user == null){
            logger.warn("user {} not exist", userId);
            return ResultObject.fail(ResultCode.USER_NOT_EXIST);
        }
        if (!user.normal()){
            return ResultObject.fail(ResultCode.USER_ILLEGAL_STATUS);
        }
        int ret = userService.changeStatus(userId, UserStatus.CLOSED.getCode());
        return ret > 0 ? ResultObject.ok() : ResultObject.fail(ResultCode.USER_CHANGE_STATUS_FAILED);
    }

    /**
     * 激活用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/active", method= {RequestMethod.POST, RequestMethod.GET})
    public ResultObject active(@RequestParam("user_id") Integer userId){
        User user = userService.get(userId);
        if (user == null){
            logger.warn("user {} not exist", userId);
            return ResultObject.fail(ResultCode.USER_NOT_EXIST);
        }
        if (!user.closed()){
            return ResultObject.fail(ResultCode.USER_ILLEGAL_STATUS);
        }
        int ret = userService.changeStatus(userId, UserStatus.NORMAL.getCode());
        return ret > 0 ? ResultObject.ok() : ResultObject.fail(ResultCode.USER_CHANGE_STATUS_FAILED);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/delete", method= {RequestMethod.POST, RequestMethod.GET})
    public ResultObject delete(@RequestParam("user_id") Integer userId){
        int ret = userService.changeStatus(userId, UserStatus.DELETED.getCode());
        return ret > 0 ? ResultObject.ok() : ResultObject.fail(ResultCode.USER_CHANGE_STATUS_FAILED);
    }
}
