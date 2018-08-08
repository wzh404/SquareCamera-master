package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DrugOrderImage implements Serializable {
    private Long id;

    private Long orderId;

    private String imageUrl;

    private Date createTime;
}