package com.example.test.service;

import com.example.test.dao.UserDao;
import com.example.test.dto.UserCreateDTO;
import com.example.test.dto.UserDTO;
import com.example.test.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserCreateDTO dto) {
        // 아이디 중복 체크
        if (userDao.existsById(dto.getUserId())) {
            throw new BadRequestException("이미 사용중인 아이디입니다");
        }

        // 이메일 중복 체크
        if (userDao.existsByEmail(dto.getUserEmail())) {
            throw new BadRequestException("이미 사용중인 이메일입니다");
        }

        UserDTO user = new UserDTO();
        user.setUserId(dto.getUserId());
        user.setUserPw(passwordEncoder.encode(dto.getUserPw()));
        user.setUserName(dto.getUserName());
        user.setUserPhone(dto.getUserPhone());
        user.setUserEmail(dto.getUserEmail());
        user.setUserBirth(dto.getUserBirth());
        user.setUserGender(dto.getUserGender());
        user.setUserRole("ROLE_USER");
        user.setUserIsActive("Y");

        userDao.save(user);
    }
} 