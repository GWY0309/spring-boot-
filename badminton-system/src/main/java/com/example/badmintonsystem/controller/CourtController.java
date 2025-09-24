package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.service.CourtService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courts") // 所有和场地相关的接口，都以 /api/courts 开头
public class CourtController {

    @Resource
    private CourtService courtService;

    /**
     * 获取所有场地列表
     * 这是一个受保护的接口，用户需要登录后才能访问
     * @return 场地列表的 JSON 数组
     */
    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts() {
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok(courts);
    }
}