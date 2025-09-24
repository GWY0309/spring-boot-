package com.example.badmintonsystem.service.impl;

import com.example.badmintonsystem.entity.Court;
import com.example.badmintonsystem.mapper.CourtMapper;
import com.example.badmintonsystem.service.CourtService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    @Resource
    private CourtMapper courtMapper;

    @Override
    public List<Court> getAllCourts() {
        return courtMapper.findAll();
    }
}