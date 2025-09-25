package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.RacketRental;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RacketRentalMapper {
    /**
     * 插入一条新的租借记录
     * @param rental 租借记录对象
     */
    void insert(RacketRental rental);
}