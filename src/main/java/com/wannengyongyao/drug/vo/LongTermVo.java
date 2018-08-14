package com.wannengyongyao.drug.vo;

import lombok.Data;

import java.util.List;

@Data
public class LongTermVo {
    private Integer drugId;
    private List<Integer> ids;
}
