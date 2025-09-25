package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.RacketRental;
import org.apache.ibatis.annotations.Mapper;
import java.util.List; // 导入 List

@Mapper
public interface RacketRentalMapper {
    /**
     * 插入一条新的租借记录
     * @param rental 租借记录对象
     */
    void insert(RacketRental rental);

    /**
     * 根据用户ID查询其所有租借记录
     * @param userId 用户ID
     * @return 该用户的租借列表
     */
    List<RacketRental> findByUserId(Long userId);
}