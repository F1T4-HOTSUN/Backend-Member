package com.ticketaka.member.controller;


import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return memberService.login(dto);
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto dto){
        return memberService.signUp(dto);
    }
    // 이메일 중복 체크
    @PostMapping("/checkDuplicateEmail")
    public ResponseEntity<String> checkDuplicateMember(@RequestBody Map<String,String> email){
        return memberService.checkDuplicateMember(email.get("email"));
    }

    @PostMapping(path="/info")
    public ResponseEntity<InfoResponseDto> info(@RequestBody Long memberId){
        log.info("called Info");
        return memberService.getInfo(memberId);
    }

}
