package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Drug  {
    private Integer id;

    private String approvalNumber;

    private String name;

    private String ename;

    private String dosageForm;

    private String specifications;

    private String manufacturer;

    private String manufacturerAddress;

    private String classify;

    private String issuingDate;

    private String usage;

    private String functions;

    private String otc;

    private String validityDay;

    private Date createTime;

    private String book;
}