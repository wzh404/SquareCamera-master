package com.wannengyongyao.drug.vo;

import lombok.Data;

import java.util.List;

@Data
public class ImageOrderVo extends OrderVo{
    private List<ImageDrugVo> drugs;
}
