package com.example.badmintonsystem.service;

import com.example.badmintonsystem.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     * @param user 包含用户名和密码的用户对象
     */
    void register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功后的 token 或用户信息
     */
    String login(String username, String password);
}