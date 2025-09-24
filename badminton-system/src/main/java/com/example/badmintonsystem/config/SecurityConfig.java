package com.example.badmintonsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 声明这是一个配置类
public class SecurityConfig {

    @Bean // 将该方法的返回值（一个对象）注册为 Spring 容器中的 Bean
    public PasswordEncoder passwordEncoder() {
        // 返回 BCryptPasswordEncoder 实例，这是目前非常安全的一种加密方式
        return new BCryptPasswordEncoder();
    }
}