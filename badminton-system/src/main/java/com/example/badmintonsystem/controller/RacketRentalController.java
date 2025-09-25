package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.RacketRental;
import com.example.badmintonsystem.service.RacketService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.badmintonsystem.exception.CustomException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RestController
@RequestMapping("/api/rentals") // 所有租借记录相关的接口
public class RacketRentalController {

    @Resource
    private RacketService racketService;

    /**
     * 获取当前登录用户的所有租借记录
     * @return 租借记录列表
     */
    @GetMapping("/my")
    public ResponseEntity<List<RacketRental>> getMyRentals() {
        List<RacketRental> rentals = racketService.getMyRentals();
        return ResponseEntity.ok(rentals);
    }

    /**
     * 归还一个球拍
     * @param id 要归还的租借记录ID
     * @return 更新后的租借记录详情
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnRacket(@PathVariable Long id) {
        try {
            RacketRental rental = racketService.returnRacket(id);
            return ResponseEntity.ok(rental);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("服务器内部错误：" + e.getMessage());
        }
    }
}