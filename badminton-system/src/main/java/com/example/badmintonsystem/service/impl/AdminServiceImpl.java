package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.*;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.*;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public List<Reservation> getAllReservations(Long userId, Integer status) {
        return reservationMapper.findAll(userId, status);
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
    public List<RacketRental> getAllRentals(Long userId, Integer status) {
        return racketRentalMapper.findAll(userId, status);
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

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        // 管理员创建预约时，也需要检查时间冲突
        List<Reservation> overlapping = reservationMapper.findOverlappingReservations(
                reservation.getCourtId(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (!overlapping.isEmpty()) {
            throw new CustomException("该时间段已被预约，请选择其他时间");
        }

        // 同样需要计算费用
        Court court = courtMapper.findById(reservation.getCourtId());
        if (court == null) {
            throw new CustomException("场地不存在");
        }
        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours <= 0) {
            throw new CustomException("预约时间不合法");
        }
        BigDecimal totalCost = court.getPricePerHour().multiply(new BigDecimal(hours));
        reservation.setTotalCost(totalCost);

        reservationMapper.insert(reservation);
        return reservation;
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = reservationMapper.findById(id);
        if (existingReservation == null) {
            throw new CustomException("要更新的预约不存在");
        }
        // TODO: 在实际项目中，这里也应该有更复杂的业务校验
        reservation.setId(id);

        // 更新时也需要重新计算费用
        Court court = courtMapper.findById(reservation.getCourtId());
        if (court == null) {
            throw new CustomException("场地不存在");
        }
        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        if (hours <= 0) {
            throw new CustomException("预约时间不合法");
        }
        BigDecimal totalCost = court.getPricePerHour().multiply(new BigDecimal(hours));
        reservation.setTotalCost(totalCost);

        // 这里我们简单地用一个update方法，需要在Mapper中添加
        reservationMapper.update(reservation); // 注意：我们需要在Mapper中添加一个update方法
        return reservation;
    }

    @Override
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation == null) {
            throw new CustomException("预约记录不存在");
        }
        if (reservation.getStatus() == 2) { // 2 代表已取消
            throw new CustomException("该预约已被取消，请勿重复操作");
        }
        reservationMapper.updateStatus(id, 2);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new CustomException("要更新的用户不存在");
        }
        // 只允许更新部分字段
        existingUser.setNickname(user.getNickname());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());

        userMapper.update(existingUser);
        return existingUser;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 1. 获取当前操作的管理员信息
        String currentAdminUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentAdmin = userMapper.findByUsername(currentAdminUsername);

        // 2. 检查是否在删除自己
        if (currentAdmin.getId().equals(id)) {
            throw new CustomException("无法删除自己");
        }

        // 3. 检查是否在删除其他管理员
        User userToDelete = userMapper.findById(id);
        if (userToDelete == null) {
            throw new CustomException("要删除的用户不存在");
        }
        if ("ADMIN".equals(userToDelete.getRole())) {
            throw new CustomException("不允许删除其他管理员账户");
        }

        // 4. 执行删除
        userMapper.deleteById(id);
    }
}