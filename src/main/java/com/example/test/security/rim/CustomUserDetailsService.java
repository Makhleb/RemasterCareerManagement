package com.example.test.security.rim;

import com.example.test.dao.rim.UserDao;
import com.example.test.dao.rim.CompanyDao;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
/**
 * Created on 2024-12-30 by 구경림
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserDao userDao;
    private final CompanyDao companyDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 먼저 일반 회원 조회
        Optional<UserDTO> userOpt = userDao.findById(username);
        if (userOpt.isPresent()) {
            UserDTO user = userOpt.get();
            return new User(
                user.getUserId(),
                user.getUserPw(),
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole()))
            );
        }

        // 일반 회원이 아니면 기업 회원 조회
        Optional<CompanyDTO> companyOpt = companyDao.findById(username);
        if (companyOpt.isPresent()) {
            CompanyDTO company = companyOpt.get();
            return new User(
                company.getCompanyId(),
                company.getCompanyPw(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            );
        }

        // 비회원은 GUEST 권한으로 인증 객체 생성
        return new User(
                "guest",
                "",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST"))
        );
    }
} 