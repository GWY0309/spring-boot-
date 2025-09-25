package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.entity.Reservation;
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.CourtMapper;
import com.example.badmintonsystem.mapper.ReservationMapper;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.ReservationService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourtMapper courtMapper; // 我们需要 CourtMapper 来获取场地信息

    @Override
    @Transactional // 添加事务注解，确保操作的原子性
    public Reservation createReservation(Reservation reservation) {
        // 1. 获取当前登录的用户名
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("用户未登录或不存在");
        }
        // 将当前用户的ID设置到预约对象中
        reservation.setUserId(currentUser.getId());

        // 为了计算费用，我们需要查询场地的单价

        Court court = courtMapper.findById(reservation.getCourtId());
        if (court == null) {
            throw new CustomException("场地不存在");
        }

        // 3. 检查时间冲突
        List<Reservation> overlapping = reservationMapper.findOverlappingReservations(
                reservation.getCourtId(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (!overlapping.isEmpty()) {
            throw new CustomException("该时间段已被预约，请选择其他时间");
        }

        // 4. 计算费用
        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours <= 0) {
            throw new CustomException("预约时间不合法");
        }
        BigDecimal totalCost = court.getPricePerHour().multiply(new BigDecimal(hours));
        reservation.setTotalCost(totalCost);

        // 5. 插入数据库
        reservationMapper.insert(reservation);

        return reservation;
    }
}