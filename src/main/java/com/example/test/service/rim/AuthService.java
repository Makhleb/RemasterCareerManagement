package com.example.test.service.rim;

import com.example.test.dao.rim.UserDao;
import com.example.test.dao.rim.CompanyDao;
import com.example.test.dto.*;
import com.example.test.dto.rim.CompanyCreateDTO;
import com.example.test.dto.rim.LoginDTO;
import com.example.test.dto.rim.UserCreateDTO;
import com.example.test.exception.AuthException;
import com.example.test.util.rim.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.test.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final CompanyDao companyDao;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(UserCreateDTO dto) {
        // 아이디 중복 체크
        if (userDao.existsById(dto.getUserId())) {
            throw BusinessException.badRequest("이미 사용중인 아이디입니다");
        }

        // 이메일 중복 체크
        if (userDao.existsByEmail(dto.getUserEmail())) {
            throw BusinessException.badRequest("이미 사용중인 이메일입니다");
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
            throw BusinessException.badRequest("비밀번호가 일치하지 않습니다");
        }

        // 아이디 중복 확인
        if (checkCompanyIdExists(dto.getCompanyId())) {
            throw BusinessException.badRequest("이미 사용중인 아이디입니다");
        }

        // 이메일 중복 확인
        if (checkCompanyEmailExists(dto.getCompanyEmail())) {
            throw BusinessException.badRequest("이미 사용중인 이메일입니다");
        }

        // 사업자등록번호 중복 확인
        if (checkCompanyNumberExists(dto.getCompanyNumber())) {
            throw BusinessException.badRequest("이미 등록된 사업자등록번호입니다");
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

    @Transactional
    public String login(LoginDTO dto) {
        UserDTO user = userDao.findById(dto.getUserId())
            .orElseThrow(AuthException::loginFailed);

        if (!passwordEncoder.matches(dto.getUserPw(), user.getUserPw())) {
            throw AuthException.loginFailed();
        }

        return jwtUtil.generateToken(user);
    }

    @Transactional
    public String companyLogin(LoginDTO dto) {
        CompanyDTO company = companyDao.findById(dto.getUserId())
                .orElseThrow(AuthException::loginFailed);

        if (!passwordEncoder.matches(dto.getUserPw(), company.getCompanyPw())) {
            throw AuthException.loginFailed();
        }

        return jwtUtil.generateToken(company);
    }

    /**
     * 일반 회원 정보 조회
     */
    public UserDTO getUserInfo(String userId) {
        return userDao.findById(userId)
            .orElseThrow(() -> BusinessException.notFound("로그인한 사용자를 찾을 수 없습니다"));
    }

    /**
     * 기업 회원 정보 조회
     */
    public CompanyDTO getCompanyInfo(String companyId) {
        return companyDao.findById(companyId)
            .orElseThrow(() -> BusinessException.notFound("로그인한 기업 정보를 찾을 수 없습니다"));
    }

    /**
     * 현재 사용자가 기업 회원인지 확인
     */
    public boolean isCompanyUser(String userId) {
        return companyDao.existsById(userId);
    }
} 