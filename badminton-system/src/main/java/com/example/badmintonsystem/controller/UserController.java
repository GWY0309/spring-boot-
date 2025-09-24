package com.example.badmintonsystem.controller;

import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.exception.CustomException;
import com.example.badmintonsystem.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // 声明这是一个 RESTful 风格的 Controller
@RequestMapping("/api/user") // 为该 Controller 下的所有接口设置一个统一的前缀，例如 /api/user
public class UserController {

    @Resource // 注入 UserService
    private UserService userService;

    /**
     * 用户注册接口
     * @param user 前端通过 JSON 格式传递过来的用户对象
     * @return 操作结果
     */
    @PostMapping("/register") // 映射 HTTP POST 请求到 /api/user/register 路径
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok("注册成功"); // 返回 200 OK 和成功信息
        } catch (CustomException e) {
            // 如果 Service 抛出我们自定义的异常（如用户名已存在），则捕获并返回错误信息
            return ResponseEntity.badRequest().body(e.getMessage()); // 返回 400 Bad Request 和错误详情
        } catch (Exception e) {
            // 捕获其他未知异常
            return ResponseEntity.internalServerError().body("服务器内部错误"); // 返回 500 Internal Server Error
        }
    }

    /**
     * 用户登录接口
     * @param user 包含用户名和密码的 JSON 对象
     * @return 包含 Token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = userService.login(user.getUsername(), user.getPassword());
            // 为了方便前端使用，我们返回一个包含 token 的 JSON 对象
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}