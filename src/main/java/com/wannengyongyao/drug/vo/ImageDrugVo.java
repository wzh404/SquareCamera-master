package com.wannengyongyao.drug.vo;

import lombok.Data;

import java.util.List;

@Data
public class ImageDrugVo {
    private List<String> photos;
    private Integer quantity;
    private String remark;
}
