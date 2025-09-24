package com.example.badmintonsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体类，对应 t_announcement 表
 */
@Data
public class Announcement {

    /**
     * 公告唯一ID
     */
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告正文
     */
    private String content;

    /**
     * 发布者ID (管理员)
     */
    private Long userId;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}