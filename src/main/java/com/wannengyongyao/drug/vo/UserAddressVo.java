package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.model.DrugUserAddress;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAddressVo {
    private String address;
    private String district;

    private Long userId;

    public DrugUserAddress asAddress(){
        DrugUserAddress address = new DrugUserAddress();
        address.setAddress(this.address);
        address.setDistrict(this.district);
        address.setCreateTime(LocalDateTime.now());
        address.setStatus(0);
        address.setUserId(this.userId);

        return address;
    }
}

