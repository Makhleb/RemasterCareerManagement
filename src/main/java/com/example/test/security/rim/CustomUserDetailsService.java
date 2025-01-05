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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created on 2024-12-30 by 구경림
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserDao userDao;
    private final CompanyDao companyDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 일반 회원 조회
        Optional<UserDTO> userOpt = userDao.findById(username);
        if (userOpt.isPresent()) {
            UserDTO user = userOpt.get();
            return new CustomUserDetails(
                user.getUserId(),
                user.getUserPw(),
                "ROLE_USER",
                true,
                user.getUserName()
            );
        }

        // 기업 회원 조회
        Optional<CompanyDTO> companyOpt = companyDao.findById(username);
        if (companyOpt.isPresent()) {
            CompanyDTO company = companyOpt.get();
            log.info("Company found - id: {}, name: {}", company.getCompanyId(), company.getCompanyName());
            
            CustomUserDetails userDetails = CustomUserDetails.builder()
                .username(company.getCompanyId())
                .password(company.getCompanyPw())
                .role("ROLE_COMPANY")
                .name(company.getCompanyName())
                .isActive(true)
                .build();
            
            log.info("CustomUserDetails created - username: {}, role: {}, name: {}", 
                userDetails.getUsername(), userDetails.getRole(), userDetails.getName());
                
            return userDetails;
        }

        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
    }
} 