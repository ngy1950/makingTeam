package com.makingTeam.mapper;

import com.makingTeam.vo.sampleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SampleMapper {
    List<sampleVO> selectEmp();
}