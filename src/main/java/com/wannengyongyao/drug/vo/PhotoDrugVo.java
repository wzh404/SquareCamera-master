package com.wannengyongyao.drug.vo;

import lombok.Data;

import java.util.List;

@Data
public class PhotoDrugVo {
    private List<String> photos;
    private Integer quantity;
    private String remark;
}
