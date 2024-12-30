package com.example.test.service;

import com.example.test.dao.UserDao;
import com.example.test.dao.CompanyDao;
import com.example.test.dto.CompanyCreateDTO;
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
    private final CompanyDao companyDao;

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

    public boolean checkUserIdExists(String userId) {
        return userDao.existsById(userId);
    }

    @Transactional
    public void companySignup(CompanyCreateDTO dto) {
        // 비밀번호 일치 여부 확인
        if (!dto.getCompanyPw().equals(dto.getCompanyPwConfirm())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }

        // 아이디 중복 확인
        if (checkCompanyIdExists(dto.getCompanyId())) {
            throw new BadRequestException("이미 사용중인 아이디입니다");
        }

        // 이메일 중복 확인
        if (checkCompanyEmailExists(dto.getCompanyEmail())) {
            throw new BadRequestException("이미 사용중인 이메일입니다");
        }

        // 사업자등록번호 중복 확인
        if (checkCompanyNumberExists(dto.getCompanyNumber())) {
            throw new BadRequestException("이미 등록된 사업자등록번호입니다");
        }

        // 비밀번호 암호화
        dto.setCompanyPw(passwordEncoder.encode(dto.getCompanyPw()));

        companyDao.save(dto);
    }

    public boolean checkCompanyIdExists(String companyId) {
        return companyDao.existsById(companyId);
    }

    private boolean checkCompanyEmailExists(String email) {
        return companyDao.existsByEmail(email);
    }

    private boolean checkCompanyNumberExists(String number) {
        return companyDao.existsByNumber(number);
    }
} 