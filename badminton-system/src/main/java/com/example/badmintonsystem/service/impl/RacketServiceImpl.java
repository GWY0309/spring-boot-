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
        // 1. 获取当前登录用户
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("用户未登录或不存在");
        }

        // 2. 检查球拍是否存在且可用
        Racket racket = racketMapper.findById(racketId);
        if (racket == null) {
            throw new CustomException("球拍不存在");
        }
        if (racket.getStatus() != 0) { // 0 表示可用
            throw new CustomException("该球拍已被租出或正在维修中");
        }

        // 3. 更新球拍状态为“已租出” (1)
        racketMapper.updateStatus(racketId, 1);

        // 4. 创建新的租借记录
        RacketRental rental = new RacketRental();
        rental.setRacketId(racketId);
        rental.setUserId(currentUser.getId());
        racketRentalMapper.insert(rental);

        return rental;
    }

    @Override
    public List<RacketRental> getMyRentals() {
        // 1. 获取当前登录用户的信息
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new CustomException("无法获取用户信息，请重新登录");
        }

        // 2. 根据用户ID查询其租借记录
        return racketRentalMapper.findByUserId(currentUser.getId());
    }
}