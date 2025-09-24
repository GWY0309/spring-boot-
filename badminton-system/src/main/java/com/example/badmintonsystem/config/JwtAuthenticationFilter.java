package com.example.badmintonsystem.config;

import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.mapper.UserMapper;
import com.example.badmintonsystem.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private UserMapper userMapper; // 我们直接使用 UserMapper 查询用户信息

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. 如果没有 Authorization 头，或者不是以 "Bearer " 开头，直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 提取 Token
        jwt = authHeader.substring(7);

        // 3. 从 Token 中解析出用户名
        username = jwtUtil.extractUsername(jwt);

        // 4. 验证 Token
        // 如果用户名存在，并且当前用户的认证信息为空 (表示用户还未登录)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从数据库中加载用户信息
            User user = this.userMapper.findByUsername(username);

            // 将我们自己的 User 对象转换为 Spring Security 需要的 UserDetails 对象
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>() // 权限列表，暂时为空
            );

            // 如果 Token 有效
            if (jwtUtil.validateToken(jwt, user)) {
                // 创建一个认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将认证令牌设置到 SecurityContext 中，表示当前用户已通过认证
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 5. 放行请求，让它继续走向下一个过滤器
        filterChain.doFilter(request, response);
    }
}