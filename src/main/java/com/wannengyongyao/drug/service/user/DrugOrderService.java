package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.vo.DrugOrderVo;
import com.wannengyongyao.drug.vo.PhotoOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DrugOrderService {
    /**
     * 用户下单
     *
     * @param orderVo
     * @return
     */
    Integer newOrder(DrugOrderVo orderVo);

    Integer newPhotoOrder(PhotoOrderVo orderVo);

    int cancelOrder(Long userId, Long orderId);

    List<DrugOrder> list(Map<String, Object> conditionMap);

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    DrugOrder orderDetail(Long orderId);
}
