package com.ticketaka.member.jwt;

//import com.ticketaka.member.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    // 테스팅용 JWT 토큰 비밀번호
    private final Key key;
    //@Value("${jwt.secret}") - spring.bean 의 @Value 임 lombok 아님

    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
//    public TokenInfo generateToken(Long memberId){
//        String accessToken = generateAccessToken(memberId);
//        String refreshToken = generateRefreshToken();
//
//        return TokenInfo.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    public String generateRefreshToken() {
        long now = (new Date(System.currentTimeMillis())).getTime();
        return Jwts.builder()
                .setExpiration(new Date(now + 86400000*30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Long memberId) {
        long now = (new Date(System.currentTimeMillis())).getTime();
        Map<String, String> payload = new HashMap<>();
        payload.put("memberId", String.valueOf(memberId));
        payload.put("auth","USER");
        // Access Token 생성 , 만료시간 30분 30*60*60
        Date accessTokenExpiresIn = new Date(now + 108000000);
        return Jwts.builder()
                .setClaims(payload)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            return false;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            return false;
        }
    }
    // refresh 토큰 정보를 검증하는 메서드 -> 토큰 유효성 검사 + Redis 에 해당 토큰이 저장되어있는가?
    public boolean validateRefreshToken(String token) {
        validateToken(token); // 기본적인 토큰의 유효성 검사
        // 이후 refreshToken 만의 유효성 검사 진행 -> 해당 Token 이 Redis 에 저장되어있는가?
        try{
            parseClaims(token);
        }catch (MissingClaimException ex) {
            // sub 필드가 없을때
            log.error(ex.toString());
        } catch (IncorrectClaimException ex) {
            // sub 필드가 있지만 값이 '' 가 아닐때
            log.error(ex.toString());
        }
        return false;
    }
    // Request Header 에서 accessToken추출
    public String getAccessToken(HttpServletRequest request) {
        log.info(request.toString());
        String accessToken = request.getHeader("X-Authorization");
        log.info(accessToken);
        if (StringUtils.hasText(accessToken)) {
            return accessToken; //앞에 Bearer 삭제
       }
        return null;
    }
    //  Request Header 에서 refreshToken 추출
    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("R-Authorization");
        log.info("refreshToken {}", refreshToken);
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken; //앞에 Bearer 삭제
        }
        return null;
    }
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    public Long getMemberIdFromHeader(Map<String, String> header){
        return getMemberIdFromAccessToken(header.get("X-Authorization"));
    }
    private Long getMemberIdFromAccessToken(String token){
        // 예외처리 필요
        return Long.valueOf(parseClaims(token).get("memberId").toString());
    }

}
