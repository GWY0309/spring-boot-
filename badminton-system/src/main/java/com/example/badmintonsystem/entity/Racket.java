package com.example.badmintonsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 球拍实体类，对应 t_racket 表
 */
@Data
public class Racket {

    private Integer id;
    private String brand;
    private String model;
    private Integer status;
    private BigDecimal rentalPricePerHour;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}