package com.wannengyongyao.drug.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CartVo {
    @NotNull
    private Integer drugId;
    private List<Integer> ids;
}
