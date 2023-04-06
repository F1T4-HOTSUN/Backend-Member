package com.ticketaka.member.service;

import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDTO;
import com.ticketaka.member.dto.request.SignupRequestDTO;
import com.ticketaka.member.dto.request.ValidateNumberDTO;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;

import java.sql.SQLException;

public interface MemberService {
    StatusCode signUp(SignupRequestDTO dto) throws SQLException;
    LoginResponseDto login(LoginRequestDTO dto);

    StatusCode checkDuplicateMember(String email);

    InfoResponseDto getInfo(Long memberId);

    StatusCode validateNumber(ValidateNumberDTO dto);

}
