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
import org.springframework.web.bind.annotation.GetMapping; // 导入 GetMapping
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable; // 导入 PathVariable
import org.springframework.web.bind.annotation.PutMapping;   // 导入 PutMapping

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

    /**
     * 获取当前登录用户的所有预约记录
     * @return 预约记录列表
     */
    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> getMyReservations() {
        List<Reservation> reservations = reservationService.getMyReservations();
        return ResponseEntity.ok(reservations);
    }

    /**
     * 取消一个预约
     * @param id 要取消的预约ID，从URL路径中获取
     * @return 操作结果
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("预约已成功取消");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("服务器内部错误：" + e.getMessage());
        }
    }
}