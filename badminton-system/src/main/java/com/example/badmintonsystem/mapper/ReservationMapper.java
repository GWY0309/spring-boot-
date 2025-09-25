package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 导入 Param 注解

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ReservationMapper {

    /**
     * 查询指定场地在特定时间段内是否有已存在的预约
     * @param courtId 场地ID
     * @param date 预约日期
     * @param startTime 预约开始时间
     * @param endTime 预约结束时间
     * @return 冲突的预约列表
     */
    List<Reservation> findOverlappingReservations(
            @Param("courtId") Integer courtId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    /**
     * 插入一条新的预约记录
     * @param reservation 预约对象
     */
    void insert(Reservation reservation);
}