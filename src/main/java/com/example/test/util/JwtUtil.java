package com.example.test.util;

import com.example.test.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2024-12-27 by 구경림
 */
@Component
public class JwtUtil {
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * application.properties 파일에서 jwt.secret 키를 가져와 사용하기 위해 @Value 어노테이션 사용
     * JWT 토큰의 서명(signature)을 생성하고 검증하는데 사용되는 비밀키
     * 토큰이 위변조되지 않았음을 보장하기 위해 필요
     * application.properties에서 설정한 값을 주입받음
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * JWT 토큰의 만료 시간을 설정하기 위한 값
     * application.properties에서 설정한 jwt.expiration 값을 주입받음
     * 토큰이 얼마나 오래 유효할지를 밀리초 단위로 지정
     * 보안을 위해 토큰의 유효기간을 제한하는 것이 중요함
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * issuer
     */
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * JWT 토큰 생성
     * 
     * @param userDTO 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDTO.getUserRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDTO.getUserId())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    /**
     * 토큰에서 사용자 ID 추출
     * 
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 토큰의 만료 시간 조회
     * 
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 토큰에서 권한(role) 정보 추출
     * 
     * @param token JWT 토큰
     * @return 사용자 권한
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * JWT 토큰 유효성 검증
     * 
     * @param token JWT 토큰
     * @return 유효성 여부
     * @throws ExpiredJwtException 토큰이 만료된 경우
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 특정 클레임 추출
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 토큰에서 모든 클레임 추출
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * 서명 키 생성
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 갱신
     * 
     * @param token 기존 JWT 토큰
     * @return 새로 발급된 JWT 토큰
     */
    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact();
    }

    /**
     * Bearer 토큰에서 JWT 추출
     * 
     * @param bearerToken Authorization 헤더 값
     * @return JWT 토큰 또는 null
     */
    public String resolveToken(String bearerToken) {
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}


