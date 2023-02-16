package com.ticketaka.member.service;

import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MemberService {
    ResponseEntity<String> signUp(SignupRequestDto dto);
    ResponseEntity<String> login(LoginRequestDto dto);

    //ResponseEntity<String> logout(Map<String,String> header);

    ResponseEntity<String> checkDuplicateMember(String email);

    ResponseEntity<InfoResponseDto> getInfo(Long memberId);

}
