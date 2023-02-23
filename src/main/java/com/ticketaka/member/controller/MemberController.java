package com.ticketaka.member.controller;


import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public BaseResponse signUp(@RequestBody SignupRequestDto dto){
        StatusCode statusCode;
        try{
            statusCode = memberService.signUp(dto);
        }catch(Exception e){
            statusCode = StatusCode.DUPLICATE_MEMBER;
            log.info("에러코드 + {} ", statusCode.toString());
        }

        return new BaseResponse(statusCode);
    }
    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequestDto dto){
        try{
            memberService.login(dto);
            return new BaseResponse(StatusCode.OK, memberService.login(dto));
        }catch (NoSuchElementException e){
            return new BaseResponse(StatusCode.NO_MEBMER);
        }
    }

    // 이메일 중복 체크
    @PostMapping("/checkDuplicateEmail")
    public BaseResponse checkDuplicateMember(@RequestBody Map<String,String> email){
        return new BaseResponse(memberService.checkDuplicateMember(email.get("email")));
    }

    @PostMapping(path="/info")
    public BaseResponse info(@RequestBody Long memberId){
        log.info("called Info");
        try{
            InfoResponseDto info = memberService.getInfo(memberId);
            return new BaseResponse(StatusCode.OK, info);
        }catch(NoSuchElementException e){
            return new BaseResponse(StatusCode.NO_MEBMER);
        }
    }

}
