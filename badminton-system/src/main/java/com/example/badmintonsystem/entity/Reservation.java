package com.example.badmintonsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 预约记录实体类，对应 t_reservation 表
 */
@Data
public class Reservation {

    /**
     * 预约记录唯一ID
     */
    private Long id;

    /**
     * 预约用户的ID
     */
    private Long userId;

    /**
     * 预约场地的ID
     */
    private Integer courtId;

    /**
     * 预约的日期
     */
    private LocalDate reservationDate;

    /**
     * 预约开始时间
     */
    private LocalTime startTime;

    /**
     * 预约结束时间
     */
    private LocalTime endTime;

    /**
     * 总费用
     */
    private BigDecimal totalCost;

    /**
     * 预约状态 (0-已预约, 1-已完成, 2-已取消)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}