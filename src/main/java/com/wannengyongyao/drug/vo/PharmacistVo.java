package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.common.status.PharmacistStatus;
import com.wannengyongyao.drug.model.DrugSeller;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PharmacistVo {
    private Long id;
    private String openid;
    private String mobile;
    private String code;
    private Integer storeId;

    private String name;
    private String job;
    private String idNumber;

    public DrugSeller asPharmacist(){
        DrugSeller s = new DrugSeller();
        s.setId(id);
        s.setAccountBalance(new BigDecimal(0.0));
        s.setMobile(this.mobile);
        s.setName(this.mobile);
        s.setStoreId(this.storeId);
        s.setGender(0);
        s.setCreateTime(LocalDateTime.now());
        s.setLastUpdatedTime(LocalDateTime.now());
        s.setCreateIp("localhost");
        s.setClassify("01");
        s.setStatus(PharmacistStatus.REGISTER.get());
        s.setReceiveStatus(0);

        return s;
    }

    public DrugSeller asEditedPharmacist(){
        DrugSeller s = new DrugSeller();
        s.setId(id);
        s.setName(this.name);
        s.setJob(this.job);
        s.setIdNumber(this.idNumber);

        return s;
    }
}
