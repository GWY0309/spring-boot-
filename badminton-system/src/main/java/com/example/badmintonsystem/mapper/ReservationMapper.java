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

    /**
     * 根据用户ID查询其所有预约记录
     * @param userId 用户ID
     * @return 该用户的预约列表
     */
    List<Reservation> findByUserId(Long userId);

    /**
     * 根据ID查询预约记录
     * @param id 预约ID
     * @return 预约对象
     */
    Reservation findById(Long id);

    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 新的状态
     * @return 更新的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * (管理员)查询系统内所有预约记录
     * @return 所有预约记录列表
     */
    List<Reservation> findAll();
}