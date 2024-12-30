package com.example.test.dao.gihwan;

import com.example.test.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2024-12-30 by 최기환
 */
@Mapper
public interface FileDao {
    int insertFile(FileDto fileDto);
}
