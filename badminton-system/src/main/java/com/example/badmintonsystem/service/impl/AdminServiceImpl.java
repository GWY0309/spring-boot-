package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.*;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.*;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private CourtMapper courtMapper;

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private RacketMapper racketMapper;

    @Resource // 1. 注入 RacketRentalMapper
    private RacketRentalMapper racketRentalMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public Court addCourt(Court court) {
        courtMapper.insert(court);
        return court;
    }

    @Override
    public Court updateCourt(Integer id, Court court) {
        Court existingCourt = courtMapper.findById(id);
        if (existingCourt == null) {
            throw new CustomException("要更新的场地不存在");
        }
        court.setId(id);
        courtMapper.update(court);
        return court;
    }

    @Override
    public void deleteCourt(Integer id) {
        Court existingCourt = courtMapper.findById(id);
        if (existingCourt == null) {
            throw new CustomException("要删除的场地不存在");
        }
        courtMapper.deleteById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationMapper.findAll();
    }

    @Override
    public Racket addRacket(Racket racket) {
        racketMapper.insert(racket);
        return racket;
    }

    @Override
    public Racket updateRacket(Integer id, Racket racket) {
        Racket existingRacket = racketMapper.findById(id);
        if (existingRacket == null) {
            throw new CustomException("要更新的球拍不存在");
        }
        racket.setId(id);
        racketMapper.update(racket);
        return racket;
    }

    @Override
    public void deleteRacket(Integer id) {
        Racket existingRacket = racketMapper.findById(id);
        if (existingRacket == null) {
            throw new CustomException("要删除的球拍不存在");
        }
        racketMapper.deleteById(id);
    }

    // --- 2. 以下是新增的两个方法 ---

    @Override
    public List<RacketRental> getAllRentals() {
        return racketRentalMapper.findAll();
    }

    @Override
    @Transactional
    public RacketRental forceReturnRacket(Long rentalId) {
        // 1. 检查租借记录是否存在
        RacketRental rental = racketRentalMapper.findById(rentalId);
        if (rental == null) {
            throw new CustomException("租借记录不存在");
        }
        if (rental.getStatus() == 1) { // 1 表示已归还
            throw new CustomException("该球拍已归还，请勿重复操作");
        }

        // 2. 获取球拍信息用于计算费用
        Racket racket = racketMapper.findById(rental.getRacketId());
        if (racket == null) {
            throw new CustomException("关联的球拍信息不存在，请联系技术支持");
        }

        // 3. 计算租借时长和费用
        LocalDateTime returnTime = LocalDateTime.now();
        Duration duration = Duration.between(rental.getRentalTime(), returnTime);
        long hours = (long) Math.ceil(duration.toMinutes() / 60.0); // 不足一小时按一小时算
        hours = Math.max(hours, 1); // 至少算一小时

        BigDecimal totalCost = racket.getRentalPricePerHour().multiply(new BigDecimal(hours));

        // 4. 更新租借记录
        rental.setReturnTime(returnTime);
        rental.setTotalCost(totalCost);
        rental.setStatus(1); // 设置为“已归还”
        racketRentalMapper.update(rental);

        // 5. 更新球拍状态为“可用” (0)
        racketMapper.updateStatus(rental.getRacketId(), 0);

        return rental;
    }
}