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

    /**
     * 更新场地信息
     * @param court 包含ID和待更新字段的场地对象
     * @return 更新的行数
     */
    int update(Court court);

    /**
     * 根据ID删除场地
     * @param id 场地ID
     * @return 删除的行数
     */
    int deleteById(Integer id);
}