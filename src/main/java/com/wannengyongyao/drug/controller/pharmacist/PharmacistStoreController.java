package com.wannengyongyao.drug.controller.pharmacist;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.service.pharmacist.StoreService;
import com.wannengyongyao.drug.util.DrugConstants;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pharmacist")
public class PharmacistStoreController {
    @Autowired
    private StoreService storeService;

    /**
     * 分页查询药店信息（所在省市地区，药店名称)
     *
     * @param page
     * @param name
     * @param province
     * @param city
     * @param district
     * @return
     */
    @RequestMapping(value="/common/stores", method= {RequestMethod.GET})
    public ResultObject stores(@RequestParam("page") Integer page,
                               @RequestParam(value="name", required = false)String name,
                               @RequestParam(value="province", required = false)String province,
                               @RequestParam(value="city", required = false)String city,
                               @RequestParam(value="district", required = false)String district){
        Map<String, Object> conditionMap = new HashMap<>();
        if (!StringUtils.isEmpty(name)){
            conditionMap.put("name", name);
        }
        if (!StringUtils.isEmpty(province)){
            conditionMap.put("province", province);
        }
        if (!StringUtils.isEmpty(city)){
            conditionMap.put("city", city);
        }
        if (!StringUtils.isEmpty(district)){
            conditionMap.put("district", district);
        }

        Page<DrugStore> storePage = storeService.list(page, DrugConstants.PAGE_SIZE, conditionMap);
        return ResultObject.ok(storePage.getResult());
    }
}
