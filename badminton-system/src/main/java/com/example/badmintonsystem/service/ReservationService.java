package com.example.badmintonsystem.service;

import com.example.badmintonsystem.entity.Reservation;

public interface ReservationService {

    /**
     * 创建一个新的预约
     * @param reservation 包含场地ID、预约日期和时间的预约请求对象
     * @return 创建成功后的预约对象
     */
    Reservation createReservation(Reservation reservation);
}