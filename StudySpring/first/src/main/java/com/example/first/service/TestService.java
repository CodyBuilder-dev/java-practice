package com.example.first.service;

import com.example.first.mapper.TestMapper;
import com.example.first.vo.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    
    @Autowired
    public TestMapper mapper;

    public List<TestVo> selectTest() {
        return mapper.selectTest();
    }
}