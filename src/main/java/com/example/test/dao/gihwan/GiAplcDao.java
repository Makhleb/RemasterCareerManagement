package com.example.test.dao.gihwan;

import com.example.test.dto.AplcHstrDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GiAplcDao {
    boolean aplcUpadte(@Param("A")AplcHstrDTO aplcHstrDTO);
}
