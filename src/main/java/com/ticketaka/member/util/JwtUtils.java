package com.ticketaka.member.util;

import com.ticketaka.member.entity.Member;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;


@Slf4j
@Component
public class JwtUtils {

    // 테스팅용 JWT 토큰 비밀번호
    private final PrivateKey privateKey;


//    public JwtUtils() {
//        Resource resource = new ClassPathResource("private-key.pem");
//
//        PrivateKey privateKey;
//        try {
//            privateKey = resource.getInputStream().toString();
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//
//        KeyPair keyPair = new KeyPair()
//        this.key = key1
//
//    }
    public JwtUtils() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Path path = Paths.get("src/main/resources/cert/private_key_pkcs8.pem");
        List<String> reads = Files.readAllLines(path);
        String content = "";
        for (String str : reads){
            content += str+"\n";
        }
        log.info(content);
        content = content.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        log.info(content);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(content));
        privateKey = kf.generatePrivate(keySpecPKCS8);
    }


    public String generateRefreshToken() {
        long now = (new Date(System.currentTimeMillis())).getTime();
        return Jwts.builder()
                .setExpiration(new Date(now + 86400000L *30))
                .signWith(privateKey,SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateAccessToken(Member member) {
        long now = (new Date(System.currentTimeMillis())).getTime();
        Map<String, String> payload = new HashMap<>();
        /*
        'iss':'Ticketaka',
        'sub':memberId,
        'aud':'AUDIENCE',
        'role': 'user',
        'permission': 'read'
         */
        payload.put("iss","Ticketaka");
        payload.put("sub", String.valueOf(member.getId()));
        payload.put("aud","AUDIENCE");
        payload.put("role", member.getRole().toString());
        payload.put("permission","all");

        // Access Token 생성 , 만료시간 30분 30*60*60
        Date accessTokenExpiresIn = new Date(now + 108000000);
        return Jwts.builder()
                .setClaims(payload)
                .setExpiration(accessTokenExpiresIn)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }


    // 토큰 정보를 검증하는 메서드
 /*   public boolean validateToken(String token) {
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
        String accessToken = request.getHeader("x-authorization");
        log.info(accessToken);
        if (StringUtils.hasText(accessToken)) {
            return accessToken; //앞에 Bearer 삭제
       }
        return null;
    }
    //  Request Header 에서 refreshToken 추출
    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("r-authorization");
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
    public String getMemberIdFromHeader(Map<String, String> header){
        return getMemberIdFromAccessToken(header.get("x-authorization"));
    }
    private String getMemberIdFromAccessToken(String token){
        // 예외처리 필요
        return parseClaims(token).get("memberId").toString();
    }*/

}
