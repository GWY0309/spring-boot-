package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.service.UserService;
import com.example.badmintonsystem.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtUtil jwtUtil; // 注入 JWT 工具类

    @Override
    public void register(User user) {
        // ... 注册逻辑保持不变 ...
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            throw new CustomException("用户名或密码不能为空");
        }
        User dbUser = userMapper.findByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomException("用户名 '" + user.getUsername() + "' 已被注册");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        userMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        // 1. 根据用户名查询用户
        User dbUser = userMapper.findByUsername(username);

        // 2. 检查用户是否存在
        if (dbUser == null) {
            throw new CustomException("用户名或密码错误");
        }

        // 3. 验证密码
        // passwordEncoder.matches 会将明文密码加密后与数据库中的密文进行比较
        if (!passwordEncoder.matches(password, dbUser.getPassword())) {
            throw new CustomException("用户名或密码错误");
        }

        // 4. 登录成功，生成 JWT Token
        return jwtUtil.generateToken(dbUser.getUsername());
    }
}