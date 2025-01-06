package com.example.test.service.rim;

import com.example.test.dao.rim.UserDao;
import com.example.test.dao.rim.CompanyDao;
import com.example.test.dto.*;
import com.example.test.dto.rim.CompanyCreateDTO;
import com.example.test.dto.rim.LoginDTO;
import com.example.test.dto.rim.UserCreateDTO;
import com.example.test.dto.rim.UserUpdateDTO;
import com.example.test.exception.AuthException;
import com.example.test.util.rim.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.test.exception.BusinessException;
import com.example.test.dao.rim.AuthDao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final CompanyDao companyDao;
    private final JwtUtil jwtUtil;
    private final AuthDao authDao;

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
    public Map<String, String> handleLoginSuccess(String token, String userId, HttpServletResponse response) {
        log.info("Generated JWT token for {}: {}", userId, token);

        // 쿠키 설정
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("Set JWT cookie for {}", userId);

        // 응답 데이터 생성
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "로그인 성공");

        // JWT 토큰에서 권한 정보 추출
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String role = (String) claims.get("role");
        String type = role.equals("ROLE_COMPANY") ? "company" : "user";


        log.info("Set JWT cookie for {}", userId);
        responseData.put("role", role);
        responseData.put("type", type);

        return responseData;
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

    /**
     * 로그아웃 처리
     */
    public void logout(HttpServletResponse response) {
        // JWT 토큰 쿠키 삭제
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 즉시 만료
        response.addCookie(cookie);
    }


    public void resetPassword(String userId, String newPassword) {
        // 사용자 존재 여부 확인
        if (!authDao.existsById(userId)) {
            throw AuthException.authenticationFailed("존재하지 않는 사용자입니다");
        }

        // 비밀번호 암호화 및 업데이트
        String encodedPassword = passwordEncoder.encode(newPassword);
        authDao.updatePassword(userId, encodedPassword);
    }

    public String findUserByIdEmailAndPhone(String userId, String email, String phone) {
        return authDao.findUserByIdEmailAndPhone(userId, email, phone)
            .orElseThrow(() -> AuthException.authenticationFailed("일치하는 사용자 정보를 찾을 수 없습니다"));
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUser(String userId, UserUpdateDTO dto) {
        // 사용자 존재 여부 확인
        UserDTO existingUser = userDao.findById(userId)
            .orElseThrow(() -> BusinessException.notFound("사용자를 찾을 수 없습니다"));

        // 이메일 중복 체크 (현재 사용자의 이메일은 제외)
        if (userDao.existsByEmailExceptUser(userId, dto.getUserEmail())) {
            throw BusinessException.badRequest("이미 사용중인 이메일입니다");
        }

        // 사용자 정보 업데이트
        UserDTO user = new UserDTO();
        user.setUserId(userId);
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPhone(dto.getUserPhone());
        user.setUserBirth(dto.getUserBirth());
        user.setUserGender(dto.getUserGender());
        user.setUserModifyDate(LocalDateTime.now());

        userDao.updateUser(user);
    }
} 