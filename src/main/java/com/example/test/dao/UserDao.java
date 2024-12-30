package com.example.test.dao;

import com.example.test.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    
    UserDTO findById(String userId);
    
    boolean existsById(String userId);
    
    boolean existsByEmail(String email);
    
    void save(UserDTO user);
} 