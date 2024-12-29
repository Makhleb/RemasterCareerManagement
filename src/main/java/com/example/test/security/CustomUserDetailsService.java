package com.example.test.security;

import com.example.test.dao.UserDao;
import com.example.test.dao.CompanyDao;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
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
        // 먼저 일반 사용자(구직자) 조회
        UserDTO user = userDao.findById(username);
        if (user != null) {
            return new CustomUserDetails(
                user.getUserId(),
                user.getUserPw(),
                user.getUserRole(),
                "Y".equals(user.getUserIsActive())
            );
        }

        // 구직자가 아니면 기업 사용자 조회
        CompanyDTO company = companyDao.findById(username);
        if (company != null) {
            return new CustomUserDetails(
                company.getCompanyId(),
                company.getCompanyPw(),
                company.getCompanyRole(),
                "Y".equals(company.getCompanyIsActive())
            );
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
} 