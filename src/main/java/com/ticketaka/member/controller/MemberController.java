package com.ticketaka.member.controller;


import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return memberService.login(dto);
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto dto){
        return memberService.signUp(dto);
    }
    // 이메일 중복 체크
    @GetMapping("/login")
    public ResponseEntity<String> checkDuplicateMember(@RequestParam String email){
        return memberService.checkDuplicateMember(email);
    }

//    @PostMapping(path = "/logout",headers = "HEADER")
//    public ResponseEntity<String> logout(@RequestHeader Map<String, String> header){
//        return memberService.logout(header);
//
//    }
    @GetMapping(path="/info", headers= "HEADER")
    public ResponseEntity<InfoResponseDto> info(@RequestHeader Map<String, String> header){
        log.info("called Info");
        return memberService.getInfo(header);
    }



    @GetMapping(path = "/adult",headers = "HEADER")
    public String checkAdult(@RequestHeader Map<String, String> header){

        return "adult";
    }
}
