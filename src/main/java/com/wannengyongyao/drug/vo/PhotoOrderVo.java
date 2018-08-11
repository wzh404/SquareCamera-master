package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.model.DrugOrderGoods;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhotoOrderVo extends OrderVo{
    List<PhotoDrugVo> drugs = new ArrayList<>();

    public List<DrugOrderGoods> asGoods(){
        List<DrugOrderGoods> goods = new ArrayList<>();
        for (PhotoDrugVo p : drugs){
            DrugOrderGoods g = new DrugOrderGoods();
            g.setPhotos(String.join(",", p.getPhotos()));
            g.setRemark(p.getRemark());
            g.setQuantity(p.getQuantity());

            g.setCreateTime(LocalDateTime.now());
            g.setUnitPrice(new BigDecimal(0.0));
            g.setDrugId(0);

            goods.add(g);
        }

        return goods;
    }
}
