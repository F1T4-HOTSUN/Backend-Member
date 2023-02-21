package com.ticketaka.member.service;

import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<String> signUp(SignupRequestDto dto);
    ResponseEntity<LoginResponseDto> login(LoginRequestDto dto);

    ResponseEntity<String> checkDuplicateMember(String email);

    ResponseEntity<InfoResponseDto> getInfo(Long memberId);

}
