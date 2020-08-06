package com.example.first.mapper;

import java.util.List;

import com.example.first.vo.TestVo; 
import org.apache.ibatis.annotations.Mapper; 
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMapper {
    List<TestVo> selectTest();
}