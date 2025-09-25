package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.Racket;
import com.example.badmintonsystem.entity.RacketRental;
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.RacketMapper;
import com.example.badmintonsystem.mapper.RacketRentalMapper;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.RacketService;
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
public class RacketServiceImpl implements RacketService {

    @Resource
    private RacketMapper racketMapper;
    @Resource
    private RacketRentalMapper racketRentalMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Racket> getAvailableRackets() {
        return racketMapper.findAvailableRackets();
    }

    @Override
    @Transactional
    public RacketRental rentRacket(Integer racketId) {
        // ... (此部分代码不变)
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("用户未登录或不存在");
        }
        Racket racket = racketMapper.findById(racketId);
        if (racket == null) {
            throw new CustomException("球拍不存在");
        }
        if (racket.getStatus() != 0) {
            throw new CustomException("该球拍已被租出或正在维修中");
        }
        racketMapper.updateStatus(racketId, 1);
        RacketRental rental = new RacketRental();
        rental.setRacketId(racketId);
        rental.setUserId(currentUser.getId());
        racketRentalMapper.insert(rental);
        return rental;
    }

    @Override
    public List<RacketRental> getMyRentals() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("无法获取用户信息，请重新登录");
        }
        return racketRentalMapper.findByUserId(currentUser.getId());
    }

    @Override
    @Transactional
    public RacketRental returnRacket(Long rentalId) {
        // 1. 获取当前登录用户
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);

        // 2. 检查租借记录是否存在且属于当前用户
        RacketRental rental = racketRentalMapper.findById(rentalId);
        if (rental == null) {
            throw new CustomException("租借记录不存在");
        }
        if (!rental.getUserId().equals(currentUser.getId())) {
            throw new CustomException("无权操作他人的租借记录");
        }
        if (rental.getStatus() == 1) { // 1 表示已归还
            throw new CustomException("该球拍已归还，请勿重复操作");
        }

        // 3. 获取球拍信息用于计算费用
        Racket racket = racketMapper.findById(rental.getRacketId());
        if (racket == null) {
            throw new CustomException("关联的球拍信息不存在，请联系管理员");
        }

        // 4. 计算租借时长和费用
        LocalDateTime returnTime = LocalDateTime.now();
        Duration duration = Duration.between(rental.getRentalTime(), returnTime);
        long hours = (long) Math.ceil(duration.toMinutes() / 60.0); // 不足一小时按一小时算
        hours = Math.max(hours, 1); // 至少算一小时

        BigDecimal totalCost = racket.getRentalPricePerHour().multiply(new BigDecimal(hours));

        // 5. 更新租借记录
        rental.setReturnTime(returnTime);
        rental.setTotalCost(totalCost);
        rental.setStatus(1); // 设置为“已归还”
        racketRentalMapper.update(rental);

        // 6. 更新球拍状态为“可用” (0)
        racketMapper.updateStatus(rental.getRacketId(), 0);

        return rental;
    }
}