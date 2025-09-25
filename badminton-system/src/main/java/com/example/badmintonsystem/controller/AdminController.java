package com.example.badmintonsystem.controller;
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.badmintonsystem.entity.Court;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/admin") // 所有管理员接口都以此为前缀
public class AdminController {
    @Resource
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/courts")
    public ResponseEntity<Court> addCourt(@RequestBody Court court) {
        Court newCourt = adminService.addCourt(court);
        return ResponseEntity.ok(newCourt);
    }
}