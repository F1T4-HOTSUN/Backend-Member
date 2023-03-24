package com.ticketaka.member.controller;


import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.service.MemberService;
import com.ticketaka.member.util.ResponseUtils;
import feign.Response;
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
    private final ResponseUtils responseUtils;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signUp(@RequestBody SignupRequestDto dto){
        StatusCode statusCode;
        BaseResponse response;
        try{
            statusCode = memberService.signUp(dto);
            response = new BaseResponse(statusCode);
            return responseUtils.makeResponse(response);
        }catch(Exception e){
            statusCode = StatusCode.DUPLICATE_MEMBER;
            response = new BaseResponse(statusCode);
            return responseUtils.makeResponse(response);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequestDto dto){
        BaseResponse response=null;
        try{
            LoginResponseDto login = memberService.login(dto);
            response = new BaseResponse(StatusCode.OK);
            return responseUtils.makeResponse(response,login.getHeaders());
        }catch (NoSuchElementException e){
            response = new BaseResponse(StatusCode.NO_MEBMER);
            return responseUtils.makeResponse(response);
        }
    }

    // 이메일 중복 체크
    @PostMapping("/checkDuplicateEmail")
    public ResponseEntity<BaseResponse> checkDuplicateMember(@RequestBody Map<String,String> email){
        BaseResponse response = new BaseResponse(memberService.checkDuplicateMember(email.get("email")));
        return responseUtils.makeResponse(response);
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse> info(@RequestHeader Map<String,String> header){
        String memberId = header.get("memberid");
        log.info("===========");
        log.info(memberId);
        BaseResponse response;
        try{
            InfoResponseDto info = memberService.getInfo(Long.parseLong(memberId));
            response = new BaseResponse(StatusCode.OK, info);
            return responseUtils.makeResponse(response);
        }catch(NoSuchElementException e){
            response = new BaseResponse(StatusCode.NO_MEBMER);
            return responseUtils.makeResponse(response);
        }
    }
}
