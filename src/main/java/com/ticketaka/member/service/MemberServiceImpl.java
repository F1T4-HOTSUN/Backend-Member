package com.ticketaka.member.service;

import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.jwt.JwtUtils;
import com.ticketaka.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;
    @Override
    @Transactional
    public ResponseEntity<String> signUp(SignupRequestDto dto) {
        // entity 로 변경 후 save
        try{
            memberRepository.save(dto.toEntity());
            return ResponseEntity.ok("SUCCESS_SIGNUP");
        }catch(Exception e){
            log.error(e.toString());
            return ResponseEntity.badRequest().body("DUPLICATE_NICKNAME");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> login(LoginRequestDto dto) {
        Optional<Member> member = memberRepository.findByEmail(dto.getEmail());
        if(!member.isPresent()){
            //없을 때 예외처리
        }
        // 있을때 정상 로직 처리 ->
        // access token 발급 및 refresh token 발급(따로 발급받는걸로) Refresh 토큰을 Redis 에 저장
        // 3. 인증 정보를 기반으로 JWT 토큰 (access, refresh) 생성
        Long memberId =  member.get().getId();
        String accessToken = jwtUtils.generateAccessToken(memberId);
        String refreshToken = jwtUtils.generateRefreshToken();


        //4. refresh 토큰을 Redis 에 저장 key - refreshToken value- memberId(String)
        redisService.setValues(refreshToken,memberId);

        // 5. 이후 토큰값을 헤더에 담아서 반환
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Authorization",accessToken);
        headers.add("R-Authorization", refreshToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body("Success");
    }

//    @Override
//    public ResponseEntity<String> logout(Map<String, String> header) {
//        // accessToken 을 SpringSecurityContext 에서 삭제 및 RefreshToken 을 Redis 에서 삭제
//        String accessToken = header.get("X-Authorization");
//        String refreshToken = header.get("R-Authorization");
//        Authentication authentication = jwtUtils.getAuthentication(accessToken);
//        redisService.deleteValue(refreshToken);
//        SecurityContextHolder.clearContext();
//
//        return null;
//    }

    @Override
    @Transactional
    public ResponseEntity<String> checkDuplicateMember(String email) {
        // Optional 람다로 바꿔봅시다,
        if(memberRepository.findByEmail(email)!=null){
            return ResponseEntity.ok()
                    .body("중복안됨");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail 중복");
    }

    @Override
    public ResponseEntity<InfoResponseDto> getInfo(Map<String, String> header) {
        // accessToken 에서 memberId 를 가져오고
        Long memberId = jwtUtils.getMemberIdFromHeader(header);
        log.info("memberId {}", memberId);
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);// 없을 때 예외를 떤짐
        log.info("member Info {}", member.toString());
        // memberId 로 InfoResponseDto 을 만들어 반환
        return ResponseEntity.ok(member.toInfoResponseDto());
    }
}
