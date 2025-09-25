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

    /**
     * 根据ID查询租借记录
     * @param id 租借ID
     * @return 租借记录对象
     */
    RacketRental findById(Long id);

    /**
     * 更新租借记录（用于归还）
     * @param rental 包含更新信息的租借对象
     * @return 更新的行数
     */
    int update(RacketRental rental);

}