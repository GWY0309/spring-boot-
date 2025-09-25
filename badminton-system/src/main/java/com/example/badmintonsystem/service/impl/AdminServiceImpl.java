package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.mapper.CourtMapper;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.badmintonsystem.entity.Reservation;
import com.example.badmintonsystem.mapper.ReservationMapper;
import com.example.badmintonsystem.entity.Racket; // 导入 Racket
import com.example.badmintonsystem.mapper.RacketMapper;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserMapper userMapper;

    @Resource // 注入 CourtMapper
    private CourtMapper courtMapper;

    @Resource // 注入 ReservationMapper
    private ReservationMapper reservationMapper;

    @Resource // 注入 RacketMapper
    private RacketMapper racketMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public Court addCourt(Court court) {
        // 这里可以添加一些业务校验，例如检查场地名称是否重复等，我们暂时简化
        courtMapper.insert(court);
        return court;
    }

    @Override
    public Court updateCourt(Integer id, Court court) {
        // 1. 检查场地是否存在
        Court existingCourt = courtMapper.findById(id);
        if (existingCourt == null) {
            throw new CustomException("要更新的场地不存在");
        }
        // 2. 将传入的ID设置到更新对象中，确保更新的是正确的记录
        court.setId(id);

        // 3. 执行更新
        courtMapper.update(court);

        // 4. 返回更新后的对象
        return court;
    }

    @Override
    public void deleteCourt(Integer id) {
        // 1. 检查场地是否存在
        Court existingCourt = courtMapper.findById(id);
        if (existingCourt == null) {
            throw new CustomException("要删除的场地不存在");
        }
        // 2. 执行删除
        // 实际项目中，这里可能还需要检查该场地是否有未完成的预约，我们暂时简化
        courtMapper.deleteById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationMapper.findAll();
    }

    @Override
    public Racket addRacket(Racket racket) {
        // 同样，这里可以添加业务校验，我们暂时简化
        racketMapper.insert(racket);
        return racket;
    }

    @Override
    public Racket updateRacket(Integer id, Racket racket) {
        // 1. 检查球拍是否存在
        Racket existingRacket = racketMapper.findById(id);
        if (existingRacket == null) {
            throw new CustomException("要更新的球拍不存在");
        }
        // 2. 将传入的ID设置到更新对象中
        racket.setId(id);

        // 3. 执行更新
        racketMapper.update(racket);

        // 4. 返回更新后的对象
        return racket;
    }

    @Override
    public void deleteRacket(Integer id) {
        // 1. 检查球拍是否存在
        Racket existingRacket = racketMapper.findById(id);
        if (existingRacket == null) {
            throw new CustomException("要删除的球拍不存在");
        }
        // 2. 执行删除 (实际项目中可能需要检查球拍是否正在被租借)
        racketMapper.deleteById(id);
    }
}