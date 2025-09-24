package com.example.badmintonsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 球拍租借记录实体类，对应 t_racket_rental 表
 */
@Data
public class RacketRental {

    private Long id;
    private Integer racketId;
    private Long userId;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private BigDecimal totalCost;
    private Integer status;
}