package com.wannengyongyao.drug.controller.user;

import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.service.user.DrugStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class DrugStoreController {
    @Autowired
    private DrugStoreService storeService;

    /**
     * 附近药店
     *
     * @param lon
     * @param lat
     * @return
     */
    @RequestMapping(value="/common/drugstore", method= {RequestMethod.GET})
    public ResultObject drugstore(@RequestParam("lon")Double lon,
                                  @RequestParam("lat")Double lat,
                                  @RequestParam(value="name", required = false)String name){
        List<DrugStore> stores = storeService.nearby(lon, lat, name, 5);

        return ResultObject.ok(stores);
    }
}
