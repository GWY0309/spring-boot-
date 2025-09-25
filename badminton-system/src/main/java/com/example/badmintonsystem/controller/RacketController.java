package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.Racket;
import com.example.badmintonsystem.entity.RacketRental;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.service.RacketService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rackets") // 所有球拍相关的接口，都以 /api/rackets 开头
public class RacketController {

    @Resource
    private RacketService racketService;

    /**
     * 获取所有可用的球拍列表
     * @return 可用球拍列表
     */
    @GetMapping
    public ResponseEntity<List<Racket>> getAvailableRackets() {
        List<Racket> rackets = racketService.getAvailableRackets();
        return ResponseEntity.ok(rackets);
    }

    /**
     * 租借一个指定的球拍
     * @param id 要租借的球拍ID
     * @return 成功创建的租借记录
     */
    @PostMapping("/{id}/rent")
    public ResponseEntity<?> rentRacket(@PathVariable Integer id) {
        try {
            RacketRental rental = racketService.rentRacket(id);
            return ResponseEntity.ok(rental);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("服务器内部错误：" + e.getMessage());
        }
    }
}