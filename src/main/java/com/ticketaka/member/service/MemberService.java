package com.ticketaka.member.service;

import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;

public interface MemberService {
    StatusCode signUp(SignupRequestDto dto) throws SQLException;
    LoginResponseDto login(LoginRequestDto dto);

    StatusCode checkDuplicateMember(String email);

    InfoResponseDto getInfo(Long memberId);

}
