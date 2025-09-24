package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder; // 注入我们在 SecurityConfig 中创建的加密器

    @Override
    public void register(User user) {
        // 1. 数据校验
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            throw new CustomException("用户名或密码不能为空");
        }

        // 2. 唯一性检查
        User dbUser = userMapper.findByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomException("用户名 '" + user.getUsername() + "' 已被注册");
        }

        // 3. 密码加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 4. 设置默认值并存入数据库
        user.setRole("USER"); // 默认角色为普通用户
        userMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        // 登录逻辑我们稍后实现
        System.out.println("执行登录业务逻辑...");
        return null;
    }
}