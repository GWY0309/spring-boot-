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

    /**
     * 获取当前登录用户的个人信息
     * @return 用户对象
     */
    User getProfile();

    /**
     * 更新当前登录用户的个人信息
     * @param user 包含要更新字段的用户对象
     */
    void updateProfile(User user);

    /**
     * 修改当前登录用户的密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(String oldPassword, String newPassword);
}