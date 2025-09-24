package com.example.badmintonsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 场地实体类，对应 t_court 表
 */
@Data
public class Court {

    /**
     * 场地唯一ID
     */
    private Integer id;

    /**
     * 场地名称
     */
    private String name;

    /**
     * 场地类型
     */
    private String type;

    /**
     * 场地状态 (0-可用, 1-维修中, 2-已停用)
     */
    private Integer status;

    /**
     * 每小时价格
     */
    private BigDecimal pricePerHour;

    /**
     * 每日开放时间
     */
    private LocalTime openTime;

    /**
     * 每日关闭时间
     */
    private LocalTime closeTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}