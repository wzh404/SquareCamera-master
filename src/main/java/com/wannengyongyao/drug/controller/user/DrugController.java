package com.wannengyongyao.drug.controller.user;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.model.Drug;
import com.wannengyongyao.drug.model.DrugRank;
import com.wannengyongyao.drug.service.user.DrugService;
import com.wannengyongyao.drug.util.DrugConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class DrugController {
    @Autowired
    private DrugService drugService;

    @RequestMapping(value="/common/search", method= {RequestMethod.GET})
    public ResultObject search(@RequestParam("page")int page,
                               @RequestParam("name")String name){
        Page<Drug> drugPage = drugService.search(page, DrugConstants.PAGE_SIZE, name);
        return ResultObject.ok(drugPage.getResult());
    }

    /**
     * 关注度排行
     *
     * @param page
     * @return
     */
    @RequestMapping(value="/common/hot/drug", method= {RequestMethod.GET})
    public ResultObject hot(@RequestParam("page")int page){
        Page<DrugRank> ranks = drugService.listDrugHotRank(page, DrugConstants.PAGE_SIZE);
        return ResultObject.ok(ranks.getResult());
    }

    /**
     * 稀少度排行
     *
     * @param page
     * @return
     */
    @RequestMapping(value="/common/rare/drug", method= {RequestMethod.GET})
    public ResultObject rare(@RequestParam("page")int page){
        Page<DrugRank> ranks = drugService.listDrugRareRank(page, DrugConstants.PAGE_SIZE);
        return ResultObject.ok(ranks.getResult());
    }

    /**
     * 成功率排行
     *
     * @param page
     * @return
     */
    @RequestMapping(value="/common/success/drug", method= {RequestMethod.GET})
    public ResultObject success(@RequestParam("page")int page){
        Page<DrugRank> ranks = drugService.listDrugSuccessRank(page, DrugConstants.PAGE_SIZE);
        return ResultObject.ok(ranks.getResult());
    }
}
