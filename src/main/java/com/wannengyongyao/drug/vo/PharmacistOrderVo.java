package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.model.DrugSellerOrder;
import com.wannengyongyao.drug.model.DrugSellerOrderGoods;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PharmacistOrderVo {
    private Long orderId;

    private List<PharmacistOrderDrugVo> drugs;

    public DrugSellerOrder asSellerOrder(){
        DrugSellerOrder order = new DrugSellerOrder();
        order.setOrderId(orderId);

        return order;
    }

    public List<DrugSellerOrderGoods> asSellerOrderGoods(){
        List<DrugSellerOrderGoods> goods = new ArrayList<>();
        for (PharmacistOrderDrugVo v : drugs){
            DrugSellerOrderGoods g = new DrugSellerOrderGoods();
            g.setOrderDrugId(v.getId());
            g.setOrderId(this.orderId);
            g.setUnitPrice(v.getUnitPrice());

            goods.add(g);
        }
        return goods;
    }
}