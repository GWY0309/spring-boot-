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

    Court findById(Integer id);

    /**
     * 插入一条新的场地记录
     * @param court 场地对象
     */
    void insert(Court court);
}