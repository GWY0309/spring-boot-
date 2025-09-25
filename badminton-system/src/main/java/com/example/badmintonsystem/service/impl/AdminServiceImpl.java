package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.Court; // 导入 Court
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.mapper.CourtMapper; // 导入 CourtMapper
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserMapper userMapper;

    @Resource // 注入 CourtMapper
    private CourtMapper courtMapper;

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
}