package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserPoints;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     * get user by login name.
     *
     * @param login login name
     * @return user
     */
    User getUserByName(@Param("name")String login);

    /**
     * paging query user.
     *
     * @return
     */
    Page<User> findByPage(Map<String, Object> conditionMap);

    /**
     * 查找专家
     *
     * @param vo
     * @return
     */
    Page<User> searchSpecialist(SpecialistVo vo);

    /**
     * 专家评分
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> byUserAvgScore(@Param("userId")Integer userId);

    /**
     * 个人评分
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> userAvgScore(@Param("userId")Integer userId);

    /**
     * 可预约时间
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> appointmentTime(@Param("userId")Integer userId);

    /**
     * 可咨询方式
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> consultWay(@Param("userId")Integer userId);

    /**
     * 用户积分列表
     *
     * @param userId
     * @return
     */
    Page<UserPoints> listUserPoints(@Param("userId")Integer userId);

    /**
     * 列表主题专家
     *
     * @param topicId
     * @return
     */
    Page<User> topicSpecialists(@Param("topicId")Integer topicId);

    /**
     * 新建积分
     *
     * @param userPoints
     * @return
     */
    int insertPoints(UserPoints userPoints);

    /**
     * 增加用户的咨询次数及咨询时长
     *
     * @param userId
     * @param duration
     * @param byUser  true : 被咨询人  false: 咨询人
     * @return
     */
    int incrConsultation(@Param("userId")Integer userId, @Param("duration")Integer duration, @Param("byUser")Boolean byUser);

    /**
     * 增加爽约次数及好评，差评次数
     *
     * @param userId
     * @param contract
     * @param level
     * @return
     */
    int updateContractAndLevel(@Param("userId")Integer userId, @Param("contract")Boolean contract, @Param("level")Integer level);
}