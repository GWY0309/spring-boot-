package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.badmintonsystem.entity.Court;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.example.badmintonsystem.entity.Reservation;
import com.example.badmintonsystem.entity.Racket;
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

    @PutMapping("/courts/{id}")
    public ResponseEntity<?> updateCourt(@PathVariable Integer id, @RequestBody Court court) {
        try {
            Court updatedCourt = adminService.updateCourt(id, court);
            return ResponseEntity.ok(updatedCourt);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/courts/{id}")
    public ResponseEntity<?> deleteCourt(@PathVariable Integer id) {
        try {
            adminService.deleteCourt(id);
            return ResponseEntity.ok("场地已成功删除");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(adminService.getAllReservations());
    }

    @PostMapping("/rackets")
    public ResponseEntity<Racket> addRacket(@RequestBody Racket racket) {
        Racket newRacket = adminService.addRacket(racket);
        return ResponseEntity.ok(newRacket);
    }

    @PutMapping("/rackets/{id}")
    public ResponseEntity<?> updateRacket(@PathVariable Integer id, @RequestBody Racket racket) {
        try {
            Racket updatedRacket = adminService.updateRacket(id, racket);
            return ResponseEntity.ok(updatedRacket);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/rackets/{id}")
    public ResponseEntity<?> deleteRacket(@PathVariable Integer id) {
        try {
            adminService.deleteRacket(id);
            return ResponseEntity.ok("球拍已成功删除");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}