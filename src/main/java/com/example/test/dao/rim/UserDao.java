package com.example.test.dao.rim;

import com.example.test.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    
    Optional<UserDTO> findById(String userId);
    boolean existsById(String userId);
    boolean existsByEmail(String email);
    void save(UserDTO user);
    boolean existsByEmailExceptUser(@Param("userId") String userId, @Param("email") String email);
    void updateUser(UserDTO user);
} 