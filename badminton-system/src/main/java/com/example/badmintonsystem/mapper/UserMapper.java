package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 告诉 Spring Boot 这是一个 MyBatis 的 Mapper 接口
public interface UserMapper {

    // 我们将在这里定义数据库操作方法
    // 例如：根据用户名查询用户
    User findByUsername(String username);
    // 在 UserMapper.java 文件中，findByUsername 方法下面，添加新方法
    void insert(User user);
}