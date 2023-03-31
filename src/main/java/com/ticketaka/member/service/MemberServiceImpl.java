package com.ticketaka.member.service;

import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.feign.ReservationFeignClient;
import com.ticketaka.member.repository.MemberRepository;
import com.ticketaka.member.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    @Override
    @Transactional
    public StatusCode signUp(SignupRequestDto dto){
        // entity 로 변경 후 save
            Member member = memberRepository.save(dto.toEntity());
            String email = member.getEmail();
            Long id = member.getId();

            return StatusCode.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(NoSuchElementException::new);
        if (!member.getPassword().equals(dto.getPassword())) {
            throw new NoSuchElementException();
        }
        member.getId();
        String accessToken = jwtUtils.generateAccessToken(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",accessToken);
        return LoginResponseDto.builder().memberId(member.getId()).role(member.getRole()).headers(headers).build();
    }

    @Override
    @Transactional(readOnly = true)
    public StatusCode checkDuplicateMember(String email) {
        // Optional 람다로 바꿔봅시다
        log.info("email : {} ", email);
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            return StatusCode.OK;
        }
        return StatusCode.DUPLICATE_EMAIL;
    }

    @Override
    @Transactional(readOnly = true)
    public InfoResponseDto getInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);// 없을 때 예외를 떤짐
        log.info("member Info {}", member.toString());
        // memberId 로 InfoResponseDto 을 만들어 반환
        return member.toInfoResponseDto();
    }
}
