package com.example.test.dao.gihwan;

import com.example.test.dto.GubnDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2024-12-28 by 최기환
 */
@Mapper
public interface GubnDao {
    List<GubnDTO> getGubn(@Param("list") List<String> groupCodeList);
}
