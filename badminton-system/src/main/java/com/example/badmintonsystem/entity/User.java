package com.example.badmintonsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类，对应数据库中的 t_user 表
 */
@Data // Lombok 注解，自动生成 Getter、Setter、toString 等方法
public class User {

    /**
     * 用户唯一ID (主键, 自增)
     */
    private Long id;

    /**
     * 登录用户名 (唯一)
     */
    private String username;

    /**
     * 登录密码 (加密后)
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 角色 ('USER', 'ADMIN')
     */
    private String role;

    /**
     * 账号创建时间
     */
    private LocalDateTime createTime;

    /**
     * 信息最后更新时间
     */
    private LocalDateTime updateTime;
}