package com.example.badmintonsystem.service;

import com.example.badmintonsystem.entity.Racket;
import com.example.badmintonsystem.entity.RacketRental;
import java.util.List;

public interface RacketService {
    /**
     * 获取所有可用的球拍列表
     * @return 可用球拍列表
     */
    List<Racket> getAvailableRackets();

    /**
     * 租借一个球拍
     * @param racketId 要租借的球拍ID
     * @return 创建成功后的租借记录
     */
    RacketRental rentRacket(Integer racketId);

    /**
     * 获取当前登录用户的所有租借记录
     * @return 租借记录列表
     */
    List<RacketRental> getMyRentals();
}