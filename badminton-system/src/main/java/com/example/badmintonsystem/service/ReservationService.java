package com.example.badmintonsystem.service;

import com.example.badmintonsystem.entity.Reservation;
import java.util.List;

public interface ReservationService {

    /**
     * 创建一个新的预约
     * @param reservation 包含场地ID、预约日期和时间的预约请求对象
     * @return 创建成功后的预约对象
     */
    Reservation createReservation(Reservation reservation);

    /**
     * 获取当前登录用户的所有预约记录
     * @return 预约记录列表
     */
    List<Reservation> getMyReservations();

    /**
     * 取消一个预约
     * @param reservationId 要取消的预约ID
     */
    void cancelReservation(Long reservationId);
}