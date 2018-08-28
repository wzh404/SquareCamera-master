package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DrugOrderShare {
    private Integer id;
    private Long orderId;
    private String openid;
    private Integer shareType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
}
