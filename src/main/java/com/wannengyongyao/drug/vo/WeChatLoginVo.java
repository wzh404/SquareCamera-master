package com.wannengyongyao.drug.vo;

import lombok.Data;

@Data
public class WeChatLoginVo {
    private String jscode;
    private String iv;
    private String encryptedData;
}
