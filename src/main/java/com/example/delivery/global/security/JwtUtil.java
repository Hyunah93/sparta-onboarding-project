package com.example.delivery.global.security;

import com.example.delivery.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header 키
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 키
    public static final String AUTHORIZATION_KEY = "auth";

    // 토큰 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    // 암호화할 때 String 형태의 비밀키를 JWT 서명에 사용할 수 있도록 SecretKey 객체로 변환
    @Value("${jwt.secret.key}")
    private String secretKey;
    private SecretKey key;

    // 설정 파일에 잇는 비밀키 문자열을 실제 암호화에 사용할 Key 객체로 준비하는 과정
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // 문자열 형태의 Base64 값을 byte 배열로 바꿈
        key = Keys.hmacShaKeyFor(bytes);
    }

    //토큰 생성
    public String createToken(String userId, UserRoleEnum role){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId)             // 식별자ID
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, Jwts.SIG.HS256) // 암호화 알고리즘 (해당 JWT를 내가 가진 비밀키로 만들고, 나중에 내가 만든 토큰인지 확인 가능)
                        .compact(); // 지금까지 만든 JWT를 문자열로 만들어줌
    }

    // Header에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){ // 값이 실존하는지 && Bearer 로 시작하는지 확인
            return bearerToken.substring(7);
        }
        return null;
    }

    //토큰 검증
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (SecurityException | MalformedJwtException e){
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");

        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");

        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");

        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}