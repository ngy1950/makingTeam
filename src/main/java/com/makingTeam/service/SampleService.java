package com.makingTeam.service;

import com.makingTeam.mapper.SampleMapper;
import com.makingTeam.vo.sampleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    @Autowired
    public SampleMapper mapper;

    public List<sampleVO> selectEmp(){
        return mapper.selectEmp();
    }


}
