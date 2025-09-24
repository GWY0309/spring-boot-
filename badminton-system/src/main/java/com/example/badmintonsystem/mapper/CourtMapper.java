package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.Court;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourtMapper {
    /**
     * 查询所有场地信息
     * @return 场地列表
     */
    List<Court> findAll();
}