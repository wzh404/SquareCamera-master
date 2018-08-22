package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugUserAddressMapper {
    /**
     * 保存用户地址
     *
     * @param address
     * @return
     */
    int insert(DrugUserAddress address);

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    int changeStatus(@Param("id")Long id, @Param("status")Integer status);

    /**
     * 取消默认地址
     *
     * @param userId
     * @return
     */
    int undefaultStatus(@Param("userId")Long userId);

    /**
     * 列表用户地址
     *
     * @param userId
     * @return
     */
    List<DrugUserAddress> list(@Param("userId")Long userId);

    /**
     *
     * @param id
     * @return
     */
    DrugUserAddress get(@Param("id")Long id);

    /**
     *
     * @param address
     * @return
     */
    int changeAddress(DrugUserAddress address);
}
