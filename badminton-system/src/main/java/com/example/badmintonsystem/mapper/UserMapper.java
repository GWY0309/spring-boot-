package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List; // 导入 List

@Mapper // 告诉 Spring Boot 这是一个 MyBatis 的 Mapper 接口
public interface UserMapper {

    User findByUsername(String username);

    void insert(User user);

    List<User> findAll();

    /**
     * 更新用户信息
     * @param user 包含ID和待更新字段的用户对象
     * @return 更新的行数
     */
    int update(User user);

    User findById(Long id);

    int deleteById(Long id);
}