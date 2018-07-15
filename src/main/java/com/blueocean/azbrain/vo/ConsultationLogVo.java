package com.blueocean.azbrain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 我要找专家vo
 */
@Data
public class ConsultationLogVo {
    private Integer id;

    // 咨询日期
    private LocalDate cdate;

    // 咨询方式
    private String way;

    // 预约开始时间
    private LocalTime startTime;

    // 预约结束时间
    private LocalTime endTime;

    // 手机号
    private String mobile;

    // 会议密码
    private String meetingPwd;

    // 会议主持人ID
    private String meetingHost;

    private Integer duration;

    private LocalDateTime lastUpdated;

    private String status;
}
