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
import org.springframework.security.core.context.SecurityContextHolder; // 1. 导入
import org.springframework.security.core.userdetails.UserDetails;

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
        return jwtUtil.generateToken(dbUser);
    }

    @Override
    public User getProfile() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userMapper.findByUsername(username);
        if (user != null) {
            user.setPassword(null); // 安全起见，不返回密码
        }
        return user;
    }

    @Override
    public void updateProfile(User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);

        user.setId(currentUser.getId()); // 确保更新的是当前用户
        userMapper.update(user);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userMapper.findByUsername(username);

        // 1. 验证旧密码是否正确
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new CustomException("旧密码不正确");
        }

        // --- 新增逻辑在这里 ---
        // 2. 检查新旧密码是否相同
        if (oldPassword.equals(newPassword)) {
            throw new CustomException("新密码不能与当前密码相同");
        }
        // --------------------

        // 3. 加密新密码 (原步骤2)
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // 4. 更新密码到数据库 (原步骤3)
        User userToUpdate = new User();
        userToUpdate.setId(currentUser.getId());
        userToUpdate.setPassword(encodedNewPassword);
        userMapper.update(userToUpdate);
    }
}