package com.example.badmintonsystem.service;

import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.entity.User;
import com.example.badmintonsystem.entity.Reservation;
import com.example.badmintonsystem.entity.Racket;
import java.util.List;

public interface AdminService {
    List<User> getAllUsers();

    /**
     * 添加一个新的场地
     * @param court 待添加的场地对象
     * @return 添加成功后的场地对象（包含生成的ID）
     */
    Court addCourt(Court court);

    /**
     * 更新一个场地的信息
     * @param id 待更新的场地ID
     * @param court 包含更新后信息的场地对象
     * @return 更新成功后的场地对象
     */
    Court updateCourt(Integer id, Court court);

    /**
     * 删除一个场地
     * @param id 待删除的场地ID
     */
    void deleteCourt(Integer id);

    /**
     * 获取系统内所有的预约记录
     * @return 预约记录列表
     */
    List<Reservation> getAllReservations();

    /**
     * 添加一个新的球拍
     * @param racket 待添加的球拍对象
     * @return 添加成功后的球拍对象（包含生成的ID）
     */
    Racket addRacket(Racket racket);

    /**
     * 更新一个球拍的信息
     * @param id 待更新的球拍ID
     * @param racket 包含更新后信息的球拍对象
     * @return 更新成功后的球拍对象
     */
    Racket updateRacket(Integer id, Racket racket);

    /**
     * 删除一个球拍
     * @param id 待删除的球拍ID
     */
    void deleteRacket(Integer id);
}