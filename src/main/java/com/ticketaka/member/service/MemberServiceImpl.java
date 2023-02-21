package com.ticketaka.member.service;

import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
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
    @Transactional(readOnly = true)
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto dto) {
        Optional<Member> member = memberRepository.findByEmail(dto.getEmail());
        if(!member.isPresent()){
            //없을 때 예외처리
        }
        // 있을때 정상 로직 처리 ->
        // access token 발급 및 refresh token 발급(따로 발급받는걸로) Refresh 토큰을 Redis 에 저장
        // 3. 인증 정보를 기반으로 JWT 토큰 (access, refresh) 생성
        // 이 로직을 auth 서버에서 진행하도록 변환

        Long memberId =  member.get().getId();
        LoginResponseDto responseDTO = LoginResponseDto.builder().memberId(memberId).build();
        return ResponseEntity.ok().body(responseDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<String> checkDuplicateMember(String email) {
        // Optional 람다로 바꿔봅시다
        log.info("email : {} ", email);
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            return ResponseEntity.ok()
                    .body("중복안됨");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail 중복");
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InfoResponseDto> getInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);// 없을 때 예외를 떤짐
        log.info("member Info {}", member.toString());
        // memberId 로 InfoResponseDto 을 만들어 반환
        return ResponseEntity.ok(member.toInfoResponseDto());
    }
}
