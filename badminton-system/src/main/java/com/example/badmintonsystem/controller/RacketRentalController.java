package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.RacketRental;
import com.example.badmintonsystem.service.RacketService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}