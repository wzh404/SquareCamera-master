package com.wannengyongyao.drug.vo;

import lombok.Data;

import java.util.List;

@Data
public class CartVo {
    private Integer drugId;
    private List<Integer> ids;
}
