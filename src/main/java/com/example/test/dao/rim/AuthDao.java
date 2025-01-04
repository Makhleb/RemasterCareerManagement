package com.example.test.dao.rim;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface AuthDao {

    Optional<String> findUserByIdEmailAndPhone(@Param("userId") String userId, 
                                             @Param("email") String email, 
                                             @Param("phone") String phone);
    
    boolean existsById(@Param("userId") String userId);
    
    void updatePassword(@Param("userId") String userId, @Param("password") String password);
} 