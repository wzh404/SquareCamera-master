package com.wannengyongyao.drug.dao;


import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.Drug;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugMapper {
    /**
     * 根据名称搜索药品
     *
     * @param name
     * @return
     */
    Page<Drug> search(@Param("name")String name);

    /**
     * 获取药品信息
     *
     * @param id
     * @return
     */
    Drug get(@Param("id")Integer id);
}