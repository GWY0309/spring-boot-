package com.example.badmintonsystem.service;
import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.entity.User;
import java.util.List;

public interface AdminService {
    List<User> getAllUsers();

    /**
     * 添加一个新的场地
     * @param court 待添加的场地对象
     * @return 添加成功后的场地对象（包含生成的ID）
     */
    Court addCourt(Court court);
}