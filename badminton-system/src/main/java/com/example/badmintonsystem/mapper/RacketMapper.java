package com.example.badmintonsystem.mapper;

import com.example.badmintonsystem.entity.Racket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RacketMapper {
    /**
     * 查询所有状态为“可用”的球拍
     * @return 可用球拍列表
     */
    List<Racket> findAvailableRackets();

    /**
     * 根据ID查询球拍信息
     * @param id 球拍ID
     * @return 球拍对象
     */
    Racket findById(Integer id);

    /**
     * 更新球拍状态
     * @param id 球拍ID
     * @param status 新的状态
     * @return 更新的行数
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}