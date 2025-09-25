package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.Reservation;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.service.ReservationService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations") // 所有预约相关的接口，都以 /api/reservations 开头
public class ReservationController {

    @Resource
    private ReservationService reservationService;

    /**
     * 创建一个新的预约
     * @param reservation 包含场地ID(courtId)、预约日期(reservationDate)、
     * 开始时间(startTime)、结束时间(endTime)的JSON对象
     * @return 成功创建后的预约信息
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservation);
            return ResponseEntity.ok(createdReservation);
        } catch (CustomException e) {
            // 捕获业务异常，例如时间冲突、场地不存在等
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            return ResponseEntity.internalServerError().body("服务器内部错误：" + e.getMessage());
        }
    }
}